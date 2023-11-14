import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./pages/home/home.component";
import {ErrorPageComponent} from "./common/error-page/error-page.component";
import {AccommodationCreateComponent} from "./pages/accommodation/accommodation-create/accommodation-create.component";
import {Step1DataComponent} from "./pages/accommodation/step1-data/step1-data.component";
import {Step2PhotosComponent} from "./pages/accommodation/step2-photos/step2-photos.component";
import {BookingComponent} from "./pages/home/booking/booking.component";
import {BookingViewComponent} from "./pages/home/booking/booking-view/booking-view.component";
import {AllBookingsComponent} from "./pages/home/booking/all-bookings/all-bookings.component";
import {UserAccommodationsComponent} from "./pages/accommodation/user-accommodations/user-accommodations.component";
import {ViewAccommodationComponent} from "./pages/accommodation/view-accommodation/view-accommodation.component";

const routes: Routes = [
  {path: 'home', component: HomeComponent},
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  {path: 'booking', component: BookingComponent, pathMatch: 'full'},
  {path: 'accommodations/view', component: ViewAccommodationComponent, pathMatch: 'full'},
  {path: 'announcements', component: UserAccommodationsComponent, pathMatch: 'full'},
  {path: 'reservations', component: AllBookingsComponent, pathMatch: 'full'},
  {path: 'booking/view', component: BookingViewComponent, pathMatch: 'full'},
  {
    path: 'announcements/add', component: AccommodationCreateComponent,
    children: [
      {path: '', redirectTo: 'data', pathMatch: 'full'},
      {path: 'data', component: Step1DataComponent},
      {path: 'photos', component: Step2PhotosComponent},
    ]
  },
  {path: '**', component: ErrorPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
