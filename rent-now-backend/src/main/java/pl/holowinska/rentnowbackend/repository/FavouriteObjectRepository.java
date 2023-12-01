package pl.holowinska.rentnowbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.holowinska.rentnowbackend.model.entities.FavouriteObject;
import pl.holowinska.rentnowbackend.model.entities.FavouriteObjectId;

import java.util.List;
import java.util.UUID;

@Repository
public interface FavouriteObjectRepository extends JpaRepository<FavouriteObject, FavouriteObjectId> {

    @Query("select f.id.accommodation.id from FAVOURITE_OBJECT f where f.id.user.id = :userUUID")
    List<Long> getFavouritesByUser(UUID userUUID);
}
