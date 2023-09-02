import {TestBed} from '@angular/core/testing';

import {AccommodationServiceImpl} from './accommodation.service';

describe('AccommodationService', () => {
    let service: AccommodationServiceImpl;

    beforeEach(() => {
        TestBed.configureTestingModule({});
        service = TestBed.inject(AccommodationServiceImpl);
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });
});
