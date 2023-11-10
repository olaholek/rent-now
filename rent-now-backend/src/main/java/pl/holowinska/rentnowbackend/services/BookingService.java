package pl.holowinska.rentnowbackend.services;

import pl.holowinska.rentnowbackend.exceptions.AccommodationNotFoundException;
import pl.holowinska.rentnowbackend.exceptions.BookingConflictException;
import pl.holowinska.rentnowbackend.exceptions.BookingNotFoundException;
import pl.holowinska.rentnowbackend.model.rq.BookingRQ;
import pl.holowinska.rentnowbackend.model.rs.BookingRS;

public interface BookingService {

    BookingRS addBooking(BookingRQ bookingRQ) throws AccommodationNotFoundException, BookingConflictException;

    void deleteBooking(Long bookingId) throws BookingNotFoundException;

    BookingRS getBooking(Long bookingId) throws BookingNotFoundException;
}
