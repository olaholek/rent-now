import {Component, OnInit} from '@angular/core';
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  public constructor(private keycloakService: KeycloakService) {
  }

  ngOnInit(): void {
    console.log('check')
    this.keycloakService.loadUserProfile().then(
      err => {
        console.log(err)
      }
    );
  }

  title = 'rent-now-ui';
}
