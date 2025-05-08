import { Component, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-breadcrumb',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    TranslateModule
  ],
  template: `
    <div class="breadcrumb-container">
      <button mat-button class="breadcrumb-item" (click)="navigateToDashboard()">
        <mat-icon>dashboard</mat-icon>
        <span>{{ 'breadcrumb.dashboard' | translate }}</span>
      </button>

      <mat-icon class="separator">chevron_right</mat-icon>

      <button 
        *ngIf="showLocationsLink" 
        mat-button 
        class="breadcrumb-item" 
        (click)="navigateToLocations()"
      >
        <mat-icon>location_on</mat-icon>
        <span>{{ 'breadcrumb.locations' | translate }}</span>
      </button>

      <ng-container *ngIf="showLocationsLink">
        <mat-icon class="separator">chevron_right</mat-icon>
      </ng-container>

      <div *ngIf="locationName" class="breadcrumb-item current">
        <mat-icon>schedule</mat-icon>
        <span>{{ locationName }}</span>
      </div>
    </div>
  `,
  styles: [`
    .breadcrumb-container {
      display: flex;
      align-items: center;
      padding: 16px 24px;
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      margin-bottom: 24px;
      flex-wrap: wrap;
      gap: 8px;

      @media (max-width: 768px) {
        padding: 12px 16px;
        margin-bottom: 16px;
      }
    }

    .breadcrumb-item {
      display: flex;
      align-items: center;
      gap: 8px;
      color: #2196f3;
      font-size: 14px;
      line-height: 1;
      padding: 8px;
      border-radius: 4px;
      transition: background-color 0.2s;

      &:hover {
        background-color: rgba(33, 150, 243, 0.1);
      }

      mat-icon {
        font-size: 18px;
        width: 18px;
        height: 18px;
      }

      @media (max-width: 768px) {
        font-size: 13px;
        padding: 6px;

        mat-icon {
          font-size: 16px;
          width: 16px;
          height: 16px;
        }
      }

      &.current {
        color: #6c757d;
        cursor: default;
        pointer-events: none;

        &:hover {
          background-color: transparent;
        }
      }
    }

    .separator {
      color: #adb5bd;
      font-size: 18px;
      width: 18px;
      height: 18px;
      margin: 0 4px;

      @media (max-width: 768px) {
        font-size: 16px;
        width: 16px;
        height: 16px;
      }
    }
  `]
})
export class BreadcrumbComponent {
  private router = inject(Router);

  @Input() showLocationsLink = true;
  @Input() locationName: string | null = null;
  @Input() tenantId: string = '';

  navigateToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }

  navigateToLocations(): void {
    this.router.navigate(['/tenant', this.tenantId, 'locations']);
  }
} 