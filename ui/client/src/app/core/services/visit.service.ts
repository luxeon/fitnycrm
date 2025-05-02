import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { 
  VisitRequest, 
  VisitResponse,
  UserVisit 
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
    return this.http.get<UserVisit[]>(
      `${this.apiUrl}/tenants/${tenantId}/locations/${locationId}/visits`
    );
  }

  getScheduleVisits(scheduleId: string, date: string): Observable<VisitResponse[]> {
    return this.http.get<VisitResponse[]>(`${this.apiUrl}/visits/schedule/${scheduleId}`, {
      params: { date }
    });
  }
} 