package pl.holowinska.rentnowbackend.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import pl.holowinska.rentnowbackend.model.entities.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FavouriteObjectRepositoryTest extends IntegrationTest {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    AccommodationRepository accommodationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FavouriteObjectRepository favouriteObjectRepository;

    @Test
    public void favouriteObjectShouldBeSaved() {
        //given
        FavouriteObject favouriteObject = new FavouriteObject();
        FavouriteObjectId id = new FavouriteObjectId(getUser(), getAccommodation());
        favouriteObject.setId(id);

        //when
        FavouriteObject saved = favouriteObjectRepository.save(favouriteObject);
        Optional<FavouriteObject> byId = favouriteObjectRepository.findById(saved.getId());

        //then
        assertTrue(byId.isPresent());
    }

    @Test
    public void favouriteObjectShouldNotBeSaved() {
        //given
        FavouriteObject favouriteObject = new FavouriteObject();

        //when
        //then
        assertThrows(JpaSystemException.class, () -> {
            favouriteObjectRepository.save(favouriteObject);
            favouriteObjectRepository.flush();
        });
    }

    @Test
    public void favouriteObjectShouldBeDeleted() {
        //given
        FavouriteObject favouriteObject = new FavouriteObject();
        FavouriteObjectId id = new FavouriteObjectId(getUser(), getAccommodation());
        favouriteObject.setId(id);

        //when
        FavouriteObject saved = favouriteObjectRepository.save(favouriteObject);
        favouriteObjectRepository.delete(saved);
        Optional<FavouriteObject> byId = favouriteObjectRepository.findById(saved.getId());

        //then
        assertFalse(byId.isPresent());
    }

    @Test
    public void userShouldNotBeDeletedWithFavouriteObject() {
        //given
        FavouriteObject favouriteObject = new FavouriteObject();
        FavouriteObjectId id = new FavouriteObjectId(getUser(), getAccommodation());
        favouriteObject.setId(id);

        //when
        FavouriteObject saved = favouriteObjectRepository.save(favouriteObject);
        User user = saved.getId().getUser();
        favouriteObjectRepository.delete(saved);
        Optional<User> byId = userRepository.findById(user.getId());

        //then
        assertTrue(byId.isPresent());
    }

    @Test
    public void accommodationShouldNotBeDeletedWithFavouriteObject() {
        //given
        FavouriteObject favouriteObject = new FavouriteObject();
        FavouriteObjectId id = new FavouriteObjectId(getUser(), getAccommodation());
        favouriteObject.setId(id);

        //when
        FavouriteObject saved = favouriteObjectRepository.save(favouriteObject);
        Accommodation accommodation = saved.getId().getAccommodation();
        favouriteObjectRepository.delete(saved);
        Optional<Accommodation> byId = accommodationRepository.findById(accommodation.getId());

        //then
        assertTrue(byId.isPresent());
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