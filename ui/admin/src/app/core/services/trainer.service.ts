import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface CreateTrainerRequest {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  specialization?: string;
}

export interface UpdateTrainerRequest {
  /** First name of the trainer (2-50 characters) */
  firstName: string;
  /** Last name of the trainer (2-50 characters) */
  lastName: string;
  /** Email address of the trainer */
  email: string;
  /** Phone number of the trainer (10-15 characters) */
  phoneNumber: string;
}

export interface TrainerDetailsResponse {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  specialization?: string;
  createdAt: string;
  updatedAt: string;
}

export interface TrainerPageItemResponse {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  specialization?: string;
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
export class TrainerService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl;

  createTrainer(tenantId: string, request: CreateTrainerRequest): Observable<TrainerDetailsResponse> {
    return this.http.post<TrainerDetailsResponse>(
      `${this.apiUrl}/tenants/${tenantId}/trainers`,
      request
    );
  }

  updateTrainer(tenantId: string, trainerId: string, request: UpdateTrainerRequest): Observable<TrainerDetailsResponse> {
    return this.http.put<TrainerDetailsResponse>(
      `${this.apiUrl}/tenants/${tenantId}/trainers/${trainerId}`,
      request
    );
  }

  deleteTrainer(tenantId: string, trainerId: string): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/tenants/${tenantId}/trainers/${trainerId}`
    );
  }

  getTrainer(tenantId: string, trainerId: string): Observable<TrainerDetailsResponse> {
    return this.http.get<TrainerDetailsResponse>(
      `${this.apiUrl}/tenants/${tenantId}/trainers/${trainerId}`
    );
  }

  getTrainers(tenantId: string, page: number = 0, size: number = 10): Observable<PageResponse<TrainerPageItemResponse>> {
    return this.http.get<PageResponse<TrainerPageItemResponse>>(`${this.apiUrl}/tenants/${tenantId}/trainers`, {
      params: {
        page: page.toString(),
        size: size.toString()
      }
    });
  }

  getAllTrainers(tenantId: string): Observable<TrainerPageItemResponse[]> {
    return this.http.get<PageResponse<TrainerPageItemResponse>>(`${this.apiUrl}/tenants/${tenantId}/trainers`, {
      params: {
        page: '0',
        size: '1000'  // Large enough to get all trainers
      }
    }).pipe(
      map(response => response.content)
    );
  }
} 