import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SchedulePageItemResponse } from '../models/schedule.model';

@Injectable({
  providedIn: 'root'
})
export class ScheduleService {
  private readonly http = inject(HttpClient);

  getSchedules(
    tenantId: string,
    locationId: string
  ): Observable<SchedulePageItemResponse[]> {
    return this.http.get<SchedulePageItemResponse[]>(
      `/api/tenants/${tenantId}/locations/${locationId}/schedules`
    );
  }
} 