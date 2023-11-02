import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {BookingRQ} from "../../data/model/rq/BookingRQ";
import {BookingRS} from "../../data/model/rs/BookingRS";
import {BookingService} from "./BookingService";

@Injectable({
  providedIn: 'root'
})
export class BookingServiceImpl implements BookingService {

  public readonly baseUrl = '/api/reservations'

  constructor(private readonly httpClient: HttpClient) {
  }

  addBooking(data: BookingRQ): Observable<BookingRS> {
    return this.httpClient.post<BookingRS>(this.baseUrl, {
      'userUUID': data.userUUID,
      'accommodationId': data.accommodationId,
      'startDate': data.startDate,
      'endDate': data.endDate,
      'price': data.price,
      'conveniences': Object.fromEntries(data.conveniences)
    })
  }
}
