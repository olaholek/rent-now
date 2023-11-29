package pl.holowinska.rentnowbackend.services;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.holowinska.rentnowbackend.exceptions.AccommodationNotFoundException;
import pl.holowinska.rentnowbackend.exceptions.BookingConflictException;
import pl.holowinska.rentnowbackend.exceptions.BookingNotFoundException;
import pl.holowinska.rentnowbackend.mappers.BookingMapper;
import pl.holowinska.rentnowbackend.model.entities.*;
import pl.holowinska.rentnowbackend.model.enums.ConvenienceType;
import pl.holowinska.rentnowbackend.model.enums.Status;
import pl.holowinska.rentnowbackend.model.rq.BookingRQ;
import pl.holowinska.rentnowbackend.model.rs.BookingRS;
import pl.holowinska.rentnowbackend.repository.AccommodationRepository;
import pl.holowinska.rentnowbackend.repository.BookingConvenienceRepository;
import pl.holowinska.rentnowbackend.repository.BookingRepository;
import pl.holowinska.rentnowbackend.repository.UserRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final AccommodationRepository accommodationRepository;
    private final BookingConvenienceRepository bookingConvenienceRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository,
                              AccommodationRepository accommodationRepository,
                              BookingConvenienceRepository bookingConvenienceRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.accommodationRepository = accommodationRepository;
        this.bookingConvenienceRepository = bookingConvenienceRepository;
    }

    @Override
    public BookingRS addBooking(BookingRQ bookingRQ) throws AccommodationNotFoundException, BookingConflictException {
        User user = userRepository.findById(bookingRQ.getUserUUID())
                .orElse(userRepository.save(new User(bookingRQ.getUserUUID())));
        Optional<Accommodation> accommodation = accommodationRepository.findById(bookingRQ.getAccommodationId());
        if (accommodation.isEmpty()) {
            throw new AccommodationNotFoundException();
        }
        if (bookingRQ.getStartDate() == null || bookingRQ.getEndDate() == null || bookingRQ.getStartDate().isAfter(bookingRQ.getEndDate())) {
            throw new IllegalArgumentException();
        }
        if (!bookingRepository.isBookingAvailable(bookingRQ.getStartDate(), bookingRQ.getEndDate(), accommodation.get().getId())) {
            throw new BookingConflictException();
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setAccommodation(accommodation.get());
        booking.setStartDate(Timestamp.valueOf(LocalDateTime.of(bookingRQ.getStartDate(), LocalTime.of(15, 0, 0))));
        booking.setEndDate(Timestamp.valueOf(LocalDateTime.of(bookingRQ.getEndDate(), LocalTime.of(10, 0, 0))));
        booking.setBookingDate(Timestamp.valueOf(LocalDateTime.now()));
        booking.setPrice(bookingRQ.getPrice());
        booking.setStatus(Status.BOOKED);
        Booking saved = bookingRepository.save(booking);

        setAndSaveConveniences(bookingRQ.getConveniences(), saved);

        return BookingMapper.mapToDto(saved, bookingRQ.getConveniences());
    }

    @Override
    public void deleteBooking(Long bookingId) throws BookingNotFoundException {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new BookingNotFoundException();
        }
        Booking entity = optionalBooking.get();
        entity.setStatus(Status.CANCELED);
        bookingRepository.save(entity);
    }

    private HashMap<ConvenienceType, BigDecimal> setAndSaveConveniences(HashMap<ConvenienceType, BigDecimal> conveniences, Booking saved) {
        if (conveniences != null) {
            for (ConvenienceType type : conveniences.keySet()) {
                BookingConvenience bookingConvenience = new BookingConvenience();
                BookingConvenienceId id = new BookingConvenienceId(saved, type);
                bookingConvenience.setPrice(conveniences.get(type));
                bookingConvenience.setId(id);
                bookingConvenienceRepository.save(bookingConvenience);
            }
        }
        return conveniences;
    }

    @Override
    public BookingRS getBooking(Long bookingId) throws BookingNotFoundException {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new BookingNotFoundException();
        }
        HashMap<ConvenienceType, BigDecimal> conveniences = getConveniences(bookingId);
        return BookingMapper.mapToDto(booking.get(), conveniences);
    }

    @Override
    public Page<BookingRS> getBookingListByUser(String uuid, Pageable pageable) {
        return bookingRepository.findAll(bookingByUser(uuid), pageable)
                .map(entity -> BookingMapper.mapToDto(entity, getConveniences(entity.getId())));
    }

    @Override
    public Page<BookingRS> getBookingListByAccommodation(String accommodationId, Pageable pageable) {
        return bookingRepository.findAll(bookingByAccommodation(accommodationId), pageable)
                .map(entity -> BookingMapper.mapToDto(entity, getConveniences(entity.getId())));
    }

    @Override
    public List<LocalDate> getBookedStartDatesByAccommodation(String accommodationId) {
        List<Booking> bookings = bookingRepository.findAll(activeBookingByAccommodation(accommodationId));

        List<LocalDate> bookedStartDates = new ArrayList<>();
        bookings.forEach(booking -> {
            LocalDate iterDate = booking.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endDate = booking.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            while (iterDate.isBefore(endDate)) {
                bookedStartDates.add(iterDate);
                iterDate = iterDate.plusDays(1);
            }
        });
        return bookedStartDates;
    }

    @Override
    public List<LocalDate> getBookedEndDatesByAccommodation(String accommodationId) {
        List<Booking> bookings = bookingRepository.findAll(activeBookingByAccommodation(accommodationId));

        List<LocalDate> bookedEndDates = new ArrayList<>();
        bookings.forEach(booking -> {
            LocalDate iterDate = booking.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate startDate = booking.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();


            while (iterDate.isAfter(startDate)) {
                bookedEndDates.add(iterDate);
                iterDate = iterDate.minusDays(1);
            }
        });
        return bookedEndDates;
    }

    private Specification<Booking> bookingByAccommodation(String accommodationId) {
        return (root, query, cb) -> cb.equal(root.get("accommodation").get("id"), accommodationId);
    }

    private Specification<Booking> activeBookingByAccommodation(String accommodationId) {
        return (root, query, cb) -> {
            Predicate spec = cb.equal(root.get("accommodation").get("id"), accommodationId);
            spec = cb.and(spec, cb.or(cb.equal(root.get("status"), Status.BOOKED),
                    cb.equal(root.get("status"), Status.PENDING)));
            return spec;
        };
    }

    private Specification<Booking> bookingByUser(String uuid) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), UUID.fromString(uuid));
    }

    private HashMap<ConvenienceType, BigDecimal> getConveniences(Long bookingId) {
        List<BookingConvenience> convenienceList = bookingConvenienceRepository.getConvenienceByBookingId(bookingId);
        HashMap<ConvenienceType, BigDecimal> conveniences = new HashMap<>();
        for (BookingConvenience convenience : convenienceList) {
            conveniences.put(convenience.getId().getConvenienceType(), convenience.getPrice());
        }
        return conveniences;
    }
}
