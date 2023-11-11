import {Component, OnInit} from '@angular/core';
import {ConvenienceType} from "../../../data/model/common/ConvenienceType";
import {KeycloakService} from "keycloak-angular";
import {ActivatedRoute, Router} from "@angular/router";
import {AccommodationServiceImpl} from "../../../services/accommodation/accommodation.service";
import {AccommodationRQ} from "../../../data/model/rq/AccommodationRQ";
import {AddressRQ} from "../../../data/model/rq/AddressRQ";
import {ConvenienceOption} from "../../../data/model/common/ConvenienceOption";
import {ToastService} from "../../../services/toast/toast.service";
import {catchError} from "rxjs";

@Component({
    selector: 'app-step1-data',
    templateUrl: './step1-data.component.html',
    styleUrls: ['./step1-data.component.scss']
})
export class Step1DataComponent implements OnInit {

    categories: ConvenienceOption[] = [];
    value: AccommodationRQ = {
        addressRQ: {} as AddressRQ,
    } as AccommodationRQ;

    constructor(
        private readonly accommodationService: AccommodationServiceImpl,
        private readonly keycloak: KeycloakService,
        private readonly route: ActivatedRoute,
        private readonly toastService: ToastService,
        private readonly router: Router
    ) {
    }

    ngOnInit(): void {
        for (const conv of Object.values(ConvenienceType)) {
            this.categories.push({
                convenience: conv,
                selected: false,
                additionalCost: 0
            } as ConvenienceOption)
        }
        this.categories = this.categories.filter((category, index) => index < 11);
        this.route.queryParamMap.subscribe(params => {
            this.keycloak.getKeycloakInstance().loadUserProfile().then(profile => {
                this.value.userUUID = profile.id!;
            })
        })
    }

    nextPage(): void {
        const selectedConveniences = this.categories.filter(category => category.selected);
        const conveniencesToSave = new Map<ConvenienceType, number>();

        for (const category of selectedConveniences) {
            conveniencesToSave.set(category.convenience, category.additionalCost);
        }

        console.log(conveniencesToSave);

        this.value.conveniences = conveniencesToSave;

        this.accommodationService.createAccommodation(this.value)
            .pipe(
                catchError((error) => {
                    this.toastService.showError('Error during accommodation creation.');
                    throw error;
                })
            )
            .subscribe((res) => {
                this.toastService.showSuccess('Accommodation created successfully.');
                this.router.navigate(['announcements/add/photos'], {queryParams: {id: res.id}});
            });
    }


    protected beutify(type: ConvenienceType): string {
        return this.capitalizeFirstLetter(type.toString().toLowerCase().replace('_', ' '));
    }

    capitalizeFirstLetter(str: string): string {
        return str.charAt(0).toUpperCase() + str.slice(1);
    }

  onSelectionChange(option: ConvenienceOption) {
    option.selected = !option.selected;
  }
}
