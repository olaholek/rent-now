import {AddressRS} from "./AddressRS";

export interface AccommodationRS {
  id: number;
  addressRS: AddressRS;
  userUUID: string;
  name: string;
  priceForDay: number;
  squareFootage: number;
  description: string;
  conveniences: Map<string, number>;
  maxNoOfPeople: number;
}
