package pl.holowinska.rentnowbackend.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.holowinska.rentnowbackend.model.rq.AccommodationRQ;
import pl.holowinska.rentnowbackend.model.rs.AccommodationRS;
import pl.holowinska.rentnowbackend.services.AccommodationService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/accommodations")
@Slf4j
@Validated
public class AccommodationController {

    private final AccommodationService accommodationService;

    public AccommodationController(AccommodationService accommodationService) {
        this.accommodationService = accommodationService;
    }

    @PostMapping
    public ResponseEntity<AccommodationRS> addAccommodation(@RequestBody @Valid AccommodationRQ accommodationRQ) {
        try {
            return ResponseEntity.ok(accommodationService.addAccommodation(accommodationRQ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/photos/{id}")
    public ResponseEntity<AccommodationRS> addPhotosToAccommodation(
            @PathVariable("id") Long accommodationId,
            @RequestParam("files") List<MultipartFile> files
    ) {
        try {
            return ResponseEntity.ok(accommodationService.addPhotosToAccommodation(accommodationId, files));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
