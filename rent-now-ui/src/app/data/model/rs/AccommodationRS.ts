import {AddressRS} from "./AddressRS";
import {ConvenienceType} from "../common/ConvenienceType";

export interface AccommodationRS {
  id: number;
  addressRS: AddressRS;
  userUUID: string;
  name: string;
  priceForDay: number;
  squareFootage: number;
  description: string;
  conveniences: Map<ConvenienceType, number>;
  maxNoOfPeople: number;
}
