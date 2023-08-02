import {Component, OnInit} from '@angular/core';
import {MenuItem} from "primeng/api";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit{

  public avatarMenuItems: MenuItem[] = [];

  constructor(
    private readonly keycloak: KeycloakService
  ) { }

  public ngOnInit() {
    this.initAvatarMenuItems();
  }

  private async initAvatarMenuItems(){
    this.avatarMenuItems = [
      {
        label: 'Sign out',
        icon: 'pi pi-sign-out',
        command: () => {
          this.keycloak.logout()
        },
      }
    ]
  }
}
