package pl.holowinska.rentnowbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.holowinska.rentnowbackend.model.entities.Accommodation;

import java.util.UUID;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    @Query("select a from ACCOMMODATION a where a.user.id = :userUUID")
    Page<Accommodation> getAllByUser(UUID userUUID, Pageable pageable);
}
