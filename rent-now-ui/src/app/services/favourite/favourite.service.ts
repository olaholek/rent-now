import {Injectable} from '@angular/core';
import {FavouriteService} from "./FavouriteService";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Page} from "../../data/model/common/Page";
import {AccommodationRS} from "../../data/model/rs/AccommodationRS";

@Injectable({
  providedIn: 'root'
})
export class FavouriteServiceImpl implements FavouriteService {

  public readonly baseUrl = '/api/favourites';

  constructor(private readonly httpClient: HttpClient) {
  }

  addToFavourites(uuid: string, accommodationId: number): Observable<string> {
    const httpParams: { [key: string]: string } = {}
    if (uuid != null)
      httpParams['uuid'] = uuid;
    if (accommodationId != null)
      httpParams['accommodationId'] = accommodationId.toString();
    return this.httpClient.post<string>(this.baseUrl, null,{params: httpParams});
  }

  deleteFromFavourites(uuid: string, accommodationId: number): Observable<string> {
    const httpParams: { [key: string]: string } = {}
    if (uuid != null)
      httpParams['uuid'] = uuid;
    if (accommodationId != null)
      httpParams['accommodationId'] = accommodationId.toString();
    return this.httpClient.delete<string>(this.baseUrl, {params: httpParams});
  }

  getFavouritesByUser(uuid: string, page: number, size: number): Observable<Page<AccommodationRS>> {
    const httpParams: { [key: string]: string } = {}
    if (page != null)
      httpParams['page'] = page.toString();
    if (size != null)
      httpParams['size'] = size.toString();
    return this.httpClient.get<Page<AccommodationRS>>(this.baseUrl + '/' + uuid);
  }

  getFavouriteListByUser(uuid: string): Observable<number[]> {
    return this.httpClient.get<number[]>(this.baseUrl + '/' + uuid + '/favouriteList');
  }
}
