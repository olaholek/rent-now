import {Component, OnInit} from '@angular/core';
import {MenuItem} from "primeng/api";
import {KeycloakService} from "keycloak-angular";
import {Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit{

  public avatarMenuItems: MenuItem[] = [];

  constructor(
    private readonly keycloak: KeycloakService,
    private readonly router: Router
  ) { }

  public ngOnInit() {
    this.initAvatarMenuItems();
  }

  private async initAvatarMenuItems(){
    this.avatarMenuItems = [
      {
        label: 'My announcements',
        command: () => {
          this.router.navigate(['/announcements'])
        },
      },
      {
        label: 'My reservations',
        command: () => {
          this.router.navigate(['/reservations'])
        },
      },
      {
        label: 'Sign out',
        icon: 'pi pi-sign-out',
        command: () => {
          this.keycloak.logout()
        },
      }
    ]
  }

  public toMainPage() {
    this.router.navigate(['/home']);
  }

  public toFavouritePage() {
    this.router.navigate(['/favourites']);
  }

  public toAddAnnouncementPage() {
    this.router.navigate(['/announcements/add']);
  }
}
