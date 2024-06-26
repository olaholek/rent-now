package pl.holowinska.rentnowbackend.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.holowinska.rentnowbackend.exceptions.AccommodationNotFoundException;
import pl.holowinska.rentnowbackend.exceptions.BookingConflictException;
import pl.holowinska.rentnowbackend.exceptions.PhotoDeleteException;
import pl.holowinska.rentnowbackend.model.enums.ConvenienceType;
import pl.holowinska.rentnowbackend.model.rq.AccommodationCriteriaRQ;
import pl.holowinska.rentnowbackend.model.rq.AccommodationRQ;
import pl.holowinska.rentnowbackend.model.rs.AccommodationRS;
import pl.holowinska.rentnowbackend.services.AccommodationService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.IMAGE_JPEG;

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

    @GetMapping(value = "/photos/{id}")
    public ResponseEntity<List<String>> getAccommodationImageNames(@PathVariable("id") Long accommodationId
    ) {
        try {
            return ResponseEntity.ok()
                    .body(accommodationService.getAccommodationImageNames(accommodationId));
        } catch (AccommodationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping(value = "/photos/{id}")
    public ResponseEntity<String> deletePhotoFromAccommodation(@PathVariable("id") Long accommodationId,
                                                               @RequestParam("photoName") String photoName) {
        try {
            accommodationService.deletePhotoFromAccommodation(accommodationId, photoName);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (AccommodationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (PhotoDeleteException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping(value = "/photo/{id}", produces = "image/jpeg")
    public ResponseEntity<InputStreamResource> getAccommodationMainPhoto(@PathVariable("id") Long accommodationId
    ) {
        try {
            return ResponseEntity.ok().contentType(IMAGE_JPEG)
                    .body(accommodationService.getAccommodationMainPhoto(accommodationId));
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
    public Page<AccommodationRS> getAccommodationListByUserUUID(@PathVariable UUID userUUID,
                                                                Pageable pageable) {
        return accommodationService.getAccommodationByUserUUID(userUUID, pageable);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccommodation(@PathVariable Long id) {
        try {
            accommodationService.deleteAccommodation(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (AccommodationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (BookingConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccommodationRS> updateAccommodation(@PathVariable Long id,
                                                               @RequestBody @Valid AccommodationRQ accommodationRQ) {
        try {
            return ResponseEntity.ok(accommodationService.updateAccommodation(id, accommodationRQ));
        } catch (AccommodationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/filter")
    public Page<AccommodationRS> getAccommodationListByFilter(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "street", required = false) String street,
            @RequestParam(value = "squareFootage", required = false) BigDecimal squareFootage,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(value = "maxNoOfPeople", required = false) Integer maxNoOfPeople,
            @RequestParam(value = "conveniences", required = false) List<ConvenienceType> conveniences,
            @RequestParam(value = "name", required = false) String name,
            Pageable pageable) {

        AccommodationCriteriaRQ accommodationCriteriaRQ =
                AccommodationCriteriaRQ.builder()
                        .startDate(startDate)
                        .endDate(endDate)
                        .city(city)
                        .street(street)
                        .squareFootage(squareFootage)
                        .minPrice(minPrice)
                        .maxPrice(maxPrice)
                        .conveniences(conveniences)
                        .maxNoOfPeople(maxNoOfPeople)
                        .name(name)
                        .build();

        try {
            return accommodationService.getAccommodationListByFilter(accommodationCriteriaRQ, pageable);
        } catch (IllegalArgumentException e) {
            return Page.empty();
        }
    }
}
