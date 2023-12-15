package pl.holowinska.rentnowbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.holowinska.rentnowbackend.model.entities.Booking;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

    @Query("select distinct b.accommodation.id from BOOKING b where DATE(b.startDate) <= :startDate " +
            "and DATE(b.endDate) > :startDate and b.status!='CANCELED' " +
            "or DATE(b.endDate) >= :endDate and DATE(b.startDate) < :endDate or DATE(b.endDate) <= :endDate and DATE(b.startDate) >= :startDate" +
            " and b.status!='CANCELED'")
    List<Long> getBookingAccommodationIdByDates(LocalDate startDate, LocalDate endDate);

    @Modifying
    @Query("update BOOKING b set b.status='PENDING' where b.status='BOOKED' and DATE(b.startDate) = CURDATE()")
    void updateStartingReservations();

    @Modifying
    @Query("update BOOKING b set b.status='FINISHED' where b.status='PENDING' and DATE(b.endDate) < CURDATE()")
    void updateFinishedReservations();

    @Query("select case when count(*) > 0 then false else true end " +
            "from BOOKING b where (b.status = 'BOOKED' or b.status = 'PENDING') and b.accommodation.id= :accommodationId and " +
            "(DATE(b.startDate) >= :startDate and DATE(b.endDate) <= :endDate or " +
            "DATE(b.startDate) >= :startDate and DATE(b.startDate) < :endDate or " +
            "DATE(b.endDate) <= :endDate and DATE(b.endDate) > :startDate)")
    boolean isBookingAvailable(LocalDate startDate, LocalDate endDate, Long accommodationId);

    @Query("select b from BOOKING b where b.accommodation.id= :accommodationId and " +
            "(b.status = 'PENDING' or b.status='BOOKED')")
    List<Booking> getBookingsByAccommodation(Long accommodationId);
}
