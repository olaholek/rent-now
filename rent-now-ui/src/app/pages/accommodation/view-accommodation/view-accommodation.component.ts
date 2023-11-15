import {Component, OnInit} from '@angular/core';
import {ConvenienceOption} from "../../../data/model/common/ConvenienceOption";
import {AccommodationRS} from "../../../data/model/rs/AccommodationRS";
import {AccommodationServiceImpl} from "../../../services/accommodation/accommodation.service";
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";
import {ConvenienceType, getConvenienceTypeText} from "../../../data/model/common/ConvenienceType";

@Component({
  selector: 'app-view-accommodation',
  templateUrl: './view-accommodation.component.html',
  styleUrls: ['./view-accommodation.component.scss']
})
export class ViewAccommodationComponent implements OnInit {

  photos: string[] = [];
  path !: string;
  visible: boolean = false;
  src !: string;
  numberOfDays !: number;
  accommodationId !: number;

  accommodation !: AccommodationRS;
  freeConveniences: ConvenienceOption[] = [];
  paidConveniences: ConvenienceOption[] = [];

  protected readonly getConvenienceTypeText = getConvenienceTypeText;

  constructor(private readonly accommodationService: AccommodationServiceImpl,
              private readonly route: ActivatedRoute,
              private readonly location: Location) {
    this.route.queryParamMap
      .subscribe(params => {
        this.accommodationId = Number(params.get('id'));
      })
  }

  ngOnInit(): void {
    this.getAccommodation();
  }

  showDialog(src: string) {
    this.visible = true;
    this.src = src;
  }

  loadImages() {
    this.accommodationService.getAccommodationImageNames(this.accommodationId)
      .subscribe(imageNames => {
          this.photos = imageNames
        }
      );
  }

  getAccommodation() {
    this.accommodationService.getAccommodation(this.accommodationId).subscribe(
      accommodation => {
        this.accommodation = accommodation;
        this.path = 'assets/photos/' + this.accommodation.id + '/';

        for (const [type, cost] of Object.entries(this.accommodation.conveniences)) {
          if (cost == null || cost === 0) {
            this.freeConveniences.push({
              convenience: type as unknown as ConvenienceType,
              selected: false,
              additionalCost: cost
            });
          } else {
            this.paidConveniences.push({
              convenience: type as unknown as ConvenienceType,
              selected: false,
              additionalCost: cost
            });
          }
        }

        this.loadImages();
      }
    )
  }

  goBack() {
    this.location.back();
  }
}
