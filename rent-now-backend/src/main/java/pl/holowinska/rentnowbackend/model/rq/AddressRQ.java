package pl.holowinska.rentnowbackend.model.rq;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRQ {

    @NotBlank
    private String country;
    @NotBlank
    private String city;
    private String street;
    @NotBlank
    private String houseNumber;
    private String apartmentNumber;
    @NotBlank
    private String postalCode;
    @NotBlank
    private String post;
    @NotBlank
    private String county;
    @NotBlank
    private String province;
}
