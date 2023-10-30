import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./pages/home/home.component";
import {ErrorPageComponent} from "./common/error-page/error-page.component";
import {AccommodationCreateComponent} from "./pages/accommodation/accommodation-create/accommodation-create.component";
import {Step1DataComponent} from "./pages/accommodation/step1-data/step1-data.component";
import {Step2PhotosComponent} from "./pages/accommodation/step2-photos/step2-photos.component";
import {AccommodationViewComponent} from "./pages/home/accommodation-view/accommodation-view.component";

const routes: Routes = [
  {path: 'home', component: HomeComponent},
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  {path: 'accommodation/view', component: AccommodationViewComponent},
  {path: 'announcements/add', component: AccommodationCreateComponent,
    children: [
      { path: '', redirectTo: 'data', pathMatch: 'full' },
      { path: 'data', component: Step1DataComponent },
      { path: 'photos', component: Step2PhotosComponent },
    ]},
  {path: '**', component: ErrorPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
