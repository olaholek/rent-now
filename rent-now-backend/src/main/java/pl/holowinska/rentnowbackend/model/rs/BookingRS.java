package pl.holowinska.rentnowbackend.model.rs;

import lombok.Builder;
import lombok.Data;
import pl.holowinska.rentnowbackend.model.enums.ConvenienceType;
import pl.holowinska.rentnowbackend.model.enums.Status;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

@Data
@Builder
public class BookingRS {

    private Long id;
    private UUID userUUID;
    private AccommodationRS accommodation;
    private Timestamp startDate;
    private Timestamp endDate;
    private Timestamp bookingDate;
    private BigDecimal price;
    private Status status;
    private HashMap<ConvenienceType, BigDecimal> conveniences;
}
