import {Injectable} from '@angular/core';
import {AccommodationService} from "./AccommodationService";
import {AccommodationRQ} from "../../data/model/rq/AccommodationRQ";
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AccommodationRS} from "../../data/model/rs/AccommodationRS";
import {AddressRQ} from "../../data/model/rq/AddressRQ";
import {AccommodationCriteriaRQ} from "../../data/model/rq/AccommodationCriteriaRQ";
import {Page} from "../../data/model/common/Page";
import {ConvenienceType} from "../../data/model/common/ConvenienceType";
import {DateService} from "../date/date.service";

@Injectable({
  providedIn: 'root'
})
export class AccommodationServiceImpl implements AccommodationService {

  public readonly baseUrl = '/api/accommodations'

  constructor(private readonly httpClient: HttpClient,
              private readonly dateService: DateService) {
  }

  createAccommodation(accommodationData: AccommodationRQ): Observable<AccommodationRS> {
    return this.httpClient.post<AccommodationRS>(this.baseUrl, {
      'addressRQ': accommodationData.addressRQ as AddressRQ,
      'userUUID': accommodationData.userUUID,
      'name': accommodationData.name,
      'priceForDay': accommodationData.priceForDay,
      'squareFootage': accommodationData.squareFootage,
      'description': accommodationData.description,
      'conveniences': Object.fromEntries(accommodationData.conveniences),
      'maxNoOfPeople': accommodationData.maxNoOfPeople
    })
  }

  addPhotos(accommodationId: string | null | undefined, photos: Blob[]): Observable<any> {
    let headers = new Headers();
    headers.append('Content-Type', 'application/pdf');
    const formData = new FormData();
    photos.forEach(photo => {
        formData.append('files', photo);
      }
    )
    return this.httpClient.post(this.baseUrl + '/photos/' + accommodationId, formData);
  }

  filterAccommodations(criteriaRQ: AccommodationCriteriaRQ, page: number, size: number, sort: string):
    Observable<Page<AccommodationRS>> {
    const httpParams: { [key: string]: string } = {}

    if (criteriaRQ.startDate != null)
      httpParams['startDate'] = this.dateService.buildDateToSendInJSON(criteriaRQ.startDate);
    if (criteriaRQ.endDate != null)
      httpParams['endDate'] = this.dateService.buildDateToSendInJSON(criteriaRQ.endDate);
    if (criteriaRQ.city != null && criteriaRQ.city !== '')
      httpParams['city'] = criteriaRQ.city;
    if (criteriaRQ.street != null && criteriaRQ.street !== '')
      httpParams['street'] = criteriaRQ.street;
    if (criteriaRQ.squareFootage != null)
      httpParams['squareFootage'] = criteriaRQ.squareFootage.toString();
    if (criteriaRQ.minPrice != null)
      httpParams['minPrice'] = criteriaRQ.minPrice.toString();
    if (criteriaRQ.maxPrice != null)
      httpParams['maxPrice'] = criteriaRQ.maxPrice.toString();
    if (criteriaRQ.conveniences != null && this.getConveniences(criteriaRQ.conveniences) !== '')
      httpParams['conveniences'] = this.getConveniences(criteriaRQ.conveniences);
    if (criteriaRQ.maxNoOfPeople != null && !isNaN(criteriaRQ.maxNoOfPeople))
      httpParams['maxNoOfPeople'] = criteriaRQ.maxNoOfPeople.toString();
    if (criteriaRQ.name != null && criteriaRQ.name !== '')
      httpParams['name'] = criteriaRQ.name;
    if (page != null)
      httpParams['page'] = page.toString();
    if (size != null)
      httpParams['size'] = size.toString();
    if (sort != null && sort !== '')
      httpParams['sort'] = sort;

    return this.httpClient.get<Page<AccommodationRS>>(this.baseUrl + '/filter',
      {params: httpParams}
    )
  }

  getAccommodation(accommodationId: number): Observable<AccommodationRS> {
    return this.httpClient.get<AccommodationRS>(this.baseUrl + "/" + accommodationId);
  }

  getAccommodationPhoto(accommodationId: number): Observable<Blob> {
    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'image/jpeg');
    return this.httpClient.get(this.baseUrl + "/photo/" + accommodationId, {headers: headers, responseType: 'blob'});
  }

  getAccommodationImageNames(accommodationId: number): Observable<string[]> {
    return this.httpClient.get<string[]>(this.baseUrl + '/photos/' + accommodationId);
  }

  getConveniences(conveniences: ConvenienceType[]): string {
    let convenienceList = ''
    for (let convenience of conveniences) {
      if (convenienceList === '') {
        convenienceList = convenience.toString()
      } else {
        convenienceList = convenienceList + ', ' + convenience.toString();
      }
    }
    return convenienceList;
  }
}
