package pl.holowinska.rentnowbackend.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import pl.holowinska.rentnowbackend.model.entities.*;
import pl.holowinska.rentnowbackend.model.enums.ConvenienceType;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ConvenienceRepositoryTest extends IntegrationTest {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    AccommodationRepository accommodationRepository;

    @Autowired
    ConvenienceRepository convenienceRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void convenienceShouldBeSaved() {
        //given
        Convenience convenience = new Convenience();
        ConvenienceId id = new ConvenienceId(getAccommodation(), ConvenienceType.BALCONY);
        convenience.setId(id);

        //when
        Convenience saved = convenienceRepository.save(convenience);
        Optional<Convenience> byId = convenienceRepository.findById(saved.getId());

        //then
        assertTrue(byId.isPresent());
    }

    @Test
    public void convenienceShouldNotBeSaved() {
        //given
        Convenience convenience = new Convenience();

        //when
        //then
        assertThrows(JpaSystemException.class, () -> {
            convenienceRepository.save(convenience);
            convenienceRepository.flush();
        });
    }

    @Test
    public void convenienceShouldBeDeleted() {
        //given
        Convenience convenience = new Convenience();
        ConvenienceId id = new ConvenienceId(getAccommodation(), ConvenienceType.BALCONY);
        convenience.setId(id);

        //when
        Convenience saved = convenienceRepository.save(convenience);
        convenienceRepository.delete(saved);
        Optional<Convenience> byId = convenienceRepository.findById(saved.getId());

        //then
        assertFalse(byId.isPresent());
    }

    @Test
    public void accommodationShouldNotBeDeletedWithConvenience() {
        //given
        Convenience convenience = new Convenience();
        ConvenienceId id = new ConvenienceId(getAccommodation(), ConvenienceType.BALCONY);
        convenience.setId(id);

        //when
        Convenience saved = convenienceRepository.save(convenience);
        Accommodation accommodation = saved.getId().getAccommodation();
        convenienceRepository.delete(saved);
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
