package pl.holowinska.rentnowbackend.model.rs;

import lombok.Builder;
import lombok.Data;
import pl.holowinska.rentnowbackend.model.enums.ConvenienceType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

@Data
@Builder
public class AccommodationRS {

    private Long id;
    private AddressRS addressRS;
    private UUID userUUID;
    private BigDecimal priceForDay;
    private Double squareFootage;
    private String description;
    private HashMap<ConvenienceType, BigDecimal> conveniences;
}
