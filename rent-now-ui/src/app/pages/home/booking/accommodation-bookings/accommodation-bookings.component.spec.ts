import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccommodationBookingsComponent } from './accommodation-bookings.component';

describe('AccommodationBookingsComponent', () => {
  let component: AccommodationBookingsComponent;
  let fixture: ComponentFixture<AccommodationBookingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AccommodationBookingsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccommodationBookingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
