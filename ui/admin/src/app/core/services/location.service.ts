import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../models/page.model';

export interface LocationPageItemResponse {
  id: string;
  address: string;
  city: string;
  state: string;
  postalCode: string;
  country: string;
  timezone: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateLocationRequest {
  address: string;
  city: string;
  state: string;
  postalCode: string;
  country: string;
  timezone: string;
}

@Injectable({
  providedIn: 'root'
})
export class LocationService {
  private readonly http = inject(HttpClient);

  getLocations(tenantId: string, page = 0, size = 10): Observable<Page<LocationPageItemResponse>> {
    return this.http.get<Page<LocationPageItemResponse>>(`/api/tenants/${tenantId}/locations`, {
      params: { page, size }
    });
  }

  createLocation(tenantId: string, request: CreateLocationRequest): Observable<LocationPageItemResponse> {
    return this.http.post<LocationPageItemResponse>(`/api/tenants/${tenantId}/locations`, request);
  }
}
