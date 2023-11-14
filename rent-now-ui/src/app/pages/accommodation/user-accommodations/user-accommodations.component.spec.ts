import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserAccommodationsComponent } from './user-accommodations.component';

describe('UserAccommodationsComponent', () => {
  let component: UserAccommodationsComponent;
  let fixture: ComponentFixture<UserAccommodationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserAccommodationsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserAccommodationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
