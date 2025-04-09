import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ClientInvitationService {
  private http = inject(HttpClient);

  /**
   * Join a tenant using an invitation for an already authenticated user
   */
  joinByInvitation(
    tenantId: string,
    clientInvitationId: string
  ): Observable<void> {
    return this.http.post<void>(
      `/api/tenants/${tenantId}/clients/join/${clientInvitationId}`,
      {}
    );
  }
}
