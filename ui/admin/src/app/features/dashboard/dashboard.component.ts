import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService, UserDetailsResponse } from '../../core/services/auth.service';
import { TenantService, TenantResponse } from '../../core/services/tenant.service';
import { TranslateModule } from '@ngx-translate/core';
import { LocationsComponent } from './components/locations/locations.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    LocationsComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly tenantService = inject(TenantService);
  private readonly router = inject(Router);

  userDetails: UserDetailsResponse | null = null;
  tenantDetails: TenantResponse | null = null;

  ngOnInit(): void {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return;
    }

    this.userDetails = this.authService.getCurrentUser();
    
    this.tenantService.getAllForAuthenticatedUser()
      .subscribe(tenants => {
        if (tenants.length > 0) {
          this.tenantService.getById(tenants[0].id)
            .subscribe(tenant => this.tenantDetails = tenant);
        }
      });
  }
} 