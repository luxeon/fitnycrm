import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CreateTenantRequest {
  name: string;
  description?: string;
  locale: string;
}

export interface TenantResponse {
  id: string;
  name: string;
  description?: string;
  createdAt: string;
  updatedAt: string;
}

export interface TenantListItemResponse {
  id: string;
  name: string;
  createdAt: string;
  updatedAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class TenantService {
  private readonly http = inject(HttpClient);

  createTenant(request: CreateTenantRequest): Observable<TenantResponse> {
    return this.http.post<TenantResponse>('/api/tenants', request);
  }

  getById(id: string): Observable<TenantResponse> {
    return this.http.get<TenantResponse>(`/api/tenants/${id}`);
  }

  getAllForAuthenticatedUser(): Observable<TenantListItemResponse[]> {
    return this.http.get<TenantListItemResponse[]>('/api/tenants');
  }
} 