import {BookingRQ} from "../../data/model/rq/BookingRQ";
import {Observable} from "rxjs";
import {BookingRS} from "../../data/model/rs/BookingRS";
import {Page} from "../../data/model/common/Page";

export interface BookingService {

  addBooking(data: BookingRQ): Observable<BookingRS>;

  getBooking(bookingId: number): Observable<BookingRS>;

  cancelBooking(bookingId: number): Observable<String>;

  getBookingsByUser(uuid: string, page: number, size: number, sort: string): Observable<Page<BookingRS>>;
}
