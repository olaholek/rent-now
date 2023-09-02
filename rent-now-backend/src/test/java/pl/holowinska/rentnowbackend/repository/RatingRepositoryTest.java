package pl.holowinska.rentnowbackend.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import pl.holowinska.rentnowbackend.model.entities.Accommodation;
import pl.holowinska.rentnowbackend.model.entities.Address;
import pl.holowinska.rentnowbackend.model.entities.Rating;
import pl.holowinska.rentnowbackend.model.entities.User;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RatingRepositoryTest extends IntegrationTest {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    AccommodationRepository accommodationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RatingRepository ratingRepository;

    @Test
    public void ratingShouldBeSaved() {
        //given
        Rating rating = new Rating();
        rating.setUser(getUser());
        rating.setAccommodation(getAccommodation());
        rating.setGrade(8L);

        //when
        Rating saved = ratingRepository.save(rating);
        Optional<Rating> byId = ratingRepository.findById(saved.getId());

        //then
        assertTrue(byId.isPresent());
    }

    @Test
    public void ratingShouldNotBeSaved() {
        //given
        Rating rating = new Rating();
        rating.setUser(getUser());
        rating.setAccommodation(getAccommodation());

        //when
        //then
        assertThrows(DataIntegrityViolationException.class, () -> {
            ratingRepository.save(rating);
            ratingRepository.flush();
        });
    }

    @Test
    public void ratingShouldBeDeleted() {
        //given
        Rating rating = new Rating();
        rating.setUser(getUser());
        rating.setAccommodation(getAccommodation());
        rating.setGrade(8L);

        //when
        Rating saved = ratingRepository.save(rating);
        ratingRepository.delete(saved);
        Optional<Rating> byId = ratingRepository.findById(saved.getId());

        //then
        assertFalse(byId.isPresent());
    }

    @Test
    public void userShouldNotBeDeletedWithRating() {
        //given
        Rating rating = new Rating();
        rating.setUser(getUser());
        rating.setAccommodation(getAccommodation());
        rating.setGrade(8L);

        //when
        Rating saved = ratingRepository.save(rating);
        User user = saved.getUser();
        ratingRepository.delete(saved);
        Optional<User> byId = userRepository.findById(user.getId());

        //then
        assertTrue(byId.isPresent());
    }

    @Test
    public void accommodationShouldNotBeDeletedWithRating() {
        //given
        Rating rating = new Rating();
        rating.setUser(getUser());
        rating.setAccommodation(getAccommodation());
        rating.setGrade(8L);

        //when
        Rating saved = ratingRepository.save(rating);
        Accommodation accommodation = saved.getAccommodation();
        ratingRepository.delete(saved);
        Optional<Accommodation> byId = accommodationRepository.findById(accommodation.getId());

        //then
        assertTrue(byId.isPresent());
    }

    @Test
    public void ratingShouldBeUpdated() {
        //given
        Rating rating = new Rating();
        rating.setUser(getUser());
        rating.setAccommodation(getAccommodation());
        rating.setGrade(8L);

        //when
        Rating saved = ratingRepository.save(rating);
        saved.setGrade(7L);
        Rating updated = ratingRepository.save(saved);

        //then
        assertEquals(updated.getGrade(), 7L);
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