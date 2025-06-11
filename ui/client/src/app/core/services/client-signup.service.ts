import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SignupClientRequest {
  firstName: string;
  lastName: string;
  phoneNumber: string;
  password: string;
  locale: string;
}

export interface ClientDetailsResponse {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  createdAt: string;
  updatedAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class ClientSignupService {
  private http = inject(HttpClient);

  /**
   * Register a new client using an invitation
   */
  signup(
    tenantId: string, 
    clientInvitationId: string, 
    request: SignupClientRequest
  ): Observable<ClientDetailsResponse> {
    return this.http.post<ClientDetailsResponse>(
      `/api/tenants/${tenantId}/clients/signup/${clientInvitationId}`, 
      request
    );
  }
} 