package pl.holowinska.rentnowbackend.mappers;

import pl.holowinska.rentnowbackend.model.entities.Booking;
import pl.holowinska.rentnowbackend.model.enums.ConvenienceType;
import pl.holowinska.rentnowbackend.model.rs.BookingRS;

import java.math.BigDecimal;
import java.util.HashMap;

public class BookingMapper {

    public static BookingRS mapToDto(Booking booking, HashMap<ConvenienceType, BigDecimal> conveniences) {
        if (booking == null) return null;
        return BookingRS.builder()
                .id(booking.getId())
                .userUUID(booking.getUser().getId())
                .accommodationId(booking.getAccommodation().getId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .bookingDate(booking.getBookingDate())
                .status(booking.getStatus())
                .price(booking.getPrice())
                .conveniences(conveniences)
                .build();
    }
}
