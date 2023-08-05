package pl.holowinska.rentnowbackend.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity(name = "ACCOMMODATION")
@NoArgsConstructor
@Getter
@Setter
public class Accommodation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOMMODATION_ID", unique = true)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ADDRESS_ID")
    @LazyGroup("address")
    @NotNull
    private Address address;

    @Column(name = "PRICE_FOR_DAY")
    @NotNull
    private BigDecimal priceForDay;

    @Column(name = "SQUARE_FOOTAGE")
    @NotNull
    private double squareFootage;

    @Column(name = "DESCRIPTION")
    @NotEmpty
    private String description;
}
