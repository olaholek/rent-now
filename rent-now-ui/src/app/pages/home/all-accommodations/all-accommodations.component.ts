import {Component, Input, OnInit} from '@angular/core';
import {Page} from "../../../data/model/common/Page";
import {AccommodationRS} from "../../../data/model/rs/AccommodationRS";
import {DomSanitizer, SafeUrl} from "@angular/platform-browser";

@Component({
  selector: 'app-all-accommodations',
  templateUrl: './all-accommodations.component.html',
  styleUrls: ['./all-accommodations.component.scss']
})
export class AllAccommodationsComponent implements OnInit {

  @Input() accommodationList !: Page<AccommodationRS>;
  @Input() photos = new Map<number, SafeUrl>();
  @Input() numberOfDays !: number;
  showEmptyHeart: boolean = false;

  constructor(private sanitizer: DomSanitizer) {
  }

  getPhoto(id: number): SafeUrl {
    return <SafeUrl>this.photos.get(id);
  }

  ngOnInit(): void {
  }

  getCost(priceForDay: number): number {
    return Number(priceForDay) * this.numberOfDays;
  }

  showDetails(accommodationId: number) {
    //id, startDate, endDate
  }
}
