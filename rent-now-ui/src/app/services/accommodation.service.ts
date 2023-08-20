import {Injectable} from '@angular/core';
import {AccommodationService} from "./AccommodationService";
import {AccommodationRQ} from "../data/model/rq/AccommodationRQ";
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {AccommodationRS} from "../data/model/rs/AccommodationRS";
import {AddressRQ} from "../data/model/rq/AddressRQ";

@Injectable({
    providedIn: 'root'
})
export class AccommodationServiceImpl implements AccommodationService {

    public readonly baseUrl = '/api/accommodations'

    constructor(private readonly httpClient: HttpClient) {
    }

    createAccommodation(accommodationData: AccommodationRQ): Observable<AccommodationRS> {
        return this.httpClient.post<AccommodationRS>(this.baseUrl, {
            'addressRQ': accommodationData.addressRQ as AddressRQ,
            'userUUID': accommodationData.userUUID,
            'name': accommodationData.name,
            'priceForDay': accommodationData.priceForDay,
            'squareFootage': accommodationData.squareFootage,
            'description': accommodationData.description,
            'conveniences': accommodationData.conveniences,
            'maxNoOfPeople': accommodationData.maxNoOfPeople
        })
    }
}
