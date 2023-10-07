package pl.holowinska.rentnowbackend.model.rq;

import lombok.Builder;
import lombok.Data;
import pl.holowinska.rentnowbackend.model.enums.ConvenienceType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class AccommodationCriteriaRQ {

    private LocalDate startDate;
    private LocalDate endDate;
    private String city;
    private String street;
    private BigDecimal squareFootage;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<ConvenienceType> conveniences;
}