import { Component, Input, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LocationService, LocationPageItemResponse } from '../../../../core/services/location.service';
import { Page } from '../../../../core/models/page.model';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { Router } from '@angular/router';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { animate, style, transition, trigger } from '@angular/animations';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { LocationDialogComponent } from './location-dialog.component';
import { catchError, finalize } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-locations',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MatDialogModule,
    MatSnackBarModule,
    MatTooltipModule,
    ConfirmationDialogComponent
  ],
  animations: [
    trigger('fadeInOut', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('300ms ease-out', style({ opacity: 1 }))
      ]),
      transition(':leave', [
        animate('300ms ease-in', style({ opacity: 0 }))
      ])
    ])
  ],
  template: `
    <div class="locations-section" @fadeInOut>
      <div class="action-header">
        <h3>{{ 'dashboard.locations.title' | translate }}</h3>
        <button class="action-button" (click)="onAddLocation()">
          {{ 'dashboard.locations.addLocation' | translate }}
        </button>
      </div>
      
      <div class="locations-grid" *ngIf="!isLoading && locations?.content?.length" @fadeInOut>
        <div class="location-card" *ngFor="let location of locations?.content" (click)="onLocationClick(location)">
          <div class="card-actions" (click)="$event.stopPropagation()">
            <button class="edit-btn" (click)="onEditClick(location)" matTooltip="{{ 'dashboard.locations.edit' | translate }}">
              <span class="edit-icon">✎</span>
            </button>
            <button class="delete-btn" (click)="onDeleteClick(location)" matTooltip="{{ 'dashboard.locations.delete.confirm' | translate }}">
              <span class="delete-icon">×</span>
            </button>
          </div>
          <div class="location-info">
            <div class="address">{{ location.address }}</div>
            <div class="city-state">{{ location.city }}, {{ location.state }} {{ location.postalCode }}</div>
            <div class="country">{{ location.country }}</div>
          </div>
        </div>
      </div>

      <div class="empty-state" *ngIf="!isLoading && !locations?.content?.length" @fadeInOut>
        {{ 'dashboard.locations.noLocations' | translate }}
      </div>

      <div class="loading" *ngIf="isLoading" @fadeInOut>
        {{ 'dashboard.locations.loading' | translate }}
      </div>

      <div class="pagination" *ngIf="locations && locations.totalPages > 1" @fadeInOut>
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
      padding: 0;
    }

    .action-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 24px;

      h3 {
        margin: 0;
        color: #2c3e50;
        font-size: 20px;
      }
    }

    .action-button {
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
      border-radius: 8px;
      border: 1px solid #e9ecef;
      transition: transform 0.2s, box-shadow 0.2s;
      cursor: pointer;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        background: #f1f3f5;
      }

      .card-actions {
        position: absolute;
        top: 8px;
        right: 8px;
        display: flex;
        gap: 8px;
      }

      .edit-btn, .delete-btn {
        width: 24px;
        height: 24px;
        border-radius: 50%;
        border: none;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        transition: background-color 0.2s;
      }

      .edit-btn {
        background: #3498db;
        color: white;

        .edit-icon {
          font-size: 14px;
          line-height: 1;
        }

        &:hover {
          background: #2980b9;
        }
      }

      .delete-btn {
        background: #e74c3c;
        color: white;

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

    .empty-state {
      text-align: center;
      padding: 48px;
      background: #f8f9fa;
      border-radius: 8px;
      color: #6c757d;
      font-size: 16px;
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
  private readonly dialog = inject(MatDialog);
  private readonly snackBar = inject(MatSnackBar);
  private readonly translate = inject(TranslateService);

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
    const dialogRef = this.dialog.open(LocationDialogComponent, {
      width: '500px',
      data: {}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.isLoading = true;
        this.locationService.createLocation(this.tenantId, result)
          .pipe(
            catchError(() => {
              this.snackBar.open(
                this.translate.instant('location.create.error'),
                this.translate.instant('common.close'),
                { duration: 3000 }
              );
              return of(null);
            }),
            finalize(() => this.isLoading = false)
          )
          .subscribe(location => {
            if (location) {
              this.snackBar.open(
                this.translate.instant('location.create.success'),
                this.translate.instant('common.close'),
                { duration: 3000 }
              );
              this.loadPage(this.locations?.number || 0);
            }
          });
      }
    });
  }

  onEditClick(location: LocationPageItemResponse): void {
    this.locationService.getLocation(this.tenantId, location.id).subscribe(locationDetails => {
      const dialogRef = this.dialog.open(LocationDialogComponent, {
        width: '500px',
        data: { location: locationDetails }
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.isLoading = true;
          this.locationService.updateLocation(this.tenantId, location.id, result)
            .pipe(
              catchError(() => {
                this.snackBar.open(
                  this.translate.instant('location.update.error'),
                  this.translate.instant('common.close'),
                  { duration: 3000 }
                );
                return of(null);
              }),
              finalize(() => this.isLoading = false)
            )
            .subscribe(updatedLocation => {
              if (updatedLocation) {
                this.snackBar.open(
                  this.translate.instant('location.update.success'),
                  this.translate.instant('common.close'),
                  { duration: 3000 }
                );
                this.loadPage(this.locations?.number || 0);
              }
            });
        }
      });
    });
  }

  onDeleteClick(location: LocationPageItemResponse): void {
    this.locationToDelete = location;
  }

  onDeleteCancel(): void {
    this.locationToDelete = null;
  }

  onDeleteConfirm(): void {
    if (this.locationToDelete) {
      this.locationService.deleteLocation(this.tenantId, this.locationToDelete.id)
        .pipe(
          catchError(() => {
            this.snackBar.open(
              this.translate.instant('location.delete.error'),
              this.translate.instant('common.close'),
              { duration: 3000 }
            );
            return of(null);
          }),
          finalize(() => {
            this.locationToDelete = null;
            this.isLoading = false;
          })
        )
        .subscribe(result => {
          if (result !== null) {
            this.snackBar.open(
              this.translate.instant('location.delete.success'),
              this.translate.instant('common.close'),
              { duration: 3000 }
            );
            this.loadPage(this.locations?.number || 0);
          }
        });
    }
  }

  onLocationClick(location: LocationPageItemResponse): void {
    this.router.navigate([`/tenant/${this.tenantId}/location/${location.id}`]);
  }
} 