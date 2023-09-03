package pl.holowinska.rentnowbackend.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.holowinska.rentnowbackend.exceptions.AccommodationNotFoundException;
import pl.holowinska.rentnowbackend.model.rq.AccommodationRQ;
import pl.holowinska.rentnowbackend.model.rs.AccommodationRS;
import pl.holowinska.rentnowbackend.services.AccommodationService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
        } catch (AccommodationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccommodationRS> getAccommodationById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(accommodationService.getAccommodationById(id));
        } catch (AccommodationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/user/{userUUID}")
    public Page<AccommodationRS> getAccommodationByUserUUID(@PathVariable UUID userUUID,
                                                            Pageable pageable) {
        return accommodationService.getAccommodationByUserUUID(userUUID, pageable);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccommodation(@PathVariable Long id) {
        try {
            accommodationService.deleteAccommodation(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccommodationRS> updateAccommodation(@PathVariable Long id, @RequestBody @Valid AccommodationRQ accommodationRQ) {
        try {
            return ResponseEntity.ok(accommodationService.updateAccommodation(id, accommodationRQ));
        } catch (AccommodationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //todo 2 endpoints: pobieranie wszystkich noclegów na stronę głowną (sort id desc) i filtrowanie noclegów
}
