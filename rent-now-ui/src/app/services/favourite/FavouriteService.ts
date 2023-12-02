import {Observable} from "rxjs";
import {Page} from "../../data/model/common/Page";
import {AccommodationRS} from "../../data/model/rs/AccommodationRS";

export interface FavouriteService {

  addToFavourites(uuid: string, accommodationId: number): Observable<string>;

  deleteFromFavourites(uuid: string, accommodationId: number): Observable<string>;

  getFavouritesByUser(uuid: string, page: number, size: number): Observable<Page<AccommodationRS>>;

  getFavouriteListByUser(uuid: string): Observable<number[]>;
}
