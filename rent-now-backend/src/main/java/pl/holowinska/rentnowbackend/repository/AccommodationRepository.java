package pl.holowinska.rentnowbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.holowinska.rentnowbackend.model.entities.Accommodation;

import java.util.UUID;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long>, JpaSpecificationExecutor<Accommodation> {

    @Query("select a from ACCOMMODATION a where a.user.id = :userUUID and a.status IS NULL")
    Page<Accommodation> getAllByUser(UUID userUUID, Pageable pageable);
}
