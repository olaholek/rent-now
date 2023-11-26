import {Component, OnInit} from '@angular/core';
import {ConvenienceOption} from "../../../data/model/common/ConvenienceOption";
import {AccommodationRS} from "../../../data/model/rs/AccommodationRS";
import {ActivatedRoute, Router} from "@angular/router";
import {AccommodationServiceImpl} from "../../../services/accommodation/accommodation.service";
import {ConvenienceType} from "../../../data/model/common/ConvenienceType";
import {ToastService} from "../../../services/toast/toast.service";
import {catchError} from "rxjs";
import {ConfirmationService} from "primeng/api";
import {Location} from "@angular/common";
import {AccommodationRQ} from "../../../data/model/rq/AccommodationRQ";
import {AddressRQ} from "../../../data/model/rq/AddressRQ";
import {AddressRS} from "../../../data/model/rs/AddressRS";

@Component({
  selector: 'app-edit-accommodation',
  templateUrl: './edit-accommodation.component.html',
  styleUrls: ['./edit-accommodation.component.scss']
})
export class EditAccommodationComponent implements OnInit {

  categories: ConvenienceOption[] = [];
  accommodationId !: number;
  accommodation !: AccommodationRS;

  path !: string;
  visible: boolean = false;
  src !: string;

  photos: string[] = [];
  uploadedFiles: any[] = [];

  constructor(private readonly confirmationService: ConfirmationService,
              private readonly toastService: ToastService,
              private readonly accommodationService: AccommodationServiceImpl,
              private readonly route: ActivatedRoute,
              private readonly router: Router,
              private readonly location: Location) {
    this.route.queryParamMap.subscribe(params => {
      this.accommodationId = Number(params.get('id'));
      this.getAccommodation();
    })
  }

  ngOnInit(): void {
  }

  setConveniences(): void {
    for (const conv of Object.values(ConvenienceType)) {
      this.categories.push({
        convenience: conv,
        selected: this.getIsSelected(conv as ConvenienceType),
        additionalCost: this.getCost(conv as ConvenienceType)
      } as ConvenienceOption)
    }
    this.categories = this.categories.filter((category, index) => index < 11);
  }

  getAccommodation(): void {
    this.accommodationService.getAccommodation(this.accommodationId)
      .subscribe(result => {
        this.accommodation = result;
        this.setConveniences();
        this.path = 'assets/photos/' + this.accommodation.id + '/';
        this.accommodationService.getAccommodationImageNames(this.accommodationId)
          .subscribe(imageNames => {
              this.photos = imageNames
            }
          );

      })
  }

  showDialog(src: string) {
    this.visible = true;
    this.src = src;
  }

  getCost(type: ConvenienceType): number {
    for (const [convenience, cost] of Object.entries(this.accommodation.conveniences)) {
      if (convenience === type.toString()) {
        return cost;
      }
    }
    return 0;
  }

  getIsSelected(type: ConvenienceType): boolean {
    for (const [convenience, cost] of Object.entries(this.accommodation.conveniences)) {
      if (type.toString() === convenience) {
        return true;
      }
    }
    return false;
  }

  onSelectionChange(option: ConvenienceOption) {
    option.selected = !option.selected;
  }

  edit(): void {
    const selectedConveniences = this.categories.filter(category => category.selected);
    const conveniencesToSave = new Map<ConvenienceType, number>();

    for (const category of selectedConveniences) {
      conveniencesToSave.set(category.convenience, category.additionalCost);
    }

    this.accommodationService.updateAccommodation(this.accommodationId, this.mapAccommodation(), conveniencesToSave)
      .pipe(
        catchError((error) => {
          this.toastService.showError('Error during update announcement.');
          throw error;
        })
      )
      .subscribe((res) => {
        this.toastService.showSuccess('Announcement updated successfully.');
        this.router.navigate(['accommodations/view'], {queryParams: {id: res.id}});
      });
  }

  onUpload(event: { files: Blob[] }) {
    this.accommodationService.addPhotos(this.accommodationId.toString(), event.files).pipe(
      catchError((error) => {
        this.toastService.showError('Error during uploading photos.');
        throw error;
      })
    )
      .subscribe((res) => {
        location.reload();
        this.toastService.showSuccess('Photos uploaded successfully.');
      });
  }

  protected beutify(type: ConvenienceType): string {
    return this.capitalizeFirstLetter(type.toString().toLowerCase().replace('_', ' '));
  }

  capitalizeFirstLetter(str: string): string {
    return str.charAt(0).toUpperCase() + str.slice(1);
  }

  public onDeleteClick(event: Event, photo: string): void {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Are you sure you want to delete this photo?',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.accommodationService.deletePhoto(photo, this.accommodationId)
          .pipe(
            catchError((error) => {
              this.toastService.showError('Error during deleting photo.');
              throw error;
            })
          )
          .subscribe((res) => {
            this.photos = this.photos.filter(deletedPhoto => photo != deletedPhoto);
            this.toastService.showSuccess('Photo deleted successfully.');
          });
      },
      reject: () => {
      }
    })
  }

  goBack() {
    this.location.back();
  }

  mapAccommodation(): AccommodationRQ {
    return {
      addressRQ: this.mapAddress(this.accommodation.addressRS),
      userUUID: this.accommodation.userUUID,
      name: this.accommodation.name,
      priceForDay: this.accommodation.priceForDay,
      squareFootage: this.accommodation.squareFootage,
      description: this.accommodation.description,
      maxNoOfPeople: this.accommodation.maxNoOfPeople,
      conveniences: new Map<ConvenienceType, number>()
    };
  }

  mapAddress(addressRS: AddressRS): AddressRQ {
    return {
      city: addressRS.city,
      street: addressRS.street,
      houseNumber: addressRS.houseNumber,
      apartmentNumber: addressRS.apartmentNumber,
      postalCode: addressRS.postalCode,
      post: addressRS.post,
      country: addressRS.country,
      county: addressRS.county,
      province: addressRS.province
    };
  }
}
