package pl.holowinska.rentnowbackend.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;
import pl.holowinska.rentnowbackend.model.enums.ConvenienceType;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingConvenienceId implements Serializable {

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY, targetEntity = Booking.class)
    @JoinColumn(name = "BOOKING_ID", referencedColumnName = "BOOKING_ID")
    @LazyGroup("booking")
    @NotNull
    private Booking booking;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "CONVENIENCE_TYPE")
    @NotNull
    private ConvenienceType convenienceType;
}
