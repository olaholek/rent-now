import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {BookingRQ} from "../../data/model/rq/BookingRQ";
import {BookingRS} from "../../data/model/rs/BookingRS";
import {BookingService} from "./BookingService";
import {DateService} from "../date/date.service";
import {Page} from "../../data/model/common/Page";

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

  getBookingsByUser(uuid: string, page: number, size: number, sort: string): Observable<Page<BookingRS>> {
    const httpParams: { [key: string]: string } = {}
    if (page != null)
      httpParams['page'] = page.toString();
    if (size != null)
      httpParams['size'] = size.toString();
    if (sort != null && sort !== '')
      httpParams['sort'] = sort;
    return this.httpClient.get<Page<BookingRS>>(this.baseUrl + '/all/' + uuid,
      {params: httpParams}
    )
  }
}
