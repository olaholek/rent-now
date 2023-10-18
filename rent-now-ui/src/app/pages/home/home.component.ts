import {Component, OnInit} from '@angular/core';
import {AccommodationCriteriaRQ} from "../../data/model/rq/AccommodationCriteriaRQ";
import {Page} from "../../data/model/common/Page";
import {AccommodationRS} from "../../data/model/rs/AccommodationRS";
import {catchError} from "rxjs";
import {AccommodationServiceImpl} from "../../services/accommodation/accommodation.service";
import {ToastService} from "../../services/toast/toast.service";
import {SafeUrl} from "@angular/platform-browser";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  accommodationList !: Page<AccommodationRS>;
  pageNo !: number;
  photos = new Map<number, SafeUrl>();
  numberOfDays !: number;
  sort !: string;
  //todo zrobić wybór sortowania <select>

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
  ) {
    this.criteria.startDate = new Date();
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    this.criteria.endDate = tomorrow;
    this.sort = 'id,desc'
    this.numberOfDays = accommodationService.getNumberOfDays(this.criteria.startDate, this.criteria.endDate);
  }

  ngOnInit(): void {
    this.pageNo = 0;
    this.getAccommodations(this.criteria, this.pageNo, this.sort);
  }

  onPageChange(event: any) {
    this.pageNo = event.page;
    this.getAccommodations(this.criteria, this.pageNo, this.sort);
    window.scrollTo(0, 0);
  }

  private getAccommodations(criteria: AccommodationCriteriaRQ, pageNumber: number, sort: string) {
    this.accommodationService.filterAccommodations(criteria, pageNumber, 10, sort)
      .pipe(
        catchError((error) => {
          this.toastService.showError('');
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
    this.getAccommodations(criteria, 0, this.sort);
  }
}
