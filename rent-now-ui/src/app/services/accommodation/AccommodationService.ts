import {AccommodationRQ} from "../../data/model/rq/AccommodationRQ";
import {Observable} from "rxjs";
import {AccommodationRS} from "../../data/model/rs/AccommodationRS";

export interface AccommodationService {

    createAccommodation(accommodationData: AccommodationRQ): Observable<AccommodationRS>;
}
