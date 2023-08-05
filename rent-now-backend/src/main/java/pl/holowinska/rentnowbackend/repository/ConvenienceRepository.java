package pl.holowinska.rentnowbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.holowinska.rentnowbackend.model.entities.Convenience;
import pl.holowinska.rentnowbackend.model.entities.ConvenienceId;

@Repository
public interface ConvenienceRepository extends JpaRepository<Convenience, ConvenienceId> {
}
