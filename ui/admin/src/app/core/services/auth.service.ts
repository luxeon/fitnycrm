import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginRequest, LoginResponse } from '../models/auth.model';
import { environment } from '../../../environments/environment';
import { JwtPayload, decodeJwtToken } from '../utils/jwt.utils';

export interface UserSignupRequest {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  password: string;
}

export interface UserDetailsResponse {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  roles: string[];
  tenantIds: string[];
  createdAt: string;
  updatedAt: string;
}

export interface RefreshTokenResponse {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/auth`;

  signup(request: UserSignupRequest): Observable<UserDetailsResponse> {
    return this.http.post<UserDetailsResponse>(`${this.apiUrl}/signup`, request);
  }

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, request);
  }

  refreshToken(): Observable<RefreshTokenResponse> {
    const refreshToken = localStorage.getItem('refreshToken');
    return this.http.post<RefreshTokenResponse>('/api/auth/refresh', { refreshToken });
  }

  updateTokens(response: RefreshTokenResponse): void {
    localStorage.setItem('accessToken', response.accessToken);
    localStorage.setItem('refreshToken', response.refreshToken);
  }

  logout(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  }

  isAuthenticated(): boolean {
    const token = this.getAccessToken();
    if (!token) return false;

    const payload = this.getDecodedToken();
    if (!payload) return false;

    // Check if token is expired
    const now = Date.now() / 1000;
    return payload.exp > now;
  }

  hasRole(role: string): boolean {
    const payload = this.getDecodedToken();
    if (!payload) return false;
    return payload.roles.some(r => r.authority === role);
  }

  hasTenant(tenantId: string): boolean {
    const payload = this.getDecodedToken();
    if (!payload) return false;
    return payload.tenantIds.includes(tenantId);
  }

  getCurrentUser(): UserDetailsResponse | null {
    const payload = this.getDecodedToken();
    if (!payload) return null;

    return {
      id: payload.sub,
      firstName: payload.firstName,
      lastName: payload.lastName,
      email: payload.email,
      roles: payload.roles.map(role => role.authority),
      tenantIds: payload.tenantIds,
      createdAt: new Date(payload.iat * 1000).toISOString(),
      updatedAt: new Date(payload.iat * 1000).toISOString()
    };
  }

  private getAccessToken(): string | null {
    return localStorage.getItem('accessToken');
  }

  private getDecodedToken(): JwtPayload | null {
    const token = this.getAccessToken();
    if (!token) return null;
    return decodeJwtToken(token);
  }
}
