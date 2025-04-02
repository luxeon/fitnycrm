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

export interface ScheduleDetailsResponse extends ScheduleListItemResponse {
  trainingName: string;
  defaultTrainerName: string;
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

  getSchedule(tenantId: string, locationId: string, scheduleId: string): Observable<ScheduleDetailsResponse> {
    return this.http.get<ScheduleDetailsResponse>(`/api/tenants/${tenantId}/locations/${locationId}/schedules/${scheduleId}`);
  }

  createSchedule(tenantId: string, locationId: string, request: CreateScheduleRequest): Observable<ScheduleDetailsResponse> {
    return this.http.post<ScheduleDetailsResponse>(`/api/tenants/${tenantId}/locations/${locationId}/schedules`, request);
  }

  updateSchedule(tenantId: string, locationId: string, scheduleId: string, request: UpdateScheduleRequest): Observable<ScheduleDetailsResponse> {
    return this.http.put<ScheduleDetailsResponse>(`/api/tenants/${tenantId}/locations/${locationId}/schedules/${scheduleId}`, request);
  }

  deleteSchedule(tenantId: string, locationId: string, scheduleId: string): Observable<void> {
    return this.http.delete<void>(`/api/tenants/${tenantId}/locations/${locationId}/schedules/${scheduleId}`);
  }
} 