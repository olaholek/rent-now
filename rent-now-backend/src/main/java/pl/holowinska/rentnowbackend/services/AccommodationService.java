package pl.holowinska.rentnowbackend.services;

import pl.holowinska.rentnowbackend.model.rq.AccommodationRQ;
import pl.holowinska.rentnowbackend.model.rs.AccommodationRS;

public interface AccommodationService {

    AccommodationRS addAccommodation(AccommodationRQ accommodationRQ);
}
