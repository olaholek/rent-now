import {Component, OnInit} from '@angular/core';
import {AccommodationServiceImpl} from "../../../services/accommodation/accommodation.service";
import {ToastService} from "../../../services/toast/toast.service";
import {ActivatedRoute, Router} from "@angular/router";
import {catchError} from "rxjs";
import {AccommodationRS} from "../../../data/model/rs/AccommodationRS";
import {ConvenienceType, getConvenienceTypeText} from "../../../data/model/common/ConvenienceType";
import {ConvenienceOption} from "../../../data/model/common/ConvenienceOption";
import {BookingRQ} from "../../../data/model/rq/BookingRQ";
import {KeycloakService} from "keycloak-angular";
import {BookingServiceImpl} from "../../../services/booking/booking.service";
import {DateService} from "../../../services/date/date.service";
import {Location} from "@angular/common";

@Component({
  selector: 'app-booking',
  templateUrl: './booking.component.html',
  styleUrls: ['./booking.component.scss']
})
export class BookingComponent implements OnInit {

  path !: string;
  photos: string[] = [];
  accommodationId !: number;
  visible: boolean = false;
  src !: string;
  numberOfDays !: number;

  startDate: Date | undefined;
  endDate: Date | undefined;
  minStartDate = new Date();
  minEndDate = new Date();

  accommodation: AccommodationRS = {
    id: this.accommodationId,
    addressRS: {
      city: '',
      street: null,
      houseNumber: '',
      apartmentNumber: null,
      postalCode: '',
      post: '',
      country: '',
      county: '',
      province: ''
    },
    userUUID: '',
    name: '',
    priceForDay: 0,
    squareFootage: 0,
    description: '',
    conveniences: new Map<string, number>,
    maxNoOfPeople: 0
  };

  freeConveniences: ConvenienceOption[] = [];
  paidConveniences: ConvenienceOption[] = [];

  booking: BookingRQ = {} as BookingRQ;

  constructor(private readonly accommodationService: AccommodationServiceImpl,
              private readonly bookingService: BookingServiceImpl,
              private readonly route: ActivatedRoute,
              private readonly keycloak: KeycloakService,
              private readonly toastService: ToastService,
              private readonly router: Router,
              private readonly dateService: DateService,
              private readonly location: Location) {
    this.route.queryParamMap
      .subscribe(params => {
        this.accommodationId = Number(params.get('id'));
        this.path = 'assets/photos/' + this.accommodationId + '/';
        if (params.get('startDate') !== null && params.get('startDate') !== '') {
          this.startDate = new Date(params.get('startDate') as string);
        }
        if (params.get('endDate') !== null && params.get('endDate') !== '') {
          this.endDate = new Date(params.get('endDate') as string);
        }
        this.keycloak.getKeycloakInstance().loadUserProfile().then(profile => {
          this.booking.userUUID = profile.id as string;
        })
      })

    this.booking.conveniences = new Map<ConvenienceType, number>();
  }

  showDialog(src: string) {
    this.visible = true;
    this.src = src;
  }

  ngOnInit() {
    this.loadImages();
    this.getAccommodation();
    this.minEndDate.setDate(this.minStartDate.getDate() + 1);
  }

  calculate(): void {
    this.booking.conveniences.clear();
    this.booking.accommodationId = this.accommodationId;
    if (this.startDate) {
      this.booking.startDate = this.startDate;
      if (this.endDate && this.startDate < this.endDate) {
        this.booking.endDate = this.endDate;
        this.numberOfDays = this.dateService.getNumberOfDays(this.startDate, this.endDate);
        for (let freeConvenience of this.freeConveniences) {
          this.booking.conveniences.set(freeConvenience.convenience, freeConvenience.additionalCost);
        }
        this.paidConveniences.forEach(option => option.selected ?
          this.booking.conveniences.set(option.convenience, option.additionalCost) : '');
        this.booking.price = (this.accommodation.priceForDay * this.numberOfDays) + this.sumConveniences();
      }
    }
  }

  loadImages() {
    this.accommodationService.getAccommodationImageNames(this.accommodationId)
      .subscribe(imageNames => {
          this.photos = imageNames
        }
      );
  }

  onSelectionChange(option: ConvenienceOption) {
    option.selected = !option.selected;
    this.calculate();
  }

  getAccommodation() {
    this.accommodationService.getAccommodation(this.accommodationId).subscribe(
      accommodation => {
        this.accommodation = accommodation;
        for (const [type, cost] of Object.entries(this.accommodation.conveniences)) {
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
      }
    )
  }

  sumConveniences(): number {
    let total = 0;
    for (const value of this.booking.conveniences.values()) {
      total += value;
    }
    return total;
  }

  bookAccommodation(): void {
    this.bookingService.addBooking(this.booking)
      .pipe(
        catchError((error) => {
          this.toastService.showError('Error during booking accommodation.');
          throw error;
        })
      )
      .subscribe((res) => {
        this.toastService.showSuccess('Booking completed successfully.');
        this.router.navigate(['booking/view'], {queryParams: {id: res.id}});
      });
  }

  goBack() {
    this.location.back();
  }

  protected readonly getConvenienceTypeText = getConvenienceTypeText;
}

