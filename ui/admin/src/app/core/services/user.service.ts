import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly http = inject(HttpClient);

  inviteUser(tenantId: string, email: string): Observable<void> {
    return this.http.post<void>(`/api/tenants/${tenantId}/clients/invite`, { email });
  }
}
