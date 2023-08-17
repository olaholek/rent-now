package pl.holowinska.rentnowbackend.model.rq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.holowinska.rentnowbackend.model.enums.ConvenienceType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationRQ {

    @NotNull
    private AddressRQ addressRQ;
    @NotNull
    private UUID userUUID;
    @NotNull
    @Positive
    private BigDecimal priceForDay;
    private Double squareFootage;
    @NotBlank
    private String description;
    private HashMap<ConvenienceType, BigDecimal> conveniences;
}
