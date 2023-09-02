import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Step2PhotosComponent } from './step2-photos.component';

describe('Step2PhotosComponent', () => {
  let component: Step2PhotosComponent;
  let fixture: ComponentFixture<Step2PhotosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ Step2PhotosComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Step2PhotosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
