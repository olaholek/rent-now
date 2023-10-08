import {Component, OnInit} from '@angular/core';
import {AccommodationCriteriaRQ} from "../../../data/model/rq/AccommodationCriteriaRQ";
import {ConvenienceType} from "../../../data/model/common/ConvenienceType";
import {AccommodationServiceImpl} from "../../../services/accommodation/accommodation.service";
import {KeycloakService} from "keycloak-angular";
import {ActivatedRoute, Router} from "@angular/router";
import {ToastService} from "../../../services/toast/toast.service";


@Component({
  selector: 'app-filter',
  templateUrl: './filter.component.html',
  styleUrls: ['./filter.component.scss']
})
export class FilterComponent implements OnInit {
  showFilters = false;
  conveniencesList: ConvenienceType[] = [];

  value: AccommodationCriteriaRQ = {
    startDate: new Date(),
    endDate: new Date(),
    city: '',
    street: '',
    squareFootage: undefined,
    minPrice: undefined,
    maxPrice: undefined,
    conveniences: [],
    maxNoOfPeople: NaN,
    name: ''
  };

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
      this.conveniencesList.push(conv as ConvenienceType);
    }
    this.conveniencesList = this.conveniencesList.filter((category, index) => index < 11);

    this.value.startDate = new Date();
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    this.value.endDate = tomorrow;
  }

  toggleFilters() {
    this.showFilters = !this.showFilters;
  }

  search(): void {
    console.log("pppp")
    // this.accommodationService.(this.value)
    //   .pipe(
    //     catchError((error) => {
    //       this.toastService.showError('Error during accommodation creation.');
    //       throw error;
    //     })
    //   )
    //   .subscribe((res) => {
    //     this.toastService.showSuccess('Accommodation created successfully.');
    //     this.router.navigate(['announcements/add/photos'], {queryParams: {id: res.id}});
    //   });
  }

  protected beutify(type: ConvenienceType): string {
    return this.capitalizeFirstLetter(type.toString().toLowerCase().replace('_', ' '));
  }

  capitalizeFirstLetter(str: string): string {
    return str.charAt(0).toUpperCase() + str.slice(1);
  }
}
