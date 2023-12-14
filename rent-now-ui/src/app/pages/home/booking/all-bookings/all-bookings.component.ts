import {Component, OnInit} from '@angular/core';
import {KeycloakService} from "keycloak-angular";
import {ActivatedRoute, Router} from "@angular/router";
import {Page} from "../../../../data/model/common/Page";
import {BookingRS} from "../../../../data/model/rs/BookingRS";
import {AccommodationServiceImpl} from "../../../../services/accommodation/accommodation.service";
import {SafeUrl} from "@angular/platform-browser";
import {BookingServiceImpl} from "../../../../services/booking/booking.service";
import {DateService} from "../../../../services/date/date.service";
import {FavouriteServiceImpl} from "../../../../services/favourite/favourite.service";

@Component({
  selector: 'app-all-bookings',
  templateUrl: './all-bookings.component.html',
  styleUrls: ['./all-bookings.component.scss']
})
export class AllBookingsComponent implements OnInit {

  pageNo !: number;
  uuid !: string;
  photos = new Map<number, SafeUrl>();
  bookingList !: Page<BookingRS>;
  favourites: number[] = [];

  constructor(private readonly router: Router,
              private readonly keycloak: KeycloakService,
              private readonly route: ActivatedRoute,
              private readonly accommodationService: AccommodationServiceImpl,
              private readonly favouriteService: FavouriteServiceImpl,
              private readonly bookingService: BookingServiceImpl,
              private readonly dateService: DateService) {
    this.route.queryParamMap.subscribe(params => {
      this.keycloak.getKeycloakInstance().loadUserProfile().then(profile => {
        this.uuid = profile.id!;
        this.getBookings(0);
        this.getFavourites();
      })
    })
  }

  ngOnInit(): void {
  }

  private getBookings(pageNumber: number) {
    let sort = "startDate,desc"
    this.bookingService.getBookingsByUser(this.uuid, pageNumber, 10, sort)
      .subscribe(result => {
        this.bookingList = result;
        for (let element of this.bookingList.content) {
          this.accommodationService.getAccommodationPhoto(element.accommodation.id).subscribe(
            photo => {
              const blobUrl = URL.createObjectURL(photo);
              this.photos.set(element.id, blobUrl);
            }
          )
        }
      })
  }

  onPageChange(event: any) {
    this.pageNo = event.page;
    this.getBookings(this.pageNo);
    window.scrollTo(0, 0);
  }

  getPhoto(id: number): SafeUrl {
    return <SafeUrl>this.photos.get(id);
  }

  showDetails(bookingId: number) {
    this.router.navigate(['booking/view'], {
      queryParams: {
        id: bookingId
      }
    })
  }

  getNumberOfDays(startDate: Date, endDate: Date): number {
    return this.dateService.getNumberOfDays(startDate, endDate);
  }

  isFavourite(id: number): boolean {
    return this.favourites.includes(id);
  }

  getFavourites() {
    this.favouriteService.getFavouriteListByUser(this.uuid).subscribe(
      favouriteList => {
        this.favourites = favouriteList;
      }
    )
  }

  addToFavourites(id: number) {
    this.favouriteService.addToFavourites(this.uuid, id).subscribe();
    window.location.reload();
  }

  deleteFromFavourites(id: number) {
    this.favouriteService.deleteFromFavourites(this.uuid, id).subscribe();
    window.location.reload();
  }
}
