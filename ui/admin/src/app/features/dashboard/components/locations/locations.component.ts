import { Component, Input, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LocationService, LocationPageItemResponse } from '../../../../core/services/location.service';
import { Page } from '../../../../core/models/page.model';
import { TranslateModule } from '@ngx-translate/core';
import { Router } from '@angular/router';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';

@Component({
  selector: 'app-locations',
  standalone: true,
  imports: [CommonModule, TranslateModule, ConfirmationDialogComponent],
  template: `
    <div class="locations-section">
      <div class="section-header">
        <h3>{{ 'dashboard.locations.title' | translate }}</h3>
        <button class="add-location-btn" (click)="onAddLocation()">
          {{ 'dashboard.locations.addLocation' | translate }}
        </button>
      </div>
      
      <div class="locations-grid" *ngIf="!isLoading && locations?.content?.length">
        <div class="location-card" *ngFor="let location of locations?.content">
          <button class="delete-btn" (click)="onDeleteClick(location)">
            <span class="delete-icon">×</span>
          </button>
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

    <app-confirmation-dialog
      *ngIf="locationToDelete"
      [title]="'dashboard.locations.delete.title' | translate"
      [message]="'dashboard.locations.delete.message' | translate"
      [confirmText]="'dashboard.locations.delete.confirm' | translate"
      (confirm)="onDeleteConfirm()"
      (cancel)="onDeleteCancel()"
    ></app-confirmation-dialog>
  `,
  styles: [`
    .locations-section {
      margin-top: 2rem;
      padding: 1rem;
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

      .section-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 1rem;

        h3 {
          margin: 0;
          color: #2c3e50;
        }

        .add-location-btn {
          padding: 8px 16px;
          background: #3498db;
          color: white;
          border: none;
          border-radius: 4px;
          cursor: pointer;
          font-size: 14px;
          transition: background-color 0.2s;

          &:hover {
            background: #2980b9;
          }
        }
      }
    }

    .locations-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
      gap: 1rem;
      margin-bottom: 1rem;
    }

    .location-card {
      position: relative;
      padding: 1rem;
      background: #f8f9fa;
      border-radius: 6px;
      border: 1px solid #e9ecef;
      transition: transform 0.2s, box-shadow 0.2s;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      }

      .delete-btn {
        position: absolute;
        top: 8px;
        right: 8px;
        width: 24px;
        height: 24px;
        border-radius: 50%;
        background: #e74c3c;
        border: none;
        color: white;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        transition: background-color 0.2s;

        .delete-icon {
          font-size: 18px;
          line-height: 1;
        }

        &:hover {
          background: #c0392b;
        }
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
  private readonly router = inject(Router);

  locations: Page<LocationPageItemResponse> | null = null;
  isLoading = false;
  locationToDelete: LocationPageItemResponse | null = null;

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

  onAddLocation(): void {
    this.router.navigate(['/create-location'], { queryParams: { tenantId: this.tenantId } });
  }

  onDeleteClick(location: LocationPageItemResponse): void {
    this.locationToDelete = location;
  }

  onDeleteConfirm(): void {
    if (this.locationToDelete) {
      this.locationService.deleteLocation(this.tenantId, this.locationToDelete.id)
        .subscribe({
          next: () => {
            this.locationToDelete = null;
            this.loadPage(this.locations?.number || 0);
          }
        });
    }
  }

  onDeleteCancel(): void {
    this.locationToDelete = null;
  }
} 