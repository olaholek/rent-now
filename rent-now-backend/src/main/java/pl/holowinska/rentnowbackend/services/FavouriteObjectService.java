package pl.holowinska.rentnowbackend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.holowinska.rentnowbackend.exceptions.AccommodationNotFoundException;
import pl.holowinska.rentnowbackend.model.rs.AccommodationRS;

import java.util.List;

public interface FavouriteObjectService {

    void addToFavourites(String uuid, Long accommodationId) throws AccommodationNotFoundException;

    void deleteFromFavourites(String uuid, Long accommodationId) throws AccommodationNotFoundException;

    List<Long> getFavouriteListByUser(String uuid);

    Page<AccommodationRS> getFavouritesByUser(String uuid, Pageable pageable);
}
