import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, expand, reduce, map } from 'rxjs';
import { 
  VisitRequest, 
  VisitResponse,
  UserVisit,
  PageResponse
} from '../models/schedule.model';

@Injectable({
  providedIn: 'root'
})
export class VisitService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api';

  createVisit(tenantId: string, locationId: string, scheduleId: string, request: VisitRequest): Observable<VisitResponse> {
    return this.http.post<VisitResponse>(
      `${this.apiUrl}/tenants/${tenantId}/locations/${locationId}/schedules/${scheduleId}/visits`,
      request
    );
  }

  cancelVisit(tenantId: string, locationId: string, scheduleId: string, visitId: string): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/tenants/${tenantId}/locations/${locationId}/schedules/${scheduleId}/visits/${visitId}`
    );
  }

  getUserVisits(tenantId: string, locationId: string): Observable<UserVisit[]> {
    return this.http.get<PageResponse<UserVisit>>(
      `${this.apiUrl}/tenants/${tenantId}/locations/${locationId}/visits`
    ).pipe(
      map(response => response.content)
    );
  }

  getScheduleVisits(scheduleId: string, date: string): Observable<VisitResponse[]> {
    return this.http.get<PageResponse<VisitResponse>>(`${this.apiUrl}/visits/schedule/${scheduleId}`, {
      params: { date }
    }).pipe(
      map(response => response.content)
    );
  }

  findAllVisits(
    tenantId: string,
    locationId: string,
    dateFrom: string,
    dateTo: string,
    pageSize: number = 20
  ): Observable<VisitResponse[]> {
    // Initial request
    return this.getVisitsPage(tenantId, locationId, dateFrom, dateTo, 0, pageSize).pipe(
      // Use expand to handle pagination
      expand((response) => {
        // If this is the last page, stop
        if (response.last) {
          return [];
        }
        // Otherwise, get the next page
        return this.getVisitsPage(tenantId, locationId, dateFrom, dateTo, response.number + 1, pageSize);
      }),
      // Reduce all pages into a single array of visits
      reduce((acc: VisitResponse[], page) => {
        return [...acc, ...page.content];
      }, [] as VisitResponse[])
    );
  }

  private getVisitsPage(
    tenantId: string,
    locationId: string,
    dateFrom: string,
    dateTo: string,
    page: number,
    size: number
  ): Observable<PageResponse<VisitResponse>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('dateFrom', dateFrom)
      .set('dateTo', dateTo);

    return this.http.get<PageResponse<VisitResponse>>(
      `${this.apiUrl}/tenants/${tenantId}/locations/${locationId}/visits`,
      { params }
    );
  }
} 