import {ConvenienceType} from "../common/ConvenienceType";

export interface BookingRQ {
  userUUID: string;
  accommodationId: number;
  startDate: Date;
  endDate: Date;
  price: number;
  conveniences: Map<ConvenienceType, number>;
}
