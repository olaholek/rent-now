import {APP_INITIALIZER, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {KeycloakAngularModule, KeycloakBearerInterceptor, KeycloakService} from "keycloak-angular";
import {HeaderComponent} from './common/header/header.component';
import {FooterComponent} from './common/footer/footer.component';
import {HomeComponent} from './pages/home/home.component';
import {ErrorPageComponent} from './common/error-page/error-page.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {ButtonModule} from "primeng/button";
import {SidebarModule} from "primeng/sidebar";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MenuModule} from "primeng/menu";
import {AvatarModule} from "primeng/avatar";
import {AccommodationCreateComponent} from './pages/accommodation/accommodation-create/accommodation-create.component';
import {CheckboxModule} from "primeng/checkbox";
import {FormsModule} from '@angular/forms';
import {InputTextareaModule} from "primeng/inputtextarea";
import {FileUploadModule} from "primeng/fileupload";
import {MessageService} from "primeng/api";
import {Step1DataComponent} from './pages/accommodation/step1-data/step1-data.component';
import {Step2PhotosComponent} from './pages/accommodation/step2-photos/step2-photos.component';
import {StepsModule} from "primeng/steps";
import {ToastModule} from "primeng/toast";
import {FilterComponent} from './pages/home/filter/filter.component';
import {AllAccommodationsComponent} from './pages/accommodation/all-accommodations/all-accommodations.component';
import {CalendarModule} from "primeng/calendar";
import {PaginatorModule} from "primeng/paginator";
import {GalleriaModule} from "primeng/galleria";
import {ImageModule} from "primeng/image";
import {BookingComponent} from './pages/home/booking/booking.component';
import {DialogModule} from "primeng/dialog";
import {NgOptimizedImage} from "@angular/common";
import { BookingViewComponent } from './pages/home/booking/booking-view/booking-view.component';
import {TagModule} from "primeng/tag";
import { AllBookingsComponent } from './pages/home/booking/all-bookings/all-bookings.component';
import { UserAccommodationsComponent } from './pages/accommodation/user-accommodations/user-accommodations.component';
import { ViewAccommodationComponent } from './pages/accommodation/view-accommodation/view-accommodation.component';

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
      bearerExcludedUrls: ['/assets']
    });
}

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    ErrorPageComponent,
    AccommodationCreateComponent,
    Step1DataComponent,
    Step2PhotosComponent,
    FilterComponent,
    AllAccommodationsComponent,
    BookingComponent,
    BookingViewComponent,
    AllBookingsComponent,
    UserAccommodationsComponent,
    ViewAccommodationComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        KeycloakAngularModule,
        ButtonModule,
        SidebarModule,
        BrowserAnimationsModule,
        MenuModule,
        AvatarModule,
        CheckboxModule,
        FormsModule,
        InputTextareaModule,
        FileUploadModule,
        HttpClientModule,
        StepsModule,
        ToastModule,
        CalendarModule,
        PaginatorModule,
        GalleriaModule,
        ImageModule,
        DialogModule,
        NgOptimizedImage,
        TagModule
    ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService]
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: KeycloakBearerInterceptor,
      multi: true
    },
    MessageService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
