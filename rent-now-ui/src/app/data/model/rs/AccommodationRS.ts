import {AddressRS} from "./AddressRS";
import bigDecimal from "js-big-decimal";
import {ConvenienceType} from "../common/ConvenienceType";

export interface AccommodationRS {
    id: number;
    addressRS: AddressRS;
    userUUID: string;
    name: string;
    priceForDay: bigDecimal;
    squareFootage: bigDecimal;
    description: string;
    conveniences: Map<ConvenienceType, bigDecimal>;
    maxNoOfPeople: number;
}
