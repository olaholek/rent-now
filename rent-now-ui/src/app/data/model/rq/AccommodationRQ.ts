import {AddressRQ} from "./AddressRQ";
import bigDecimal from "js-big-decimal";
import {ConvenienceType} from "../common/ConvenienceType";

export interface AccommodationRQ {
    addressRQ: AddressRQ;
    userUUID: string;
    name: string;
    priceForDay: bigDecimal;
    squareFootage: bigDecimal;
    description: string;
    conveniences: Map<ConvenienceType, string>;
    maxNoOfPeople: number;
}
