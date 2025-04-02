import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { LocationService, LocationPageItemResponse } from '../../core/services/location.service';
import { TranslateModule } from '@ngx-translate/core';
import { firstValueFrom } from 'rxjs';
import { animate, style, transition, trigger } from '@angular/animations';
import { ScheduleService, ScheduleListItemResponse } from '../../core/services/schedule.service';
import { ScheduleListComponent } from '../schedule/components/schedule-list.component';
import { ConfirmationDialogComponent } from '../dashboard/components/confirmation-dialog/confirmation-dialog.component';

@Component({
  selector: 'app-club-details',
  standalone: true,
  imports: [CommonModule, TranslateModule, ScheduleListComponent, ConfirmationDialogComponent],
  animations: [
    trigger('fadeInOut', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('300ms ease-out', style({ opacity: 1 }))
      ])
    ])
  ],
  template: `
    <div class="club-details-container">
      <div class="club-details-content" @fadeInOut>
        <div class="header-section">
          <button class="back-button" (click)="onBack()">
            <span class="back-icon">←</span>
            {{ 'common.back' | translate }}
          </button>
          <h2>{{ 'location.details.club_location_title' | translate }}</h2>
        </div>

        <div class="address-section" *ngIf="location">
          <h3>{{ location.address }}</h3>
          <p>{{ location.city }}, {{ location.state }} {{ location.postalCode }}</p>
          <p>{{ location.country }}</p>
        </div>

        <div class="loading" *ngIf="isLoading">
          {{ 'common.loading' | translate }}
        </div>

        <div class="schedule-section" *ngIf="!isLoading && location">
          <div class="action-header">
            <h3>{{ 'location.details.schedule.title' | translate }}</h3>
            <button class="action-button" (click)="onAddSchedule()">
              {{ 'location.details.schedule.add' | translate }}
            </button>
          </div>
          <div class="loading" *ngIf="isLoadingSchedules">
            {{ 'common.loading' | translate }}
          </div>
          <div class="empty-state" *ngIf="!isLoadingSchedules && !schedules?.length">
            {{ 'location.details.schedule.empty' | translate }}
          </div>
          <app-schedule-list
            *ngIf="!isLoadingSchedules && schedules?.length"
            [schedules]="schedules"
            (scheduleDeleted)="onScheduleDelete($event)"
            (scheduleEdit)="onScheduleEdit($event)">
          </app-schedule-list>
        </div>
      </div>
    </div>

    <app-confirmation-dialog
      *ngIf="scheduleToDelete"
      [title]="'location.details.schedule.delete.title' | translate"
      [message]="'location.details.schedule.delete.message' | translate"
      [confirmText]="'location.details.schedule.delete.confirm' | translate"
      (confirm)="onScheduleDeleteConfirm()"
      (cancel)="onScheduleDeleteCancel()">
    </app-confirmation-dialog>
  `,
  styles: [`
    .club-details-container {
      min-height: 100vh;
      padding: 40px;
      background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    }

    .club-details-content {
      background: white;
      padding: 40px;
      border-radius: 12px;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      max-width: 1200px;
      margin: 0 auto;
    }

    .header-section {
      display: flex;
      align-items: center;
      margin-bottom: 32px;
      gap: 24px;
      border-bottom: 1px solid #eee;
      padding-bottom: 24px;

      h2 {
        color: #2c3e50;
        margin: 0;
        font-size: 24px;
        font-weight: 600;
      }
    }

    .back-button {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 8px 0;
      background: none;
      border: none;
      color: #3498db;
      cursor: pointer;
      font-size: 14px;
      transition: color 0.2s;

      .back-icon {
        font-size: 18px;
        line-height: 1;
      }

      &:hover {
        color: #2980b9;
      }
    }

    .address-section {
      margin-bottom: 32px;

      h3 {
        color: #2c3e50;
        margin: 0 0 8px;
        font-size: 20px;
      }

      p {
        color: #34495e;
        margin: 0 0 4px;
        font-size: 16px;
      }
    }

    .loading {
      text-align: center;
      padding: 1rem;
      color: #6c757d;
    }

    .schedule-section {
      margin-top: 24px;
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

    .empty-state {
      text-align: center;
      padding: 48px;
      background: #f8f9fa;
      border-radius: 8px;
      color: #6c757d;
      font-size: 16px;
    }
  `]
})
export class ClubDetailsComponent implements OnInit {
  private readonly locationService = inject(LocationService);
  private readonly scheduleService = inject(ScheduleService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  location: LocationPageItemResponse | null = null;
  isLoading = false;
  tenantId = '';
  locationId = '';

  // Schedules state
  schedules: ScheduleListItemResponse[] = [];
  isLoadingSchedules = false;
  scheduleToDelete: ScheduleListItemResponse | null = null;

  ngOnInit(): void {
    const params = this.route.snapshot.params;
    this.tenantId = params['tenantId'];
    this.locationId = params['locationId'];

    if (this.tenantId && this.locationId) {
      this.loadLocation(this.tenantId, this.locationId);
    } else {
      this.router.navigate(['/dashboard']);
    }
  }

  private async loadLocation(tenantId: string, locationId: string): Promise<void> {
    this.isLoading = true;
    try {
      this.location = await firstValueFrom(this.locationService.getLocation(tenantId, locationId));
      this.loadSchedules();
    } catch (error) {
      // Handle error appropriately
      this.router.navigate(['/dashboard']);
    } finally {
      this.isLoading = false;
    }
  }

  private async loadSchedules(): Promise<void> {
    this.isLoadingSchedules = true;
    try {
      this.schedules = await firstValueFrom(this.scheduleService.getSchedules(this.tenantId, this.locationId));
    } finally {
      this.isLoadingSchedules = false;
    }
  }

  onBack(): void {
    this.router.navigate(['/dashboard']).then(() => {
      // Restore scroll position after navigation
      const savedScrollPos = sessionStorage.getItem('dashboardScrollPos');
      if (savedScrollPos) {
        window.scrollTo(0, parseInt(savedScrollPos, 10));
        sessionStorage.removeItem('dashboardScrollPos'); // Clean up
      }
    });
  }

  onAddSchedule(): void {
    this.router.navigate([`/tenant/${this.tenantId}/location/${this.locationId}/schedule/create`], {
      state: { returnUrl: this.router.url }
    });
  }

  onScheduleDelete(scheduleId: string): void {
    this.scheduleToDelete = this.schedules.find(s => s.id === scheduleId) || null;
  }

  onScheduleEdit(schedule: ScheduleListItemResponse): void {
    this.router.navigate([`/tenant/${this.tenantId}/location/${this.locationId}/schedule/${schedule.id}/edit`], {
      state: { returnUrl: this.router.url }
    });
  }

  async onScheduleDeleteConfirm(): Promise<void> {
    if (this.scheduleToDelete) {
      try {
        await firstValueFrom(this.scheduleService.deleteSchedule(this.tenantId, this.locationId, this.scheduleToDelete.id));
        this.scheduleToDelete = null;
        this.loadSchedules();
      } catch (error) {
        // Handle error appropriately
      }
    }
  }

  onScheduleDeleteCancel(): void {
    this.scheduleToDelete = null;
  }
}
