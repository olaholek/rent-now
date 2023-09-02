package pl.holowinska.rentnowbackend.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import pl.holowinska.rentnowbackend.model.entities.Accommodation;
import pl.holowinska.rentnowbackend.model.entities.Address;
import pl.holowinska.rentnowbackend.model.entities.Booking;
import pl.holowinska.rentnowbackend.model.entities.User;
import pl.holowinska.rentnowbackend.model.enums.Status;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingRepositoryTest extends IntegrationTest {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    AccommodationRepository accommodationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Test
    public void bookingShouldBeSaved() {
        //given
        Accommodation accommodation = getAccommodation();
        Booking booking = new Booking();
        booking.setUser(getUser());
        booking.setStatus(Status.BOOKED);
        booking.setAccommodation(accommodation);
        booking.setBookingDate(Timestamp.valueOf(LocalDateTime.now()));
        booking.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        booking.setEndDate(Timestamp.valueOf(LocalDateTime.now().plusMonths(2)));
        booking.setPrice(getPrice(booking.getStartDate(), booking.getEndDate(), accommodation.getPriceForDay()));

        //when
        Booking saved = bookingRepository.save(booking);
        Optional<Booking> byId = bookingRepository.findById(saved.getId());

        //then
        assertTrue(byId.isPresent());
    }

    @Test
    public void bookingShouldNotBeSaved() {
        //given
        Accommodation accommodation = getAccommodation();
        Booking booking = new Booking();
        booking.setAccommodation(accommodation);
        booking.setStatus(Status.BOOKED);
        booking.setBookingDate(Timestamp.valueOf(LocalDateTime.now()));
        booking.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        booking.setEndDate(Timestamp.valueOf(LocalDateTime.now().plusMonths(2)));
        booking.setPrice(getPrice(booking.getStartDate(), booking.getEndDate(), accommodation.getPriceForDay()));

        //when
        //then
        assertThrows(DataIntegrityViolationException.class, () -> {
            bookingRepository.save(booking);
            bookingRepository.flush();
        });
    }

    @Test
    public void bookingShouldBeDeleted() {
        //given
        Accommodation accommodation = getAccommodation();
        Booking booking = new Booking();
        booking.setUser(getUser());
        booking.setStatus(Status.BOOKED);
        booking.setAccommodation(accommodation);
        booking.setBookingDate(Timestamp.valueOf(LocalDateTime.now()));
        booking.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        booking.setEndDate(Timestamp.valueOf(LocalDateTime.now().plusMonths(2)));
        booking.setPrice(getPrice(booking.getStartDate(), booking.getEndDate(), accommodation.getPriceForDay()));

        //when
        Booking saved = bookingRepository.save(booking);
        bookingRepository.delete(saved);
        Optional<Booking> byId = bookingRepository.findById(saved.getId());

        //then
        assertFalse(byId.isPresent());
    }

    @Test
    public void userShouldNotBeDeletedWithBooking() {
        //given
        Accommodation accommodation = getAccommodation();
        Booking booking = new Booking();
        booking.setUser(getUser());
        booking.setStatus(Status.BOOKED);
        booking.setAccommodation(accommodation);
        booking.setBookingDate(Timestamp.valueOf(LocalDateTime.now()));
        booking.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        booking.setEndDate(Timestamp.valueOf(LocalDateTime.now().plusMonths(2)));
        booking.setPrice(getPrice(booking.getStartDate(), booking.getEndDate(), accommodation.getPriceForDay()));

        //when
        Booking saved = bookingRepository.save(booking);
        User user = saved.getUser();
        bookingRepository.delete(saved);
        Optional<User> byId = userRepository.findById(user.getId());

        //then
        assertTrue(byId.isPresent());
    }

    @Test
    public void accommodationShouldNotBeDeletedWithBooking() {
        //given
        Accommodation accommodation = getAccommodation();
        Booking booking = new Booking();
        booking.setUser(getUser());
        booking.setStatus(Status.BOOKED);
        booking.setAccommodation(accommodation);
        booking.setBookingDate(Timestamp.valueOf(LocalDateTime.now()));
        booking.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        booking.setEndDate(Timestamp.valueOf(LocalDateTime.now().plusMonths(2)));
        booking.setPrice(getPrice(booking.getStartDate(), booking.getEndDate(), accommodation.getPriceForDay()));

        //when
        Booking saved = bookingRepository.save(booking);
        bookingRepository.delete(saved);
        Optional<Accommodation> byId = accommodationRepository.findById(accommodation.getId());

        //then
        assertTrue(byId.isPresent());
    }

    private BigDecimal getPrice(Timestamp startDate, Timestamp endDate, BigDecimal priceForDay) {
        long numberOfDays = TimeUnit.MILLISECONDS.toDays(endDate.getTime() - startDate.getTime());
        return priceForDay.multiply(new BigDecimal(numberOfDays));
    }

    private Accommodation getAccommodation() {
        Accommodation accommodation = new Accommodation();
        accommodation.setAddress(getAddress());
        accommodation.setUser(getUser());
        accommodation.setSquareFootage(BigDecimal.valueOf(30));
        accommodation.setDescription("Piękna okolica blisko centrum");
        accommodation.setPriceForDay(new BigDecimal(160));
        return accommodationRepository.save(accommodation);
    }

    private User getUser() {
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setId(uuid);
        user.setFirstName("Ola");
        user.setLastName("Holo");
        user.setPhoneNumber("675534211");
        return userRepository.save(user);
    }

    private Address getAddress() {
        Address address = new Address();
        address.setCity("Krakow");
        address.setCountry("PL");
        address.setPostalCode("12-123");
        address.setPost("Krakow Lobzow");
        address.setProvince("Malopolskie");
        address.setCounty("krakowski");
        address.setStreet("Wybickiego");
        address.setApartmentNumber("106");
        address.setHouseNumber("56");
        return addressRepository.save(address);
    }
}