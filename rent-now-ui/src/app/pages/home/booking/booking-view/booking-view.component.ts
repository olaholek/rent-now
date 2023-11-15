import {Component, OnInit} from '@angular/core';
import {ConvenienceType, getConvenienceTypeText} from "../../../../data/model/common/ConvenienceType";
import {AccommodationServiceImpl} from "../../../../services/accommodation/accommodation.service";
import {BookingServiceImpl} from "../../../../services/booking/booking.service";
import {ActivatedRoute} from "@angular/router";
import {ToastService} from "../../../../services/toast/toast.service";
import {DateService} from "../../../../services/date/date.service";
import {BookingRS} from "../../../../data/model/rs/BookingRS";
import {ConvenienceOption} from "../../../../data/model/common/ConvenienceOption";
import {catchError} from "rxjs";
import {Location} from '@angular/common';

@Component({
  selector: 'app-booking-view',
  templateUrl: './booking-view.component.html',
  styleUrls: ['./booking-view.component.scss']
})
export class BookingViewComponent implements OnInit {

  photos: string[] = [];
  path !: string;
  visible: boolean = false;
  src !: string;
  numberOfDays !: number;
  bookingId !: number;

  booking !: BookingRS;
  freeConveniences: ConvenienceOption[] = [];
  paidConveniences: ConvenienceOption[] = [];

  protected readonly getConvenienceTypeText = getConvenienceTypeText;

  constructor(private readonly accommodationService: AccommodationServiceImpl,
              private readonly bookingService: BookingServiceImpl,
              private readonly route: ActivatedRoute,
              private readonly toastService: ToastService,
              private readonly dateService: DateService,
              private readonly location: Location) {
    this.route.queryParamMap
      .subscribe(params => {
        this.bookingId = Number(params.get('id'));
      })
  }

  ngOnInit(): void {
    this.getBooking();
  }

  showDialog(src: string) {
    this.visible = true;
    this.src = src;
  }

  loadImages() {
    this.accommodationService.getAccommodationImageNames(this.booking.accommodation.id)
      .subscribe(imageNames => {
          this.photos = imageNames
        }
      );
  }

  getBooking() {
    this.bookingService.getBooking(this.bookingId).subscribe(
      booking => {
        this.booking = booking;
        this.path = 'assets/photos/' + this.booking.accommodation.id + '/';

        for (const [type, cost] of Object.entries(this.booking.conveniences)) {
          if (cost == null || cost === 0) {
            this.freeConveniences.push({
              convenience: type as unknown as ConvenienceType,
              selected: false,
              additionalCost: cost
            });
          } else {
            this.paidConveniences.push({
              convenience: type as unknown as ConvenienceType,
              selected: false,
              additionalCost: cost
            });
          }
        }

        this.calculate();
        this.loadImages();
      }
    )
  }

  calculate(): void {
    this.numberOfDays = this.dateService.getNumberOfDays(this.booking.startDate, this.booking.endDate);
  }

  cancelBooking(): void {
    this.bookingService.cancelBooking(this.bookingId)
      .pipe(
        catchError((error) => {
          this.toastService.showError('Error during cancelling booking.');
          throw error;
        })
      )
      .subscribe((res) => {
        this.toastService.showSuccess('Booking cancelled successfully.');
        window.location.reload();
      });
  }

  goBack() {
    this.location.back();
  }
}
