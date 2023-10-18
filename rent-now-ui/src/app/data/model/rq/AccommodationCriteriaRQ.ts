import {ConvenienceType} from "../common/ConvenienceType";

export interface AccommodationCriteriaRQ {

  startDate: Date;
  endDate: Date;
  city?: string;
  street?: string;
  squareFootage?: number;
  minPrice?: number;
  maxPrice?: number;
  conveniences?: ConvenienceType[];
  maxNoOfPeople?: number;
  name?: string;
}
