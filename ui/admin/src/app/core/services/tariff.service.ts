import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface TariffResponse {
  id: string;
  name: string;
  trainingsCount: number;
  validDays: number;
  price: number;
  currency: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateTariffRequest {
  name: string;
  trainingsCount: number;
  validDays: number;
  price: number;
  currency: string;
}

@Injectable({
  providedIn: 'root'
})
export class TariffService {
  constructor(private http: HttpClient) {}

  findAll(tenantId: string): Observable<TariffResponse[]> {
    return this.http.get<TariffResponse[]>(`/api/tenants/${tenantId}/tariffs`);
  }

  create(tenantId: string, request: CreateTariffRequest): Observable<TariffResponse> {
    return this.http.post<TariffResponse>(`/api/tenants/${tenantId}/tariffs`, request);
  }

  update(tenantId: string, tariffId: string, request: CreateTariffRequest): Observable<TariffResponse> {
    return this.http.put<TariffResponse>(`/api/tenants/${tenantId}/tariffs/${tariffId}`, request);
  }

  delete(tenantId: string, tariffId: string): Observable<void> {
    return this.http.delete<void>(`/api/tenants/${tenantId}/tariffs/${tariffId}`);
  }
} 