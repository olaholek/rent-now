import {APP_INITIALIZER, NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {KeycloakAngularModule, KeycloakBearerInterceptor, KeycloakService} from "keycloak-angular";
import { HeaderComponent } from './common/header/header.component';
import { FooterComponent } from './common/footer/footer.component';
import { HomeComponent } from './pages/home/home.component';
import { ErrorPageComponent } from './common/error-page/error-page.component';
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {ButtonModule} from "primeng/button";
import {SidebarModule} from "primeng/sidebar";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MenuModule} from "primeng/menu";
import {AvatarModule} from "primeng/avatar";

function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: {
        url: 'http://localhost:8443',
        realm: 'rent-now',
        clientId: 'rent-now-ui'
      },
      initOptions: {
        onLoad: 'login-required',
        silentCheckSsoRedirectUri:
          window.location.origin + '/assets/silent-check-sso.html'
      },
      bearerExcludedUrls:['/assets']
    });
}

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    ErrorPageComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        KeycloakAngularModule,
        ButtonModule,
        SidebarModule,
        BrowserAnimationsModule,
        MenuModule,
        AvatarModule
    ],
  providers: [
    {
    provide: APP_INITIALIZER,
    useFactory: initializeKeycloak,
    multi: true,
    deps: [KeycloakService]
    },
    {
      provide : HTTP_INTERCEPTORS,
      useClass: KeycloakBearerInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
