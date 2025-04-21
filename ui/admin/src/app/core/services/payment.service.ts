import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaymentStatus } from '../models/payment-status.enum';

export interface PaymentPageItemResponse {
  id: string;
  status: PaymentStatus;
  trainingId: string;
  trainingsCount: number;
  validDays: number;
  price: number;
  currency: string;
  createdAt: string;
}

export interface PaymentFilterRequest {
  status?: string;
  trainingId?: string;
  createdAtFrom?: string;
  createdAtTo?: string;
}

export interface CreatePaymentRequest {
  trainingId: string;
  trainingsCount: number;
  validDays: number;
  price: number;
  currency: string;
}

export interface PaymentDetailsResponse extends PaymentPageItemResponse {}

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private http = inject(HttpClient);

  getClientPayments(tenantId: string, clientId: string, page: number = 0, size: number = 10, filter?: PaymentFilterRequest): Observable<any> {
    const params = {
      page: page.toString(),
      size: size.toString(),
      ...filter
    };

    return this.http.get<any>(`/api/tenants/${tenantId}/clients/${clientId}/payments`, { params });
  }

  createPayment(tenantId: string, clientId: string, request: CreatePaymentRequest): Observable<PaymentDetailsResponse> {
    return this.http.post<PaymentDetailsResponse>(`/api/tenants/${tenantId}/clients/${clientId}/payments`, request);
  }

  cancelPayment(tenantId: string, clientId: string, paymentId: string): Observable<any> {
    return this.http.post<any>(`/api/tenants/${tenantId}/clients/${clientId}/payments/${paymentId}/cancel`, {});
  }
} 