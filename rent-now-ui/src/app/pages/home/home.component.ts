import {Component, OnInit} from '@angular/core';
import {AccommodationCriteriaRQ} from "../../data/model/rq/AccommodationCriteriaRQ";
import {Page} from "../../data/model/common/Page";
import {AccommodationRS} from "../../data/model/rs/AccommodationRS";
import {catchError} from "rxjs";
import {AccommodationServiceImpl} from "../../services/accommodation/accommodation.service";
import {ToastService} from "../../services/toast/toast.service";
import {SafeUrl} from "@angular/platform-browser";
import {DateService} from "../../services/date/date.service";
import {TranslocoService} from "@ngneat/transloco";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  accommodationList !: Page<AccommodationRS>;
  pageNo !: number;
  photos = new Map<number, SafeUrl>();
  numberOfDays = 1;
  sort !: string;

  sortOptions = ['From the newest', 'Price high to low', 'Price low to high'];
  options = new Map([
    ['From the newest', 'id,desc'], ['Price high to low', 'priceForDay,desc'], ['Price low to high', 'priceForDay,asc']
  ]);

  sortOptionsPL = ['Od najnowszych', 'Od najdroższych', 'Od najtańszych'];
  optionsPL = new Map([
    ['Od najnowszych', 'id,desc'], ['Od najdroższych', 'priceForDay,desc'], ['Od najtańszych', 'priceForDay,asc']
  ]);
  selectedSort !: string;

  criteria: AccommodationCriteriaRQ = {
    startDate: new Date(),
    endDate: new Date(),
    city: '',
    street: '',
    squareFootage: undefined,
    minPrice: undefined,
    maxPrice: undefined,
    conveniences: [],
    maxNoOfPeople: undefined,
    name: ''
  };

  constructor(
    private readonly accommodationService: AccommodationServiceImpl,
    private readonly toastService: ToastService,
    private readonly dateService: DateService,
    public translocoService: TranslocoService
  ) {
    this.criteria.startDate = new Date();
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    this.criteria.endDate = tomorrow;
    this.setDefault();
  }

  ngOnInit(): void {
    this.pageNo = 0;
    this.getAccommodations(this.criteria, this.pageNo, this.sort);
    this.setDefault();
  }

  onChangeSort() {
    this.pageNo = 0;
    this.sort = <string>this.options.get(this.selectedSort);
    this.getAccommodations(this.criteria, this.pageNo, this.sort);
  }

  onChangeSortPL() {
    this.pageNo = 0;
    this.sort = <string>this.optionsPL.get(this.selectedSort);
    this.getAccommodations(this.criteria, this.pageNo, this.sort);
  }

  onPageChange(event: any) {
    this.pageNo = event.page;
    this.getAccommodations(this.criteria, this.pageNo, this.sort);
    window.scrollTo(0, 0);
  }

  setDefault(){
    if(this.translocoService.getActiveLang() == 'en') {
      this.selectedSort = this.sortOptions[0];
      this.sort = <string>this.options.get(this.selectedSort);
    }else{
      this.selectedSort = this.sortOptionsPL[0];
      this.sort = <string>this.optionsPL.get(this.selectedSort);
    }
  }

  private getAccommodations(criteria: AccommodationCriteriaRQ, pageNumber: number, sort: string) {
    this.accommodationService.filterAccommodations(criteria, pageNumber, 10, sort)
      .pipe(
        catchError((error) => {
          this.toastService.showError('Error during filter accommodations');
          throw error;
        })
      )
      .subscribe(result => {
        this.accommodationList = result;
        for (let element of this.accommodationList.content) {
          this.accommodationService.getAccommodationPhoto(element.id).subscribe(
            photo => {
              const blobUrl = URL.createObjectURL(photo);
              this.photos.set(element.id, blobUrl);
            }
          )
        }
      });
  }

  getAccommodationList(criteria: AccommodationCriteriaRQ) {
    this.criteria = criteria;
    this.numberOfDays = this.dateService.getNumberOfDays(this.criteria.startDate, this.criteria.endDate);
    this.getAccommodations(criteria, 0, this.sort);
  }
}
