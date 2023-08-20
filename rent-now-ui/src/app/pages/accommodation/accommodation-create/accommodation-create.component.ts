import {Component, OnInit} from '@angular/core';
import {MenuItem, MessageService} from "primeng/api";
import {Subscription} from "rxjs";

@Component({
    selector: 'app-accommodation-create',
    templateUrl: './accommodation-create.component.html',
    styleUrls: ['./accommodation-create.component.scss']
})
export class AccommodationCreateComponent implements OnInit {

    items: MenuItem[] = [];
    subscription: Subscription | undefined;

    constructor(public messageService: MessageService) {
    }

    ngOnInit() {
        this.items = [{
            label: 'Data',
            routerLink: 'data'
        },
            {
                label: 'Photos',
                routerLink: 'photos'
            }
        ];
    }

    ngOnDestroy() {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
    }
}
