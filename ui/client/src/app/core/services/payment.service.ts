import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../models/page.model';

export enum PaymentStatus {
  COMPLETED = 'COMPLETED',
  PENDING = 'PENDING',
  CANCELED = 'CANCELED',
  EXPIRED = 'EXPIRED'
}

export interface PaymentPageItemResponse {
  id: string;
  status: PaymentStatus;
  training: {
    id: string;
    name: string;
  };
  trainingsCount: number;
  validDays: number;
  price: number;
  currency: string;
  createdAt: string;
}

export interface PaymentFilterRequest {
  status?: PaymentStatus;
  trainingId?: string;
  fromDate?: string;
  toDate?: string;
}

export interface TrainingCreditsSummaryResponse {
  remainingTrainings: number;
  expiresAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private readonly http = inject(HttpClient);

  getPayments(tenantId: string, clientId: string, page: number = 0, size: number = 10, filter?: PaymentFilterRequest): Observable<Page<PaymentPageItemResponse>> {
    const params = {
      page: page.toString(),
      size: size.toString(),
      ...(filter || {})
    };

    return this.http.get<Page<PaymentPageItemResponse>>(`/api/tenants/${tenantId}/clients/${clientId}/payments`, { params });
  }

  getTrainingCreditsSummary(tenantId: string, clientId: string, trainingId: string): Observable<TrainingCreditsSummaryResponse> {
    return this.http.get<TrainingCreditsSummaryResponse>(
      `/api/tenants/${tenantId}/clients/${clientId}/trainings/${trainingId}/credits/summary`
    );
  }
} 