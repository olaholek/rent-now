package pl.holowinska.rentnowbackend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import pl.holowinska.rentnowbackend.exceptions.AccommodationNotFoundException;
import pl.holowinska.rentnowbackend.model.rq.AccommodationCriteriaRQ;
import pl.holowinska.rentnowbackend.model.rq.AccommodationRQ;
import pl.holowinska.rentnowbackend.model.rs.AccommodationRS;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public interface AccommodationService {

    AccommodationRS addAccommodation(AccommodationRQ accommodationRQ);

    AccommodationRS addPhotosToAccommodation(Long accommodationId, List<MultipartFile> files) throws IOException, AccommodationNotFoundException;

    AccommodationRS getAccommodationById(Long accommodationId) throws AccommodationNotFoundException;

    Page<AccommodationRS> getAccommodationByUserUUID(UUID userUUID, Pageable pageable);

    void deleteAccommodation(Long accommodationId);

    AccommodationRS updateAccommodation(Long accommodationId, AccommodationRQ accommodationRQ) throws AccommodationNotFoundException;

    Page<AccommodationRS> getAccommodationListByFilter(AccommodationCriteriaRQ accommodationCriteriaRQ, Pageable pageable);

    List<InputStream> getAccommodationPhotos(Long accommodationId) throws AccommodationNotFoundException, FileNotFoundException;

    InputStream getAccommodationMainPhoto(Long accommodationId) throws AccommodationNotFoundException, IOException;
}
