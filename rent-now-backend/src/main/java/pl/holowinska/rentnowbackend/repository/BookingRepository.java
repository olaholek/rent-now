package pl.holowinska.rentnowbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.holowinska.rentnowbackend.model.entities.Booking;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select distinct b.accommodation.id from BOOKING b where b.startDate <= :startDate and b.endDate > :startDate or " +
            "b.endDate >= :endDate and b.startDate < :endDate")
    List<Long> getBookingAccommodationIdByDates(Timestamp startDate, Timestamp endDate);
}
