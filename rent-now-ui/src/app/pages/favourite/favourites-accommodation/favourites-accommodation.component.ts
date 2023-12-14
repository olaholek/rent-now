import {Component, OnInit} from '@angular/core';
import {SafeUrl} from "@angular/platform-browser";
import {Page} from "../../../data/model/common/Page";
import {AccommodationRS} from "../../../data/model/rs/AccommodationRS";
import {ActivatedRoute, Router} from "@angular/router";
import {AccommodationServiceImpl} from "../../../services/accommodation/accommodation.service";
import {KeycloakService} from "keycloak-angular";
import {FavouriteServiceImpl} from "../../../services/favourite/favourite.service";

@Component({
  selector: 'app-favourites-accommodation',
  templateUrl: './favourites-accommodation.component.html',
  styleUrls: ['./favourites-accommodation.component.scss']
})
export class FavouritesAccommodationComponent implements OnInit{

  pageNo !: number;
  uuid !: string;
  photos = new Map<number, SafeUrl>();
  favourites !: Page<AccommodationRS>;
  favouriteList: number[] = [];

  constructor(private readonly router: Router,
              private readonly favouriteService: FavouriteServiceImpl,
              private readonly accommodationService: AccommodationServiceImpl,
              private readonly route: ActivatedRoute,
              private readonly keycloak: KeycloakService) {
    this.route.queryParamMap.subscribe(params => {
      this.keycloak.getKeycloakInstance().loadUserProfile().then(profile => {
        this.uuid = profile.id!;
        this.getFavourites(0);
        this.getFavouriteList();
      })
    })
  }

  ngOnInit(): void {
  }

  private getFavourites(pageNumber: number) {
    this.favouriteService.getFavouritesByUser(this.uuid, pageNumber, 10)
      .subscribe(result => {
        this.favourites = result;
        for (let element of this.favourites.content) {
          this.accommodationService.getAccommodationPhoto(element.id).subscribe(
            photo => {
              const blobUrl = URL.createObjectURL(photo);
              this.photos.set(element.id, blobUrl);
            }
          )
        }
      })
  }

  getPhoto(id: number): SafeUrl {
    return <SafeUrl>this.photos.get(id);
  }

  onPageChange(event: any) {
    this.pageNo = event.page;
    this.getFavourites(this.pageNo);
    window.scrollTo(0, 0);
  }

  showDetails(accommodationId: number) {
    this.router.navigate(['accommodations/view'], {
      queryParams: {
        id: accommodationId
      }
    })
  }

  isFavourite(id: number): boolean {
    return this.favouriteList.includes(id);
  }

  addToFavourites(id: number) {
    this.favouriteService.addToFavourites(this.uuid, id).subscribe();
    window.location.reload();
  }

  deleteFromFavourites(id: number) {
    this.favouriteService.deleteFromFavourites(this.uuid, id).subscribe();
    window.location.reload();
  }

  getFavouriteList() {
    this.favouriteService.getFavouriteListByUser(this.uuid).subscribe(
      favouriteList => {
        this.favouriteList = favouriteList;
      }
    )
  }
}
