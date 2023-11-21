import {Component, OnInit} from '@angular/core';
import {ConvenienceOption} from "../../../data/model/common/ConvenienceOption";
import {AccommodationRS} from "../../../data/model/rs/AccommodationRS";
import {AccommodationServiceImpl} from "../../../services/accommodation/accommodation.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Location} from "@angular/common";
import {ConvenienceType, getConvenienceTypeText} from "../../../data/model/common/ConvenienceType";
import {ConfirmationService} from "primeng/api";
import {catchError} from "rxjs";
import {ToastService} from "../../../services/toast/toast.service";

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
              private readonly router: Router,
              private readonly toastService: ToastService,
              private readonly location: Location,
              private readonly confirmationService: ConfirmationService) {
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

  showReservations(): void {
    this.router.navigate(['accommodation-reservations'], {queryParams: {id: this.accommodationId}});
  }

  editAnnouncement(): void {
    this.router.navigate(['edit-accommodation'], {queryParams: {id: this.accommodationId}});
  }

  deleteAnnouncement(): void {
    this.accommodationService.deleteAccommodation(this.accommodationId).pipe(catchError((error) => {
      this.toastService.showError('Error during deleting announcement.');
      throw error;
    }))
      .subscribe((res) => {
        this.toastService.showSuccess('Announcement deleted successfully.');
        this.goBack()
      });
  }

  confirm() {
    this.confirmationService.confirm({
      message: 'Are you sure that you want to delete this announcement?',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.deleteAnnouncement();
      },
      reject: () => {
      }
    });
  }
}
