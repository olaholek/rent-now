package pl.holowinska.rentnowbackend.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity(name = "BOOKING_CONVENIENCE")
@NoArgsConstructor
@Getter
@Setter
public class BookingConvenience {

    @EmbeddedId
    private BookingConvenienceId id;

    @Column(name = "PRICE")
    private BigDecimal price;
}
