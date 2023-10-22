import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {AccommodationCriteriaRQ} from "../../../data/model/rq/AccommodationCriteriaRQ";
import {ConvenienceType} from "../../../data/model/common/ConvenienceType";

@Component({
  selector: 'app-filter',
  templateUrl: './filter.component.html',
  styleUrls: ['./filter.component.scss']
})
export class FilterComponent implements OnInit {
  showFilters = false;
  conveniencesList: ConvenienceType[] = [];
  minStartDate = new Date();
  minEndDate = new Date();
  startDateToValid = new Date();
  invalidDateError = false;
  @Output() filtersEmitter = new EventEmitter<AccommodationCriteriaRQ>();

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

  constructor() {
  }

  ngOnInit(): void {
    for (const conv of Object.values(ConvenienceType)) {
      this.conveniencesList.push(conv as ConvenienceType);
    }
    this.conveniencesList = this.conveniencesList.filter((category, index) => index < 11);

    this.minEndDate.setDate(this.minStartDate.getDate() + 1);
    this.value.endDate.setDate(this.minEndDate.getDate());
  }

  toggleFilters() {
    this.showFilters = !this.showFilters;
  }

  search(): void {
    this.filtersEmitter.emit(this.value);
  }

  protected beutify(type: ConvenienceType): string {
    return this.capitalizeFirstLetter(type.toString().toLowerCase().replace('_', ' '));
  }

  capitalizeFirstLetter(str: string): string {
    return str.charAt(0).toUpperCase() + str.slice(1);
  }

  onStartDateChange(event: Date) {
    const today = new Date(Date.now());
    this.startDateToValid = event;
    this.invalidDateError = this.startDateToValid === null
      || (this.startDateToValid.getFullYear() < today.getFullYear() &&
        this.startDateToValid.getMonth() < today.getMonth() &&
        this.startDateToValid.getDate() < today.getDate())
      || (this.startDateToValid.getFullYear() >= this.value.endDate.getFullYear() &&
        this.startDateToValid.getMonth() >= this.value.endDate.getMonth() &&
        this.startDateToValid.getDate() >= this.value.endDate.getDate());
  }

  onEndDateChange(event: Date) {
    this.invalidDateError = event === null || event <= this.startDateToValid;
  }
}
