package pl.holowinska.rentnowbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.holowinska.rentnowbackend.model.entities.BookingConvenience;
import pl.holowinska.rentnowbackend.model.entities.BookingConvenienceId;

import java.util.List;

@Repository
public interface BookingConvenienceRepository extends JpaRepository<BookingConvenience, BookingConvenienceId> {

    @Query("select b from BOOKING_CONVENIENCE b where b.id.booking.id = :bookingId")
    List<BookingConvenience> getConvenienceByBookingId(Long bookingId);
}
