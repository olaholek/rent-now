import {TestBed} from '@angular/core/testing';
import {BookingService} from "./BookingService";
import {BookingServiceImpl} from "./booking.service";


describe('BookingService', () => {
  let service: BookingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BookingServiceImpl);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
