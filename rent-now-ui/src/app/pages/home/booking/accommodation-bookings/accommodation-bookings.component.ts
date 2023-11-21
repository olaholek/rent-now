import {Component, OnInit} from '@angular/core';
import {Page} from "../../../../data/model/common/Page";
import {BookingRS} from "../../../../data/model/rs/BookingRS";
import {ActivatedRoute, Router} from "@angular/router";
import {BookingServiceImpl} from "../../../../services/booking/booking.service";
import {DateService} from "../../../../services/date/date.service";
import {Location} from "@angular/common";

@Component({
  selector: 'app-accommodation-bookings',
  templateUrl: './accommodation-bookings.component.html',
  styleUrls: ['./accommodation-bookings.component.scss']
})
export class AccommodationBookingsComponent implements OnInit {

  pageNo !: number;
  bookingId !: number;
  bookingList !: Page<BookingRS>;

  constructor(private readonly router: Router,
              private readonly route: ActivatedRoute,
              private readonly bookingService: BookingServiceImpl,
              private readonly dateService: DateService,
              private readonly location: Location) {
    this.route.queryParamMap
      .subscribe(params => {
        this.bookingId = Number(params.get('id'));
        this.getBookings(0);
      })
  }

  ngOnInit(): void {
  }

  private getBookings(pageNumber: number) {
    let sort = "id,desc"
    this.bookingService.getBookingsByAccommodation(this.bookingId, pageNumber, 10, sort)
      .subscribe(result => {
        this.bookingList = result;
      })
  }

  onPageChange(event: any) {
    this.pageNo = event.page;
    this.getBookings(this.pageNo);
    window.scrollTo(0, 0);
  }

  showDetails(bookingId: number) {
    this.router.navigate(['booking/view'], {
      queryParams: {
        id: bookingId,
        mode: "ownerMode"
      }
    })
  }

  getNumberOfDays(startDate: Date, endDate: Date): number {
    return this.dateService.getNumberOfDays(startDate, endDate);
  }

  goBack() {
    this.location.back();
  }
}
