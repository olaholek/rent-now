import {Component, OnInit} from '@angular/core';
import {MessageService} from "primeng/api";
import {ActivatedRoute} from "@angular/router";
import {AccommodationServiceImpl} from "../../../services/accommodation/accommodation.service";

@Component({
  selector: 'app-step2-photos',
  templateUrl: './step2-photos.component.html',
  styleUrls: ['./step2-photos.component.scss']
})
export class Step2PhotosComponent implements OnInit {

  id: string | null | undefined;

  constructor(private readonly accommodationService: AccommodationServiceImpl,
              private readonly route: ActivatedRoute,
              private messageService: MessageService) {
    this.route.queryParamMap
      .subscribe(params => {
        this.id = params.get('id');
      })
  }

  ngOnInit(): void {
  }

  onUpload(event: { files: Blob[] }) {
    this.accommodationService.addPhotos(this.id, event.files).subscribe(
      () => {
        this.messageService.add({severity: 'success', summary: 'Photos saved', detail: ''});
      }
    );
  }
}
