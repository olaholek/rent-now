import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FavouritesAccommodationComponent } from './favourites-accommodation.component';

describe('FavouritesAccommodationComponent', () => {
  let component: FavouritesAccommodationComponent;
  let fixture: ComponentFixture<FavouritesAccommodationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FavouritesAccommodationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FavouritesAccommodationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
