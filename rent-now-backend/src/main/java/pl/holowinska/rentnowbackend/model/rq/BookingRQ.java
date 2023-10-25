package pl.holowinska.rentnowbackend.model.rq;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.holowinska.rentnowbackend.model.enums.ConvenienceType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingRQ {

    @NotNull
    private UUID userUUID;
    @NotNull
    private Long accommodationId;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    private BigDecimal price;
    private HashMap<ConvenienceType, BigDecimal> conveniences;
}
