import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { TranslateModule } from '@ngx-translate/core';
import { AuthService, UserClaims } from '../core/services/auth.service';
import { TenantService, Tenant } from '../core/services/tenant.service';
import { LocationService } from '../core/services/location.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatToolbarModule,
    MatIconModule,
    MatProgressSpinnerModule,
    TranslateModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  private authService = inject(AuthService);
  private tenantService = inject(TenantService);
  private locationService = inject(LocationService);
  private router = inject(Router);

  userClaims = signal<UserClaims | null>(null);
  tenants = signal<Tenant[]>([]);
  isLoading = signal(true);

  ngOnInit(): void {
    this.userClaims.set(this.authService.getUserClaims());
    this.loadTenants();
  }

  loadTenants(): void {
    this.isLoading.set(true);
    this.tenantService.getTenants().subscribe({
      next: (data) => {
        this.tenants.set(data);
        this.isLoading.set(false);
      },
      error: (error) => {
        console.error('Error loading tenants:', error);
        this.isLoading.set(false);
      }
    });
  }

  async onTenantClick(tenant: Tenant): Promise<void> {
    try {
      const locations = await firstValueFrom(this.locationService.getLocations(tenant.id));

      if (locations.content.length === 1) {
        this.router.navigate(['/tenant', tenant.id, 'locations', locations.content[0].id, 'schedules']);
      } else {
        this.router.navigate(['/tenant', tenant.id, 'locations']);
      }
    } catch (error) {
      // In case of error, fallback to locations list
      this.router.navigate(['/tenant', tenant.id, 'locations']);
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
