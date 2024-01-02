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
    this.changeLanguage(this.translocoService.getActiveLang());
  }

  private async initAvatarMenuItems(languageCode: string): Promise<void> {
    this.avatarMenuItems = [
      {
        label: languageCode === 'en' ? 'My announcements' : 'Moje ogłoszenia',
        command: () => {
          this.router.navigate(['/announcements'])
        },
      },
      {
        label: languageCode === 'en' ? 'My favourites' : 'Moje ulubione',
        command: () => {
          this.router.navigate(['/favourites'])
        },
      },
      {
        label: languageCode === 'en' ? 'My reservations' : 'Moje rezerwacje',
        command: () => {
          this.router.navigate(['/reservations'])
        },
      },
      {
        label: languageCode === 'en' ? 'Sign out' : 'Wyloguj się',
        icon: 'pi pi-sign-out',
        command: () => {
          this.keycloak.logout()
        },
      }
    ];
  }

  public toMainPage() {
    this.router.navigate(['/home']);
  }

  public toAddAnnouncementPage() {
    this.router.navigate(['/announcements/add']);
  }

  public changeLanguage(languageCode: string): void {
    this.translocoService.setActiveLang(languageCode);
    localStorage.setItem('lang', languageCode);
    this.initAvatarMenuItems(languageCode);
  }
}
