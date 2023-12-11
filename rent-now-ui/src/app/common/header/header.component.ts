import {Component, OnInit} from '@angular/core';
import {MenuItem} from "primeng/api";
import {KeycloakService} from "keycloak-angular";
import {Router} from "@angular/router";
import {TranslocoService} from "@ngneat/transloco";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit{

  public avatarMenuItems: MenuItem[] = [];

  public languagesList:
    Array<Record<'imgUrl' | 'code' | 'name' | 'shorthand', string>> = [
    {
      imgUrl: '/assets/photos/english.png',
      code: 'en',
      name: 'English',
      shorthand: 'ENG',
    },
    {
      imgUrl: '/assets/photos/poland.png',
      code: 'pl',
      name: 'Polish',
      shorthand: 'PL',
    },
  ];

  constructor(
    private readonly keycloak: KeycloakService,
    private readonly router: Router,
    private translocoService: TranslocoService
  ) { }

  public ngOnInit() {
    if(this.translocoService.getActiveLang()=='en') {
      this.initAvatarMenuItems();
    }else{
      this.initAvatarMenuItemsPL();
    }
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

  private async initAvatarMenuItemsPL(){
    this.avatarMenuItems = [
      {
        label: 'Moje ogłoszenia',
        command: () => {
          this.router.navigate(['/announcements'])
        },
      },
      {
        label: 'Moje rezerwacje',
        command: () => {
          this.router.navigate(['/reservations'])
        },
      },
      {
        label: 'Wyloguj się',
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

  public changeLanguage(languageCode: string): void {
    this.translocoService.setActiveLang(languageCode);
    this.ngOnInit();
  }
}
