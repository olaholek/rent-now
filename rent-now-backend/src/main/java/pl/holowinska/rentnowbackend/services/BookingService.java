package pl.holowinska.rentnowbackend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.holowinska.rentnowbackend.exceptions.AccommodationNotFoundException;
import pl.holowinska.rentnowbackend.exceptions.BookingConflictException;
import pl.holowinska.rentnowbackend.exceptions.BookingNotFoundException;
import pl.holowinska.rentnowbackend.model.rq.BookingRQ;
import pl.holowinska.rentnowbackend.model.rs.BookingRS;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    BookingRS addBooking(BookingRQ bookingRQ) throws AccommodationNotFoundException, BookingConflictException;

    void deleteBooking(Long bookingId) throws BookingNotFoundException;

    BookingRS getBooking(Long bookingId) throws BookingNotFoundException;

    Page<BookingRS> getBookingListByUser(String uuid, Pageable pageable);

    Page<BookingRS> getBookingListByAccommodation(String accommodationId, Pageable pageable);

    List<LocalDate> getBookedStartDatesByAccommodation(String accommodationId);

    List<LocalDate> getBookedEndDatesByAccommodation(String accommodationId);
}
