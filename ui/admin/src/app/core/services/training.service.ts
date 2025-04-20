import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface CreateTrainingRequest {
  name: string;
  description?: string;
  durationMinutes: number;
}

export interface UpdateTrainingRequest {
  name: string;
  description?: string;
  durationMinutes: number;
}

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

export interface UpdateTrainingTariffsRequest {
  tariffIds: string[];
}

interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class TrainingService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl;

  createTraining(tenantId: string, request: CreateTrainingRequest): Observable<TrainingDetailsResponse> {
    return this.http.post<TrainingDetailsResponse>(
      `${this.apiUrl}/tenants/${tenantId}/trainings`,
      request
    );
  }

  updateTraining(tenantId: string, trainingId: string, request: UpdateTrainingRequest): Observable<TrainingDetailsResponse> {
    return this.http.put<TrainingDetailsResponse>(
      `${this.apiUrl}/tenants/${tenantId}/trainings/${trainingId}`,
      request
    );
  }

  deleteTraining(tenantId: string, trainingId: string): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/tenants/${tenantId}/trainings/${trainingId}`
    );
  }

  getTraining(tenantId: string, trainingId: string): Observable<TrainingDetailsResponse> {
    return this.http.get<TrainingDetailsResponse>(
      `${this.apiUrl}/tenants/${tenantId}/trainings/${trainingId}`
    );
  }

  getTrainings(tenantId: string, page: number = 0, size: number = 10): Observable<PageResponse<TrainingPageItemResponse>> {
    return this.http.get<PageResponse<TrainingPageItemResponse>>(`${this.apiUrl}/tenants/${tenantId}/trainings`, {
      params: {
        page: page.toString(),
        size: size.toString()
      }
    });
  }

  getAllTrainings(tenantId: string): Observable<TrainingPageItemResponse[]> {
    return this.http.get<PageResponse<TrainingPageItemResponse>>(`${this.apiUrl}/tenants/${tenantId}/trainings`, {
      params: {
        page: '0',
        size: '1000'  // Large enough to get all trainings
      }
    }).pipe(
      map(response => response.content)
    );
  }

  getTrainingTariffs(tenantId: string, trainingId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/tenants/${tenantId}/trainings/${trainingId}/tariffs`);
  }

  updateTrainingTariffs(tenantId: string, trainingId: string, request: UpdateTrainingTariffsRequest): Observable<void> {
    return this.http.put<void>(
      `${this.apiUrl}/tenants/${tenantId}/trainings/${trainingId}/tariffs`,
      request
    );
  }

  findAll(tenantId: string): Observable<TrainingPageItemResponse[]> {
    return this.http.get<PageResponse<TrainingPageItemResponse>>(`${this.apiUrl}/tenants/${tenantId}/trainings`, {
      params: {
        page: '0',
        size: '1000'  // Large enough to get all trainings
      }
    }).pipe(
      map(response => response.content)
    );
  }
}
