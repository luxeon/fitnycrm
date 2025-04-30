import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { TenantService, Tenant } from '../../core/services/tenant.service';

@Component({
  selector: 'app-tenant-list',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="tenant-list">
      <div class="tenant-grid">
        <div class="tenant-card" *ngFor="let tenant of tenants" (click)="onTenantClick(tenant)">
          <h3>{{ tenant.name }}</h3>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .tenant-list {
      padding: 24px;
    }

    .tenant-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
      gap: 20px;
    }

    .tenant-card {
      background: #ffffff;
      border-radius: 12px;
      padding: 20px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      cursor: pointer;
      transition: transform 0.2s, box-shadow 0.2s;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
      }

      h3 {
        margin: 0;
        color: #2c3e50;
        font-size: 18px;
        font-weight: 500;
      }
    }
  `]
})
export class TenantListComponent implements OnInit {
  private tenantService = inject(TenantService);
  private router = inject(Router);

  tenants: Tenant[] = [];

  ngOnInit(): void {
    this.tenantService.getTenants().subscribe(tenants => {
      this.tenants = tenants;
    });
  }

  onTenantClick(tenant: Tenant): void {
    this.router.navigate(['/tenant', tenant.id, 'locations']);
  }
} 