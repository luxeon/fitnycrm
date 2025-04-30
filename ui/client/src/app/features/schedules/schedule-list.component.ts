import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { ScheduleService } from '../../core/services/schedule.service';
import { SchedulePageItemResponse } from '../../core/models/schedule.model';
import { ActivatedRoute } from '@angular/router';
import { LocationService } from '../../core/services/location.service';
import { LocationPageItemResponse } from '../../core/models/location.model';
import { combineLatest } from 'rxjs';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { animate, style, transition, trigger } from '@angular/animations';

@Component({
  selector: 'app-schedule-list',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MatButtonToggleModule
  ],
  animations: [
    trigger('fadeInOut', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('300ms ease-out', style({ opacity: 1 }))
      ])
    ])
  ],
  template: `
    <div class="schedules-container">
      <div class="header-section" @fadeInOut>
        <div class="location-header" *ngIf="location">
          <h2 class="schedules-title">{{ 'schedules.title' | translate }}</h2>
          <div class="location-info">
            <div class="address">{{ location.address }}</div>
            <div class="city-state">{{ location.city }}, {{ location.state }} {{ location.postalCode }}</div>
          </div>
        </div>

        <div class="view-controls">
          <mat-button-toggle-group
            [value]="viewMode"
            (valueChange)="onViewModeChange($event)">
            <mat-button-toggle value="weekly">
              {{ 'schedules.weekly_view' | translate }}
            </mat-button-toggle>
            <mat-button-toggle value="daily">
              {{ 'schedules.daily_view' | translate }}
            </mat-button-toggle>
          </mat-button-toggle-group>
        </div>
      </div>

      <!-- Weekly View -->
      <div class="weekly-view" *ngIf="!isLoading && schedules?.length && viewMode === 'weekly'" @fadeInOut>
        <div class="day-columns">
          @for (day of daysOfWeek; track day) {
            <div class="day-column">
              <div class="day-header">{{ day }}</div>
              <div class="day-schedules">
                @if (getDaySchedules(day).length) {
                  @for (schedule of getDaySchedules(day); track schedule.id) {
                    <div class="schedule-card">
                      <div class="schedule-info">
                        <div class="time-slot">
                          {{ schedule.startTime | slice:0:5 }} - {{ schedule.endTime | slice:0:5 }}
                        </div>
                        <div class="workout-name" *ngIf="schedule.trainingName">
                          {{ schedule.trainingName }}
                        </div>
                        <div class="trainer-name" *ngIf="schedule.defaultTrainerFullName">
                          {{ schedule.defaultTrainerFullName }}
                        </div>
                        <div class="capacity">
                          {{ 'schedules.capacity' | translate }}: {{ schedule.clientCapacity }}
                        </div>
                      </div>
                    </div>
                  }
                } @else {
                  <div class="no-schedules">
                    {{ 'schedules.no_schedules_for_day' | translate }}
                  </div>
                }
              </div>
            </div>
          }
        </div>
      </div>

      <!-- Daily View -->
      <div class="daily-view" *ngIf="!isLoading && schedules?.length && viewMode === 'daily'" @fadeInOut>
        <div class="day-section" *ngFor="let day of daysOfWeek">
          <h3 class="day-header">{{ day }}</h3>
          <div class="day-schedules">
            <div class="schedule-card" *ngFor="let schedule of getDaySchedules(day)">
              <div class="schedule-info">
                <div class="time">
                  {{ schedule.startTime | slice:0:5 }} - {{ schedule.endTime | slice:0:5 }}
                </div>
                <div class="workout-name" *ngIf="schedule.trainingName">
                  {{ schedule.trainingName }}
                </div>
                <div class="trainer-name" *ngIf="schedule.defaultTrainerFullName">
                  {{ schedule.defaultTrainerFullName }}
                </div>
                <div class="capacity">
                  {{ 'schedules.capacity' | translate }}: {{ schedule.clientCapacity }}
                </div>
              </div>
            </div>
            <div class="no-schedules" *ngIf="getDaySchedules(day).length === 0">
              {{ 'schedules.no_schedules_for_day' | translate }}
            </div>
          </div>
        </div>
      </div>

      <div class="empty-state" *ngIf="!isLoading && !schedules?.length" @fadeInOut>
        {{ 'schedules.noSchedules' | translate }}
      </div>

      <div class="loading" *ngIf="isLoading">
        {{ 'schedules.loading' | translate }}
      </div>
    </div>
  `,
  styles: [`
    .schedules-container {
      padding: 24px;
    }

    .header-section {
      margin-bottom: 24px;
    }

    .location-header {
      margin-bottom: 24px;

      .schedules-title {
        margin: 0 0 16px;
        color: #2c3e50;
        font-size: 24px;
        font-weight: 500;
      }

      .location-info {
        background: #f8f9fa;
        padding: 16px;
        border-radius: 8px;

        .address {
          font-weight: 500;
          color: #2c3e50;
          margin-bottom: 4px;
          font-size: 16px;
        }

        .city-state {
          color: #495057;
          font-size: 14px;
        }
      }
    }

    .view-controls {
      margin-bottom: 24px;
      display: flex;
      justify-content: flex-start;

      ::ng-deep {
        .mat-button-toggle-group {
          border: none;
          background: #f8f9fa;
          border-radius: 4px;
          overflow: hidden;
        }

        .mat-button-toggle {
          background: transparent;
          border: none;
          color: #6c757d;
          line-height: 32px;
          height: 32px;

          .mat-button-toggle-label-content {
            line-height: 32px;
            padding: 0 16px;
          }

          &.mat-button-toggle-checked {
            background: #3498db;
            color: white;
          }
        }
      }
    }

    .weekly-view {
      margin-bottom: 1.5rem;
    }

    .day-columns {
      display: grid;
      grid-template-columns: repeat(7, 1fr);
      gap: 0.5rem;
      overflow-x: auto;
    }

    .day-column {
      min-width: 130px;
      border-radius: 8px;
      background: #f8f9fa;
      border: 1px solid #e9ecef;
    }

    .day-header {
      padding: 0.75rem;
      background: #3498db;
      color: white;
      font-weight: 500;
      text-align: center;
      border-top-left-radius: 8px;
      border-top-right-radius: 8px;
    }

    .day-schedules {
      padding: 0.75rem;
      min-height: 100px;
      max-height: 400px;
      overflow-y: auto;
    }

    .schedule-card {
      background: #ffffff;
      border-radius: 8px;
      padding: 12px;
      margin-bottom: 8px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      transition: transform 0.2s, box-shadow 0.2s;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      }

      .schedule-info {
        .time-slot {
          font-weight: 600;
          color: #2c3e50;
          font-size: 0.8rem;
          white-space: normal;
          line-height: 1.2;
        }

        .workout-name {
          color: #2c3e50;
          font-weight: 500;
          margin-top: 4px;
          font-size: 0.85rem;
        }

        .trainer-name {
          color: #34495e;
          margin-top: 4px;
          font-size: 0.8rem;
        }

        .capacity {
          color: #7f8c8d;
          margin-top: 4px;
          font-size: 0.75rem;
        }
      }
    }

    .daily-view {
      display: flex;
      flex-direction: column;
      gap: 24px;

      .day-section {
        background: white;
        border-radius: 12px;
        overflow: hidden;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

        .day-header {
          background: #f8f9fa;
          margin: 0;
          padding: 16px;
          color: #2c3e50;
          font-size: 18px;
          font-weight: 500;
          border-bottom: 1px solid #e9ecef;
        }

        .day-schedules {
          padding: 16px;
          display: flex;
          flex-direction: column;
          gap: 16px;
        }
      }
    }

    .no-schedules {
      padding: 1rem;
      text-align: center;
      color: #6c757d;
      font-style: italic;
      background: #fff;
      border-radius: 8px;
    }

    .empty-state {
      text-align: center;
      padding: 48px;
      background: #f8f9fa;
      border-radius: 12px;
      color: #6c757d;
      font-size: 16px;
    }

    .loading {
      text-align: center;
      padding: 24px;
      color: #6c757d;
    }
  `]
})
export class ScheduleListComponent implements OnInit {
  private scheduleService = inject(ScheduleService);
  private locationService = inject(LocationService);
  private route = inject(ActivatedRoute);

  schedules: SchedulePageItemResponse[] | null = null;
  location: LocationPageItemResponse | null = null;
  isLoading = false;
  tenantId = '';
  locationId = '';
  viewMode = 'weekly';

  readonly daysOfWeek = [
    'MONDAY',
    'TUESDAY',
    'WEDNESDAY',
    'THURSDAY',
    'FRIDAY',
    'SATURDAY',
    'SUNDAY'
  ];

  ngOnInit(): void {
    this.tenantId = this.route.snapshot.params['tenantId'];
    this.locationId = this.route.snapshot.params['locationId'];

    this.loadData();
  }

  loadData(): void {
    this.isLoading = true;

    combineLatest([
      this.locationService.getLocations(this.tenantId),
      this.scheduleService.getSchedules(this.tenantId, this.locationId)
    ]).subscribe({
      next: ([locationsResponse, schedules]) => {
        this.location = locationsResponse.content.find(loc => loc.id === this.locationId) || null;
        this.schedules = schedules;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  onViewModeChange(mode: 'weekly' | 'daily'): void {
    this.viewMode = mode;
  }

  getDaySchedules(day: string): SchedulePageItemResponse[] {
    if (!this.schedules) return [];
    return this.schedules.filter(schedule => schedule.daysOfWeek.includes(day));
  }
}
