import bigDecimal from "js-big-decimal";
import {ConvenienceType} from "../common/ConvenienceType";

export interface AccommodationCriteriaRQ {

  startDate: Date;
  endDate: Date;
  city?: string;
  street?: string;
  squareFootage?: bigDecimal;
  minPrice?: bigDecimal;
  maxPrice?: bigDecimal;
  conveniences?: ConvenienceType[];
  maxNoOfPeople?: number;
  name?: string;
}
