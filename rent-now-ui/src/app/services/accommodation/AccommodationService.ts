import {AccommodationRQ} from "../../data/model/rq/AccommodationRQ";
import {Observable} from "rxjs";
import {AccommodationRS} from "../../data/model/rs/AccommodationRS";
import {AccommodationCriteriaRQ} from "../../data/model/rq/AccommodationCriteriaRQ";
import {Page} from "../../data/model/common/Page";

export interface AccommodationService {

    createAccommodation(accommodationData: AccommodationRQ): Observable<AccommodationRS>;

    addPhotos(accommodationId: string | null | undefined, photos: Blob[]): Observable<any>;

    filterAccommodations(criteriaRQ: AccommodationCriteriaRQ, page: number, size: number, sort: string):
    Observable<Page<AccommodationRS>>;

    getAccommodation(accommodationId: number): Observable<AccommodationRS>;

    getAccommodationPhoto(accommodationId: number): Observable<Blob>;

    getAccommodationImageNames(accommodationId: number): Observable<string[]>;

    getUserAccommodations(uuid: string, page: number, size: number, sort: string): Observable<Page<AccommodationRS>>;
}
