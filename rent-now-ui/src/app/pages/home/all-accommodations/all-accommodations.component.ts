import {Component, Input, OnInit} from '@angular/core';
import {Page} from "../../../data/model/common/Page";
import {AccommodationRS} from "../../../data/model/rs/AccommodationRS";
import {SafeUrl} from "@angular/platform-browser";
import {Router} from "@angular/router";
import {AccommodationServiceImpl} from "../../../services/accommodation/accommodation.service";

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

  constructor(private readonly router: Router, private readonly accommodationService: AccommodationServiceImpl,) {
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
    this.router.navigate(['accommodation/view'], {
      queryParams: {
        id: accommodationId,
        startDate: this.accommodationService.buildDateToSendInJSON(this.startDate),
        endDate: this.accommodationService.buildDateToSendInJSON(this.endDate), mode: 'reserve'
      }
    })
  }
}
