package pl.holowinska.rentnowbackend.model.rs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressRS {
    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private String apartmentNumber;
    private String postalCode;
    private String post;
    private String county;
    private String province;
}
