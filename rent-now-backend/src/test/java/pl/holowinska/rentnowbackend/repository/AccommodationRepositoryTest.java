package pl.holowinska.rentnowbackend.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.holowinska.rentnowbackend.model.entities.Accommodation;
import pl.holowinska.rentnowbackend.model.entities.Address;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class AccommodationRepositoryTest extends IntegrationTest {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    AccommodationRepository accommodationRepository;

    @Test
    public void accommodationShouldBeSaved() {
        //given
        Accommodation accommodation = new Accommodation();
        accommodation.setAddress(getAddress());
        accommodation.setSquareFootage(30);
        accommodation.setDescription("Piękna okolica blisko centrum");
        accommodation.setPriceForDay(new BigDecimal(160));

        //when
        Accommodation saved = accommodationRepository.save(accommodation);
        Optional<Accommodation> byId = accommodationRepository.findById(saved.getId());

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
        return address;
    }
}