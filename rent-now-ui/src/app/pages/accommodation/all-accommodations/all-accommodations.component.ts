import {Component, Input, OnInit} from '@angular/core';
import {Page} from "../../../data/model/common/Page";
import {AccommodationRS} from "../../../data/model/rs/AccommodationRS";
import {SafeUrl} from "@angular/platform-browser";
import {Router} from "@angular/router";
import {DateService} from "../../../services/date/date.service";

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

  constructor(private readonly router: Router,
              private readonly dateService: DateService) {
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
    this.router.navigate(['booking'], {
      queryParams: {
        id: accommodationId,
        startDate: this.dateService.buildDateToSendInJSON(this.startDate),
        endDate: this.dateService.buildDateToSendInJSON(this.endDate), mode: 'reserve'
      }
    })
  }
}
