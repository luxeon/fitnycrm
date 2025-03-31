import { Component, Input, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LocationService, LocationPageItemResponse } from '../../../../core/services/location.service';
import { Page } from '../../../../core/models/page.model';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-locations',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  template: `
    <div class="locations-section">
      <h3>{{ 'dashboard.locations.title' | translate }}</h3>
      
      <div class="locations-grid" *ngIf="!isLoading && locations?.content?.length">
        <div class="location-card" *ngFor="let location of locations?.content">
          <div class="location-info">
            <div class="address">{{ location.address }}</div>
            <div class="city-state">{{ location.city }}, {{ location.state }} {{ location.postalCode }}</div>
            <div class="country">{{ location.country }}</div>
          </div>
        </div>
      </div>

      <div class="no-locations" *ngIf="!isLoading && !locations?.content?.length">
        {{ 'dashboard.locations.noLocations' | translate }}
      </div>

      <div class="loading" *ngIf="isLoading">
        {{ 'dashboard.locations.loading' | translate }}
      </div>

      <div class="pagination" *ngIf="locations && locations.totalPages > 1">
        <button 
          [disabled]="locations.first"
          (click)="loadPage(locations.number - 1)">
          {{ 'common.previous' | translate }}
        </button>
        <span class="page-info">
          {{ 'common.page' | translate }} {{ locations.number + 1 }} {{ 'common.of' | translate }} {{ locations.totalPages }}
        </span>
        <button 
          [disabled]="locations.last"
          (click)="loadPage(locations.number + 1)">
          {{ 'common.next' | translate }}
        </button>
      </div>
    </div>
  `,
  styles: [`
    .locations-section {
      margin-top: 2rem;
      padding: 1rem;
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

      h3 {
        margin: 0 0 1rem;
        color: #2c3e50;
      }
    }

    .locations-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
      gap: 1rem;
      margin-bottom: 1rem;
    }

    .location-card {
      padding: 1rem;
      background: #f8f9fa;
      border-radius: 6px;
      border: 1px solid #e9ecef;
      transition: transform 0.2s, box-shadow 0.2s;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      }

      .location-info {
        .address {
          font-weight: 500;
          color: #2c3e50;
          margin-bottom: 0.25rem;
        }

        .city-state {
          color: #495057;
          margin-bottom: 0.25rem;
        }

        .country {
          color: #6c757d;
          font-size: 0.9rem;
        }
      }
    }

    .no-locations {
      text-align: center;
      padding: 2rem;
      color: #6c757d;
      background: #f8f9fa;
      border-radius: 6px;
      border: 1px dashed #dee2e6;
    }

    .loading {
      text-align: center;
      padding: 1rem;
      color: #6c757d;
    }

    .pagination {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 1rem;
      margin-top: 1rem;

      button {
        padding: 0.5rem 1rem;
        background: #e9ecef;
        border: none;
        border-radius: 4px;
        color: #495057;
        cursor: pointer;
        transition: background-color 0.2s;

        &:hover:not(:disabled) {
          background: #dee2e6;
        }

        &:disabled {
          opacity: 0.5;
          cursor: not-allowed;
        }
      }

      .page-info {
        color: #6c757d;
      }
    }
  `]
})
export class LocationsComponent implements OnInit {
  @Input() tenantId!: string;

  private readonly locationService = inject(LocationService);

  locations: Page<LocationPageItemResponse> | null = null;
  isLoading = false;

  ngOnInit(): void {
    this.loadPage(0);
  }

  loadPage(page: number): void {
    this.isLoading = true;
    this.locationService.getLocations(this.tenantId, page)
      .subscribe({
        next: (response) => {
          this.locations = response;
          this.isLoading = false;
        },
        error: () => {
          this.isLoading = false;
        }
      });
  }
} 