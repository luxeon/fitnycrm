import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ClientPageItemResponse {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  createdAt: string;
  updatedAt: string;
}

export interface Page<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

export interface ClientDetailsResponse {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  createdAt: string;
  updatedAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly http = inject(HttpClient);

  inviteUser(tenantId: string, email: string): Observable<void> {
    return this.http.post<void>(`/api/tenants/${tenantId}/clients/invite`, { email });
  }

  getClients(tenantId: string, page: number = 0, size: number = 10): Observable<Page<ClientPageItemResponse>> {
    return this.http.get<Page<ClientPageItemResponse>>(`/api/tenants/${tenantId}/clients`, {
      params: { page: page.toString(), size: size.toString() }
    });
  }

  getClientById(tenantId: string, clientId: string): Observable<ClientDetailsResponse> {
    return this.http.get<ClientDetailsResponse>(`/api/tenants/${tenantId}/clients/${clientId}`);
  }
}
