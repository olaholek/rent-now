package pl.holowinska.rentnowbackend.mappers;

import pl.holowinska.rentnowbackend.model.entities.Accommodation;
import pl.holowinska.rentnowbackend.model.enums.ConvenienceType;
import pl.holowinska.rentnowbackend.model.rs.AccommodationRS;

import java.math.BigDecimal;
import java.util.HashMap;

public class AccommodationMapper {

    public static AccommodationRS mapToDto(Accommodation accommodation, HashMap<ConvenienceType, BigDecimal> conveniences) {
        if (accommodation == null) return null;
        return AccommodationRS.builder()
                .id(accommodation.getId())
                .addressRS(AddressMapper.mapToDto(accommodation.getAddress()))
                .conveniences(conveniences)
                .priceForDay(accommodation.getPriceForDay())
                .description(accommodation.getDescription())
                .squareFootage(accommodation.getSquareFootage())
                .userUUID(accommodation.getUser().getId())
                .build();
    }
}
