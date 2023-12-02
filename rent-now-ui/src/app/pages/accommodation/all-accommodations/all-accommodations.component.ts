import {Component, Input, OnInit} from '@angular/core';
import {Page} from "../../../data/model/common/Page";
import {AccommodationRS} from "../../../data/model/rs/AccommodationRS";
import {SafeUrl} from "@angular/platform-browser";
import {ActivatedRoute, Router} from "@angular/router";
import {DateService} from "../../../services/date/date.service";
import {FavouriteServiceImpl} from "../../../services/favourite/favourite.service";
import {KeycloakService} from "keycloak-angular";

@Component({
    selector: 'app-all-accommodations',
    templateUrl: './all-accommodations.component.html',
    styleUrls: ['./all-accommodations.component.scss']
})
export class AllAccommodationsComponent implements OnInit {

    @Input() accommodationList !: Page<AccommodationRS>;
    @Input() photos = new Map<number, SafeUrl>();
    @Input() numberOfDays !: number;
    @Input() startDate !: Date;
    @Input() endDate !: Date;

    showEmptyHeart: boolean = false;
    userUUID !: string;
    favourites: number[] = [];

    constructor(private readonly router: Router,
                private readonly route: ActivatedRoute,
                private readonly dateService: DateService,
                private readonly favouriteService: FavouriteServiceImpl,
                private readonly keycloak: KeycloakService) {
        this.route.queryParamMap.subscribe(params => {
            this.keycloak.getKeycloakInstance().loadUserProfile().then(profile => {
                this.userUUID = profile.id!;
                this.getFavourites();
            });
        });
    }

    getPhoto(id: number): SafeUrl {
        return <SafeUrl>this.photos.get(id);
    }

    ngOnInit(): void {
    }

    getFavourites() {
        this.favouriteService.getFavouriteListByUser(this.userUUID).subscribe(
            favouriteList => {
                this.favourites = favouriteList;
            }
        )
    }

    getCost(priceForDay: number): number {
        return Number(priceForDay) * this.numberOfDays;
    }

    showDetails(accommodationId: number) {
        this.router.navigate(['booking'], {
            queryParams: {
                id: accommodationId,
                startDate: this.dateService.buildDateToSendInJSON(this.startDate),
                endDate: this.dateService.buildDateToSendInJSON(this.endDate), mode: 'reserve'
            }
        })
    }

    isFavourite(id: number): boolean {
        return this.favourites.includes(id);
    }

    addToFavourites(id: number) {
        this.favouriteService.addToFavourites(this.userUUID, id).subscribe();
        window.location.reload();
    }

    deleteFromFavourites(id: number) {
        this.favouriteService.deleteFromFavourites(this.userUUID, id).subscribe();
        window.location.reload();
    }
}
