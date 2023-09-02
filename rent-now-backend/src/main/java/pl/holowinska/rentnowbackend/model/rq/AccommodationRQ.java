package pl.holowinska.rentnowbackend.model.rq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import pl.holowinska.rentnowbackend.model.enums.ConvenienceType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccommodationRQ {

    @NotNull
    private String name;
    @NotNull
    private AddressRQ addressRQ;
    @NotNull
    private UUID userUUID;
    @NotNull
    @Positive
    private BigDecimal priceForDay;
    private BigDecimal squareFootage;
    @NotBlank
    private String description;
    private HashMap<ConvenienceType, BigDecimal> conveniences;
    @NotNull
    private Integer maxNoOfPeople;
}
