package pl.holowinska.rentnowbackend.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.holowinska.rentnowbackend.exceptions.AccommodationNotFoundException;
import pl.holowinska.rentnowbackend.services.FavouriteObjectServiceImpl;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/favourites")
@Slf4j
@Validated
public class FavouriteObjectController {

    private final FavouriteObjectServiceImpl favouriteObjectService;

    public FavouriteObjectController(FavouriteObjectServiceImpl favouriteObjectService) {
        this.favouriteObjectService = favouriteObjectService;
    }

    @PostMapping
    public ResponseEntity<String> addToFavourites(
            @RequestParam(value = "uuid") String uuid,
            @RequestParam(value = "accommodationId") Long accommodationId
    ) {
        try {
            favouriteObjectService.addToFavourites(uuid, accommodationId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (AccommodationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFromFavourites(
            @RequestParam(value = "uuid") String uuid,
            @RequestParam(value = "accommodationId") Long accommodationId
    ) {
        try {
            favouriteObjectService.deleteFromFavourites(uuid, accommodationId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (AccommodationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("{uuid}")
    public List<Long> getFavouritesByUser(@PathVariable String uuid) {
        try {
            return favouriteObjectService.getFavouritesByUser(uuid);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
