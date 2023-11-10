import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {BookingRQ} from "../../data/model/rq/BookingRQ";
import {BookingRS} from "../../data/model/rs/BookingRS";
import {BookingService} from "./BookingService";
import {DateService} from "../date/date.service";

@Injectable({
  providedIn: 'root'
})
export class BookingServiceImpl implements BookingService {

  public readonly baseUrl = '/api/reservations'

  constructor(private readonly httpClient: HttpClient,
              private readonly dateService: DateService) {
  }

  addBooking(data: BookingRQ): Observable<BookingRS> {
    return this.httpClient.post<BookingRS>(this.baseUrl, {
      'userUUID': data.userUUID,
      'accommodationId': data.accommodationId,
      'startDate': this.dateService.buildDateToSendInJSON(data.startDate),
      'endDate': this.dateService.buildDateToSendInJSON(data.endDate),
      'price': data.price,
      'conveniences': Object.fromEntries(data.conveniences)
    })
  }

  getBooking(bookingId: number): Observable<BookingRS> {
    return this.httpClient.get<BookingRS>(this.baseUrl + "/" + bookingId);
  }

  cancelBooking(bookingId: number): Observable<String> {
    return this.httpClient.delete<String>(this.baseUrl + "/" + bookingId);
  }
}
