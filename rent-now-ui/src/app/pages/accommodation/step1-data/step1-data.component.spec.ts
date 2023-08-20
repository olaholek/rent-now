import {ComponentFixture, TestBed} from '@angular/core/testing';

import {Step1DataComponent} from './step1-data.component';

describe('Step1DataComponent', () => {
    let component: Step1DataComponent;
    let fixture: ComponentFixture<Step1DataComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [Step1DataComponent]
        })
            .compileComponents();

        fixture = TestBed.createComponent(Step1DataComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
