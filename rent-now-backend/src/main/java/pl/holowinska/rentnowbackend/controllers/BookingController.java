package pl.holowinska.rentnowbackend.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.holowinska.rentnowbackend.exceptions.AccommodationNotFoundException;
import pl.holowinska.rentnowbackend.exceptions.BookingConflictException;
import pl.holowinska.rentnowbackend.exceptions.BookingNotFoundException;
import pl.holowinska.rentnowbackend.model.rq.BookingRQ;
import pl.holowinska.rentnowbackend.model.rs.BookingRS;
import pl.holowinska.rentnowbackend.services.BookingService;

@RestController
@RequestMapping("/reservations")
@Slf4j
@Validated
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingRS> addBooking(@RequestBody @Valid BookingRQ bookingRQ) {
        try {
            return ResponseEntity.ok(bookingService.addBooking(bookingRQ));
        } catch (AccommodationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException | BookingConflictException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        try {
            bookingService.deleteBooking(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (BookingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
