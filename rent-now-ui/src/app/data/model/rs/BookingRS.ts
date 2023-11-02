import {AccommodationRS} from "./AccommodationRS";

export interface BookingRS {
  id: number;
  userUUID: string;
  accommodationRS: AccommodationRS;
  startDate: Date;
  endDate: Date;
  bookingDate: Date;
  price: number;
  status: string;
  conveniences: Map<string, number>;
}
