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
import java.util.HashSet;
import java.util.Set;

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

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @LazyGroup("user")
    @NotNull
    private User user;

    @Column(name = "PRICE_FOR_DAY")
    @NotNull
    private BigDecimal priceForDay;

    @Column(name = "SQUARE_FOOTAGE")
    private BigDecimal squareFootage;

    @Column(name = "DESCRIPTION")
    @NotEmpty
    private String description;

    @Column(name = "NAME")
    @NotEmpty
    private String name;

    @Column(name = "MAX_NO_OF_PEOPLE")
    @NotNull
    private Integer maxNoOfPeople;

    @Column(name = "STATUS")
    private Long status;

    @OneToMany(mappedBy = "id.accommodation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Convenience> conveniences = new HashSet<>();
}
