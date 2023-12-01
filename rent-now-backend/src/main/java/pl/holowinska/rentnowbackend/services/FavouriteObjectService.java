package pl.holowinska.rentnowbackend.services;

import pl.holowinska.rentnowbackend.exceptions.AccommodationNotFoundException;

import java.util.List;

public interface FavouriteObjectService {

    void addToFavourites(String uuid, Long accommodationId) throws AccommodationNotFoundException;

    void deleteFromFavourites(String uuid, Long accommodationId) throws AccommodationNotFoundException;

    List<Long> getFavouritesByUser(String uuid);
}
