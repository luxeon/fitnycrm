import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Page } from '../models/page.model';

export interface TrainingDetailsResponse {
  id: string;
  name: string;
  description?: string;
  durationMinutes: number;
  createdAt: string;
  updatedAt: string;
}

export interface TrainingPageItemResponse {
  id: string;
  name: string;
  description: string;
  durationMinutes: number;
  createdAt: string;
}

export interface TrainingCreditsSummaryResponse {
  remainingTrainings: number;
  expiresAt: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class TrainingService {
  private readonly http = inject(HttpClient);

  getTraining(tenantId: string, trainingId: string): Observable<TrainingDetailsResponse> {
    return this.http.get<TrainingDetailsResponse>(`/api/tenants/${tenantId}/trainings/${trainingId}`);
  }

  getTrainings(tenantId: string, page: number = 0, size: number = 10): Observable<Page<TrainingPageItemResponse>> {
    return this.http.get<Page<TrainingPageItemResponse>>(`/api/tenants/${tenantId}/trainings`, {
      params: {
        page: page.toString(),
        size: size.toString()
      }
    });
  }

  getAllTrainings(tenantId: string): Observable<TrainingPageItemResponse[]> {
    return this.http.get<Page<TrainingPageItemResponse>>(`/api/tenants/${tenantId}/trainings`, {
      params: {
        page: '0',
        size: '1000'  // Large enough to get all trainings
      }
    }).pipe(
      map(response => response.content)
    );
  }

  getTrainingCreditsSummary(tenantId: string, clientId: string, trainingId: string): Observable<TrainingCreditsSummaryResponse> {
    return this.http.get<TrainingCreditsSummaryResponse>(
      `/api/tenants/${tenantId}/clients/${clientId}/trainings/${trainingId}/credits/summary`
    );
  }
}
