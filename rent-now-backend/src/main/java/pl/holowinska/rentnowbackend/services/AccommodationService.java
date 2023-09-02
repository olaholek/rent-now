package pl.holowinska.rentnowbackend.services;

import org.springframework.web.multipart.MultipartFile;
import pl.holowinska.rentnowbackend.model.rq.AccommodationRQ;
import pl.holowinska.rentnowbackend.model.rs.AccommodationRS;

import java.io.IOException;
import java.util.List;

public interface AccommodationService {

    AccommodationRS addAccommodation(AccommodationRQ accommodationRQ);

    AccommodationRS addPhotosToAccommodation(Long accommodationId, List<MultipartFile> files) throws IOException;
}
