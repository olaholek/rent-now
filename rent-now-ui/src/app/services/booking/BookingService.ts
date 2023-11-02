import {BookingRQ} from "../../data/model/rq/BookingRQ";
import {Observable} from "rxjs";
import {BookingRS} from "../../data/model/rs/BookingRS";

export interface BookingService {
  addBooking(data: BookingRQ): Observable<BookingRS>
}
