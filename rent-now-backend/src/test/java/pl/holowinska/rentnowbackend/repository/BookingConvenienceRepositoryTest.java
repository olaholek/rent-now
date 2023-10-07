package pl.holowinska.rentnowbackend.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import pl.holowinska.rentnowbackend.model.entities.*;
import pl.holowinska.rentnowbackend.model.enums.ConvenienceType;
import pl.holowinska.rentnowbackend.model.enums.Status;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BookingConvenienceRepositoryTest extends IntegrationTest {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    AccommodationRepository accommodationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    BookingConvenienceRepository bookingConvenienceRepository;

    @Test
    public void bookingConvenienceShouldBeSaved() {
        //given
        BookingConvenience bookingConvenience = new BookingConvenience();
        BookingConvenienceId id = new BookingConvenienceId(getBooking(), ConvenienceType.BALCONY);
        bookingConvenience.setId(id);

        //when
        BookingConvenience saved = bookingConvenienceRepository.save(bookingConvenience);
        Optional<BookingConvenience> byId = bookingConvenienceRepository.findById(saved.getId());

        //then
        assertTrue(byId.isPresent());
    }

    @Test
    public void bookingConvenienceShouldNotBeSaved() {
        //given
        BookingConvenience bookingConvenience = new BookingConvenience();

        //when
        //then
        assertThrows(JpaSystemException.class, () -> {
            bookingConvenienceRepository.save(bookingConvenience);
            bookingConvenienceRepository.flush();
        });
    }

    @Test
    public void bookingConvenienceShouldBeDeleted() {
        //given
        BookingConvenience bookingConvenience = new BookingConvenience();
        BookingConvenienceId id = new BookingConvenienceId(getBooking(), ConvenienceType.BALCONY);
        bookingConvenience.setId(id);

        //when
        BookingConvenience saved = bookingConvenienceRepository.save(bookingConvenience);
        bookingConvenienceRepository.delete(saved);
        Optional<BookingConvenience> byId = bookingConvenienceRepository.findById(saved.getId());

        //then
        assertFalse(byId.isPresent());
    }

    @Test
    public void bookingShouldNotBeDeletedWithBookingConvenience() {
        //given
        BookingConvenience bookingConvenience = new BookingConvenience();
        BookingConvenienceId id = new BookingConvenienceId(getBooking(), ConvenienceType.BALCONY);
        bookingConvenience.setId(id);

        //when
        BookingConvenience saved = bookingConvenienceRepository.save(bookingConvenience);
        Booking booking = saved.getId().getBooking();
        bookingConvenienceRepository.delete(saved);
        Optional<Booking> byId = bookingRepository.findById(booking.getId());

        //then
        assertTrue(byId.isPresent());
    }

    private Booking getBooking() {
        Booking booking = new Booking();
        booking.setUser(getUser());
        booking.setStatus(Status.BOOKED);
        booking.setAccommodation(getAccommodation());
        booking.setBookingDate(Timestamp.valueOf(LocalDateTime.now()));
        booking.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        booking.setEndDate(Timestamp.valueOf(LocalDateTime.now().plusMonths(2)));
        booking.setPrice(getPrice(booking.getStartDate(), booking.getEndDate(), new BigDecimal(160)));
        return bookingRepository.save(booking);
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
