package pl.holowinska.rentnowbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.holowinska.rentnowbackend.model.entities.Convenience;
import pl.holowinska.rentnowbackend.model.entities.ConvenienceId;

import java.util.List;

@Repository
public interface ConvenienceRepository extends JpaRepository<Convenience, ConvenienceId> {

    @Query("select c from CONVENIENCE c where c.id.accommodation.id = :accommodationId")
    List<Convenience> getConvenienceByAccommodationId(Long accommodationId);
}
