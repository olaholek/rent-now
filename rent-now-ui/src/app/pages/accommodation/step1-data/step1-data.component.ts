import {Component, OnInit} from '@angular/core';
import {ConvenienceType} from "../../../data/model/common/ConvenienceType";
import {KeycloakService} from "keycloak-angular";
import {ActivatedRoute, Router} from "@angular/router";
import {AccommodationServiceImpl} from "../../../services/accommodation.service";
import {AccommodationRQ} from "../../../data/model/rq/AccommodationRQ";
import {AddressRQ} from "../../../data/model/rq/AddressRQ";

@Component({
    selector: 'app-step1-data',
    templateUrl: './step1-data.component.html',
    styleUrls: ['./step1-data.component.scss']
})
export class Step1DataComponent implements OnInit {

    categories: ConvenienceType[] = Object.values(ConvenienceType);
    selectedCategories: ConvenienceType[] = [];

    value: AccommodationRQ = {
        addressRQ: {} as AddressRQ,
    } as AccommodationRQ;

    constructor(
        private readonly accommodationService: AccommodationServiceImpl,
        private readonly keycloak: KeycloakService,
        private readonly route: ActivatedRoute,
        private readonly router: Router
    ) {
    }

    ngOnInit(): void {
        this.route.queryParamMap.subscribe(params => {
            this.keycloak.getKeycloakInstance().loadUserProfile().then(profile => {
                this.value.userUUID = profile.id!;
            })
        })
    }

    nextPage(): void {
        this.accommodationService.createAccommodation(this.value).subscribe({
            next: (res) => {
                this.router.navigate(['photos'], {queryParams: {id: res.id}});
            }
        });
    }
}
