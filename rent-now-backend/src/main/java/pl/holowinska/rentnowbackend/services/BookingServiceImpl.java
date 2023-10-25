package pl.holowinska.rentnowbackend.services;

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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Optional;

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
        bookingConvenienceRepository.deleteConveniencesByBookingId(bookingId);
        bookingRepository.deleteById(bookingId);
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


}
