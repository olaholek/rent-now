import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AccommodationServiceImpl} from "../../../services/accommodation/accommodation.service";
import {ToastService} from "../../../services/toast/toast.service";
import {catchError} from "rxjs";
import {TranslocoService} from "@ngneat/transloco";

@Component({
  selector: 'app-step2-photos',
  templateUrl: './step2-photos.component.html',
  styleUrls: ['./step2-photos.component.scss']
})
export class Step2PhotosComponent implements OnInit {

  id: string | null | undefined;
  uploadedFiles: any[] = [];
  isPolish !: boolean;

  chooseLabelEnglish = 'Choose';
  uploadLabelEnglish = 'Upload';
  cancelLabelEnglish = 'Cancel';

  chooseLabelPolish = 'Wybierz';
  uploadLabelPolish = 'Prześlij';
  cancelLabelPolish = 'Anuluj';

  constructor(private readonly accommodationService: AccommodationServiceImpl,
              private readonly route: ActivatedRoute,
              private readonly toastService: ToastService,
              private readonly router: Router,
              private readonly translocoService: TranslocoService) {
    this.route.queryParamMap
      .subscribe(params => {
        this.id = params.get('id');
      })
  }

  ngOnInit(): void {
    this.isPolish = this.translocoService.getActiveLang() === 'pl';
  }

  onUpload(event: { files: Blob[] }) {
    this.accommodationService.addPhotos(this.id, event.files).pipe(
      catchError((error) => {
        this.toastService.showError('Error during uploading photos.');
        throw error;
      })
    )
      .subscribe((res) => {
        this.toastService.showSuccess('Photos uploaded successfully.');
        this.router.navigate(['accommodations/view'], {queryParams: {id: res.id}});
      });
  }
}
