package pl.holowinska.rentnowbackend.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;
import pl.holowinska.rentnowbackend.model.enums.Status;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity(name = "BOOKING")
@NoArgsConstructor
@Getter
@Setter
public class Booking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKING_ID", unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @LazyGroup("user")
    @NotNull
    private User user;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY, targetEntity = Accommodation.class)
    @JoinColumn(name = "ACCOMMODATION_ID", referencedColumnName = "ACCOMMODATION_ID")
    @LazyGroup("accommodation")
    @NotNull
    private Accommodation accommodation;

    @Column(name = "START_DATE")
    @NotNull
    private Timestamp startDate;

    @Column(name = "END_DATE")
    @NotNull
    private Timestamp endDate;

    @Column(name = "BOOKING_DATE")
    @NotNull
    private Timestamp bookingDate;

    @Column(name = "PRICE")
    @NotNull
    private BigDecimal price;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;
}
