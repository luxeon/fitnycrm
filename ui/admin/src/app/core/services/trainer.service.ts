import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface CreateTrainerRequest {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  specialization?: string;
}

export interface UpdateTrainerRequest {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  specialization?: string;
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

  getTrainers(tenantId: string, page: number = 0, size: number = 10): Observable<{
    content: TrainerPageItemResponse[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
    first: boolean;
    last: boolean;
  }> {
    return this.http.get<{
      content: TrainerPageItemResponse[];
      totalElements: number;
      totalPages: number;
      size: number;
      number: number;
      first: boolean;
      last: boolean;
    }>(`${this.apiUrl}/tenants/${tenantId}/trainers`, {
      params: {
        page: page.toString(),
        size: size.toString()
      }
    });
  }
} 