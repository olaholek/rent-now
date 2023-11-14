import {Component, OnInit} from '@angular/core';
import {AccommodationServiceImpl} from "../../../services/accommodation/accommodation.service";
import {AccommodationRS} from "../../../data/model/rs/AccommodationRS";
import {Page} from "../../../data/model/common/Page";
import {SafeUrl} from "@angular/platform-browser";
import {ActivatedRoute, Router} from "@angular/router";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-user-accommodations',
  templateUrl: './user-accommodations.component.html',
  styleUrls: ['./user-accommodations.component.scss']
})
export class UserAccommodationsComponent implements OnInit {

  pageNo !: number;
  uuid !: string;
  photos = new Map<number, SafeUrl>();
  accommodationList !: Page<AccommodationRS>;

  constructor(private readonly router: Router,
              private readonly accommodationService: AccommodationServiceImpl,
              private readonly route: ActivatedRoute,
              private readonly keycloak: KeycloakService) {
    this.route.queryParamMap.subscribe(params => {
      this.keycloak.getKeycloakInstance().loadUserProfile().then(profile => {
        this.uuid = profile.id!;
        this.getAccommodations(0);
      })
    })
  }

  ngOnInit(): void {
  }

  private getAccommodations(pageNumber: number) {
    let sort = "id,desc"
    this.accommodationService.getUserAccommodations(this.uuid, pageNumber, 10, sort)
      .subscribe(result => {
        this.accommodationList = result;
        for (let element of this.accommodationList.content) {
          this.accommodationService.getAccommodationPhoto(element.id).subscribe(
            photo => {
              const blobUrl = URL.createObjectURL(photo);
              this.photos.set(element.id, blobUrl);
            }
          )
        }
      })
  }

  getPhoto(id: number): SafeUrl {
    return <SafeUrl>this.photos.get(id);
  }

  onPageChange(event: any) {
    this.pageNo = event.page;
    this.getAccommodations(this.pageNo);
    window.scrollTo(0, 0);
  }

  showDetails(accommodationId: number) {
    this.router.navigate(['accommodations/view'], {
      queryParams: {
        id: accommodationId
      }
    })
  }
}
