import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ScheduleListItemResponse {
  id: string;
  trainingId: string;
  daysOfWeek: string[];
  startTime: string;
  endTime: string;
  defaultTrainerId: string;
}

export interface CreateScheduleRequest {
  trainingId: string;
  daysOfWeek: string[];
  startTime: string;
  endTime: string;
  defaultTrainerId: string;
}

export interface UpdateScheduleRequest extends CreateScheduleRequest {}

@Injectable({
  providedIn: 'root'
})
export class ScheduleService {
  private readonly http = inject(HttpClient);

  getSchedules(tenantId: string, locationId: string): Observable<ScheduleListItemResponse[]> {
    return this.http.get<ScheduleListItemResponse[]>(`/api/tenants/${tenantId}/locations/${locationId}/schedules`);
  }

  createSchedule(tenantId: string, locationId: string, request: CreateScheduleRequest): Observable<ScheduleListItemResponse> {
    return this.http.post<ScheduleListItemResponse>(`/api/tenants/${tenantId}/locations/${locationId}/schedules`, request);
  }

  updateSchedule(tenantId: string, locationId: string, scheduleId: string, request: UpdateScheduleRequest): Observable<ScheduleListItemResponse> {
    return this.http.put<ScheduleListItemResponse>(`/api/tenants/${tenantId}/locations/${locationId}/schedules/${scheduleId}`, request);
  }

  deleteSchedule(tenantId: string, locationId: string, scheduleId: string): Observable<void> {
    return this.http.delete<void>(`/api/tenants/${tenantId}/locations/${locationId}/schedules/${scheduleId}`);
  }
} 