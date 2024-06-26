package pl.holowinska.rentnowbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.holowinska.rentnowbackend.model.entities.Convenience;
import pl.holowinska.rentnowbackend.model.entities.ConvenienceId;
import pl.holowinska.rentnowbackend.model.enums.ConvenienceType;

import java.util.List;

@Repository
public interface ConvenienceRepository extends JpaRepository<Convenience, ConvenienceId> {

    @Query("select c from CONVENIENCE c where c.id.accommodation.id = :accommodationId")
    List<Convenience> getConvenienceByAccommodationId(Long accommodationId);

    @Modifying
    @Query("delete from CONVENIENCE c where c.id.accommodation.id = :accommodationId")
    void deleteConveniencesByAccommodationId(Long accommodationId);

    @Query("select c.id.accommodation.id from CONVENIENCE c where c.id.convenienceType in :conveniences " +
            "group by c.id.accommodation having count(DISTINCT c.id.convenienceType) = :size")
    List<Long> getAccommodationByConveniencesList(List<ConvenienceType> conveniences, int size);
}
