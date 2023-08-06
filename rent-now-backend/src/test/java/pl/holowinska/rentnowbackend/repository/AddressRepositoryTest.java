package pl.holowinska.rentnowbackend.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import pl.holowinska.rentnowbackend.model.entities.Address;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AddressRepositoryTest extends IntegrationTest {

    @Autowired
    AddressRepository addressRepository;

    @Test
    public void addressShouldBeSaved() {
        //given
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

        //when
        Address saved = addressRepository.save(address);
        Optional<Address> byId = addressRepository.findById(saved.getId());

        //then
        assertTrue(byId.isPresent());
    }

    @Test
    public void addressShouldNotBeSaved() {
        //given
        Address address = new Address();
        address.setCity("Krakow");
        address.setCountry("PL");
        address.setPostalCode("12-123");
        address.setPost("Krakow Lobzow");
        address.setProvince("Malopolskie");
        address.setCounty("krakowski");
        address.setStreet("Wybickiego");
        address.setApartmentNumber("106");

        //when
        //then
        assertThrows(DataIntegrityViolationException.class,
                () -> addressRepository.save(address));
    }

    @Test
    public void addressShouldBeDeleted() {
        //given
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

        //when
        Address saved = addressRepository.save(address);
        addressRepository.delete(saved);
        Optional<Address> byId = addressRepository.findById(saved.getId());

        //then
        assertFalse(byId.isPresent());
    }

    @Test
    public void addressShouldBeUpdated() {
        //given
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

        //when
        Address saved = addressRepository.save(address);
        saved.setCity("Warszawa");
        Address updated = addressRepository.save(address);

        //then
        assertEquals(updated.getCity(), "Warszawa");
    }
}