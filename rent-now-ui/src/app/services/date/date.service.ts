import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DateService {

  constructor() { }

  buildDateToSendInJSON(date: Date): string {
    let rightMonth = date.getMonth() + 1;
    let month = rightMonth < 10 ? '0' + rightMonth.toString() : rightMonth;
    let day = date.getDate() < 10 ? '0' + date.getDate().toString() : date.getDate();
    return date.getFullYear().toString() + '-' +
      month + '-' + day
  }

  getNumberOfDays(startDate: Date, endDate: Date): number {
    const start = new Date(startDate);
    const end = new Date(endDate);
    const timeDifference = end.getTime() - start.getTime();
    return Math.ceil((timeDifference) / (1000 * 60 * 60 * 24));
  }
}
