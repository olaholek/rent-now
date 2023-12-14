import {Component, OnInit} from '@angular/core';
import {MenuItem, MessageService} from "primeng/api";
import {Subscription} from "rxjs";
import {TranslocoService} from "@ngneat/transloco";

@Component({
  selector: 'app-accommodation-create',
  templateUrl: './accommodation-create.component.html',
  styleUrls: ['./accommodation-create.component.scss']
})
export class AccommodationCreateComponent implements OnInit {

  items: MenuItem[] = [];
  subscription: Subscription | undefined;

  constructor(public translocoService: TranslocoService) {
  }

  ngOnInit() {
    if (this.translocoService.getActiveLang() === 'en') {
      this.items = [{
        label: 'Data',
        routerLink: 'data'
      },
        {
          label: 'Photos',
          routerLink: 'photos'
        }
      ];
    } else {
      this.items = [{
        label: 'Dane',
        routerLink: 'data'
      },
        {
          label: 'Zdjęcia',
          routerLink: 'photos'
        }
      ];
    }
  }
}
