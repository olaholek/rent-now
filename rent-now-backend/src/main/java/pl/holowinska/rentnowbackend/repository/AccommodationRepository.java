package pl.holowinska.rentnowbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.holowinska.rentnowbackend.model.entities.Accommodation;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
}
