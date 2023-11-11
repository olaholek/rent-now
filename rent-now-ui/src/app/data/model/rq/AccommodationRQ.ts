import {AddressRQ} from "./AddressRQ";
import {ConvenienceType} from "../common/ConvenienceType";

export interface AccommodationRQ {
  addressRQ: AddressRQ;
  userUUID: string;
  name: string;
  priceForDay: number;
  squareFootage: number;
  description: string;
  conveniences: Map<ConvenienceType, number>;
  maxNoOfPeople: number;
}
