package pl.holowinska.rentnowbackend.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import pl.holowinska.rentnowbackend.model.entities.Accommodation;
import pl.holowinska.rentnowbackend.model.entities.Address;
import pl.holowinska.rentnowbackend.model.entities.User;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccommodationRepositoryTest extends IntegrationTest {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    AccommodationRepository accommodationRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void accommodationShouldBeSaved() {
        //given
        Accommodation accommodation = new Accommodation();
        accommodation.setAddress(getAddress());
        accommodation.setSquareFootage(new BigDecimal(30));
        accommodation.setUser(getUser());
        accommodation.setDescription("Piękna okolica blisko centrum");
        accommodation.setPriceForDay(new BigDecimal(160));

        //when
        Accommodation saved = accommodationRepository.save(accommodation);
        Optional<Accommodation> byId = accommodationRepository.findById(saved.getId());

        //then
        assertTrue(byId.isPresent());
    }

    @Test
    public void accommodationShouldNotBeSaved() {
        //given
        Accommodation accommodation = new Accommodation();
        accommodation.setAddress(getAddress());
        accommodation.setSquareFootage(new BigDecimal(30));
        accommodation.setUser(getUser());
        accommodation.setDescription("Piękna okolica blisko centrum");

        //when
        //then
        assertThrows(DataIntegrityViolationException.class, () -> {
            accommodationRepository.save(accommodation);
            accommodationRepository.flush();
        });
    }

    @Test
    public void accommodationShouldBeDeleted() {
        //given
        Accommodation accommodation = new Accommodation();
        accommodation.setAddress(getAddress());
        accommodation.setSquareFootage(new BigDecimal(30));
        accommodation.setUser(getUser());
        accommodation.setDescription("Piękna okolica blisko centrum");
        accommodation.setPriceForDay(new BigDecimal(160));

        //when
        Accommodation saved = accommodationRepository.save(accommodation);
        accommodationRepository.delete(saved);
        Optional<Accommodation> byId = accommodationRepository.findById(saved.getId());

        //then
        assertFalse(byId.isPresent());
    }

    @Test
    public void accommodationShouldBeUpdated() {
        //given
        Accommodation accommodation = new Accommodation();
        accommodation.setAddress(getAddress());
        accommodation.setSquareFootage(new BigDecimal(30));
        accommodation.setUser(getUser());
        accommodation.setDescription("Piękna okolica blisko centrum");
        accommodation.setPriceForDay(new BigDecimal(160));

        //when
        Accommodation saved = accommodationRepository.save(accommodation);
        saved.setSquareFootage(new BigDecimal(40));
        Accommodation updated = accommodationRepository.save(saved);

        //then
        assertEquals(updated.getSquareFootage(), 40);
    }

    @Test
    public void addressShouldBeDeletedWithAccommodation() {
        //given
        Accommodation accommodation = new Accommodation();
        accommodation.setAddress(getAddress());
        accommodation.setSquareFootage(new BigDecimal(30));
        accommodation.setUser(getUser());
        accommodation.setDescription("Piękna okolica blisko centrum");
        accommodation.setPriceForDay(new BigDecimal(160));

        //when
        Accommodation saved = accommodationRepository.save(accommodation);
        Address address = saved.getAddress();
        accommodationRepository.delete(saved);
        Optional<Address> byId = addressRepository.findById(address.getId());

        //then
        assertFalse(byId.isPresent());
    }

    @Test
    public void userShouldNotBeDeletedWithAccommodation() {
        //given
        Accommodation accommodation = new Accommodation();
        accommodation.setAddress(getAddress());
        accommodation.setSquareFootage(new BigDecimal(30));
        accommodation.setUser(getUser());
        accommodation.setDescription("Piękna okolica blisko centrum");
        accommodation.setPriceForDay(new BigDecimal(160));

        //when
        Accommodation saved = accommodationRepository.save(accommodation);
        User user = saved.getUser();
        accommodationRepository.delete(saved);
        Optional<User> byId = userRepository.findById(user.getId());

        //then
        assertTrue(byId.isPresent());
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

    private User getUser() {
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setId(uuid);
        user.setFirstName("Ola");
        user.setLastName("Holo");
        user.setPhoneNumber("675534211");
        return userRepository.save(user);
    }
}