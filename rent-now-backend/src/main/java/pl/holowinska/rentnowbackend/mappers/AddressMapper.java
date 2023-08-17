package pl.holowinska.rentnowbackend.mappers;

import pl.holowinska.rentnowbackend.model.entities.Address;
import pl.holowinska.rentnowbackend.model.rq.AddressRQ;
import pl.holowinska.rentnowbackend.model.rs.AddressRS;

public class AddressMapper {

    public static Address mapToEntity(AddressRQ addressRQ) {
        if (addressRQ == null) return null;
        Address address = new Address();
        address.setProvince(addressRQ.getProvince());
        address.setCountry(addressRQ.getCountry());
        address.setPost(addressRQ.getPost());
        address.setStreet(addressRQ.getStreet());
        address.setPostalCode(addressRQ.getPostalCode());
        address.setHouseNumber(addressRQ.getHouseNumber());
        address.setApartmentNumber(addressRQ.getApartmentNumber());
        address.setCounty(addressRQ.getCounty());
        address.setCity(addressRQ.getCity());
        return address;
    }

    public static AddressRS mapToDto(Address address) {
        if (address == null) return null;
        return AddressRS.builder()
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .post(address.getPost())
                .apartmentNumber(address.getApartmentNumber())
                .county(address.getCounty())
                .city(address.getCity())
                .province(address.getProvince())
                .street(address.getStreet())
                .houseNumber(address.getHouseNumber())
                .build();
    }
}
