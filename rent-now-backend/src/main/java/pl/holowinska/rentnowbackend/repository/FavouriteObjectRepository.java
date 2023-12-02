package pl.holowinska.rentnowbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.holowinska.rentnowbackend.model.entities.Accommodation;
import pl.holowinska.rentnowbackend.model.entities.FavouriteObject;
import pl.holowinska.rentnowbackend.model.entities.FavouriteObjectId;

import java.util.List;
import java.util.UUID;

@Repository
public interface FavouriteObjectRepository extends JpaRepository<FavouriteObject, FavouriteObjectId> {

    @Query("select f.id.accommodation.id from FAVOURITE_OBJECT f where f.id.user.id = :userUUID")
    List<Long> getFavouritesByUser(UUID userUUID);

    @Query("select a from ACCOMMODATION a, FAVOURITE_OBJECT f where f.id.user.id = :userUUID and f.id.user.id = a.user.id")
    Page<Accommodation> getAllByUser(UUID userUUID, Pageable pageable);
}
