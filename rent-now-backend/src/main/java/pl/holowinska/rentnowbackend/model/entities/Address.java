package pl.holowinska.rentnowbackend.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ADDRESS")
@NoArgsConstructor
@Getter
@Setter
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADDRESS_ID", unique = true)
    private Long id;

    @Column(name = "CITY", nullable = false)
    @NotEmpty
    private String city;

    @Column(name = "STREET")
    private String street;

    @Column(name = "HOUSE_NUMBER", nullable = false)
    @NotEmpty
    private String houseNumber;

    @Column(name = "APARTMENT_NUMBER")
    private String apartmentNumber;

    @Column(name = "POSTAL_CODE", nullable = false)
    @NotEmpty
    private String postalCode;

    @Column(name = "POST", nullable = false)
    @NotEmpty
    private String post;

    @Column(name = "COUNTY", nullable = false)
    @NotEmpty
    private String county;

    @Column(name = "PROVINCE", nullable = false)
    @NotEmpty
    private String province;

    @Column(name = "COUNTRY", nullable = false)
    @NotEmpty
    private String country;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return id.equals(address.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
