import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { ScheduleService } from '../../core/services/schedule.service';
import { SchedulePageItemResponse, VisitResponse } from '../../core/models/schedule.model';
import { ActivatedRoute } from '@angular/router';
import { LocationService } from '../../core/services/location.service';
import { LocationPageItemResponse } from '../../core/models/location.model';
import { combineLatest, map } from 'rxjs';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { animate, style, transition, trigger } from '@angular/animations';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { VisitDialogComponent } from './visit-dialog.component';
import { VisitService } from '../../core/services/visit.service';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatBadgeModule } from '@angular/material/badge';
import { provideNativeDateAdapter } from '@angular/material/core';

@Component({
  selector: 'app-schedule-list',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MatButtonToggleModule,
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    MatBadgeModule
  ],
  providers: [
    provideNativeDateAdapter()
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
            <mat-button-toggle value="calendar">
              {{ 'schedules.calendar_view' | translate }}
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
                    <div class="schedule-card"
                         [class.has-visit]="hasVisit(schedule.id)"
                         (click)="openVisitDialog(schedule, day)">
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

                        <!-- Show booked dates for this schedule and day -->
                        @if (getBookedDates(schedule.id, day).length) {
                          <div class="visit-info">
                            <div class="visit-badge">
                              <mat-icon color="primary">event_available</mat-icon>
                              {{ 'schedules.booked' | translate }}
                            </div>
                            <div class="booked-dates">
                              @for (date of getBookedDates(schedule.id, day); track date) {
                                <div class="booked-date">{{ date | date:'MMM d, yyyy' }}</div>
                              }
                            </div>
                          </div>
                        }
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

      <!-- Calendar View -->
      <div class="calendar-view" *ngIf="!isLoading && schedules?.length && viewMode === 'calendar'" @fadeInOut>
        <!-- Calendar view implementation will be added in the next iteration -->
        <div class="coming-soon">
          {{ 'schedules.calendar_coming_soon' | translate }}
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
      
      @media (max-width: 768px) {
        padding: 16px 8px;
      }
    }

    .header-section {
      margin-bottom: 24px;

      @media (max-width: 768px) {
        margin-bottom: 16px;
      }
    }

    .location-header {
      margin-bottom: 24px;

      .schedules-title {
        margin: 0 0 16px;
        color: #2c3e50;
        font-size: 24px;
        font-weight: 500;

        @media (max-width: 768px) {
          font-size: 20px;
          margin-bottom: 12px;
        }
      }

      .location-info {
        background: #f8f9fa;
        padding: 16px;
        border-radius: 8px;

        @media (max-width: 768px) {
          padding: 12px;
        }

        .address {
          font-weight: 500;
          color: #2c3e50;
          margin-bottom: 4px;
          font-size: 16px;

          @media (max-width: 768px) {
            font-size: 14px;
          }
        }

        .city-state {
          color: #495057;
          font-size: 14px;

          @media (max-width: 768px) {
            font-size: 12px;
          }
        }
      }
    }

    .view-controls {
      margin-bottom: 24px;
      display: flex;
      justify-content: flex-start;
      overflow-x: auto;
      -webkit-overflow-scrolling: touch;

      @media (max-width: 768px) {
        margin-bottom: 16px;
        justify-content: center;
      }

      ::ng-deep {
        .mat-button-toggle-group {
          border: none;
          background: #f8f9fa;
          border-radius: 4px;
          overflow: hidden;
          white-space: nowrap;
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

            @media (max-width: 768px) {
              padding: 0 12px;
              font-size: 14px;
            }
          }

          &.mat-button-toggle-checked {
            background: #3498db;
            color: white;
          }
        }
      }
    }

    .weekly-view {
      .day-columns {
        display: grid;
        grid-template-columns: repeat(7, 1fr);
        gap: 16px;
        margin-bottom: 24px;
        overflow-x: auto;
        -webkit-overflow-scrolling: touch;
        padding-bottom: 8px;

        @media (max-width: 1024px) {
          grid-template-columns: repeat(4, 1fr);
        }

        @media (max-width: 768px) {
          grid-template-columns: repeat(2, 1fr);
          gap: 12px;
        }

        @media (max-width: 480px) {
          grid-template-columns: 1fr;
          gap: 8px;
        }

        .day-column {
          background: #f8f9fa;
          border-radius: 8px;
          padding: 16px;
          min-width: 0;

          @media (max-width: 768px) {
            padding: 12px;
          }

          .day-header {
            font-weight: 500;
            color: #2c3e50;
            margin-bottom: 16px;
            text-align: center;
            padding-bottom: 8px;
            border-bottom: 2px solid #e9ecef;

            @media (max-width: 768px) {
              margin-bottom: 12px;
              font-size: 14px;
            }
          }

          .day-schedules {
            display: flex;
            flex-direction: column;
            gap: 12px;

            @media (max-width: 768px) {
              gap: 8px;
            }
          }
        }
      }

      .schedule-card {
        background: white;
        border-radius: 8px;
        padding: 16px;
        box-shadow: 0 2px 4px rgba(0,0,0,0.05);
        cursor: pointer;
        transition: transform 0.2s, box-shadow 0.2s;
        word-break: break-word;

        @media (max-width: 768px) {
          padding: 12px;
        }

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }

        &.has-visit {
          border: 2px solid #2196f3;
        }

        .schedule-info {
          .time-slot {
            font-weight: 500;
            color: #2c3e50;
            margin-bottom: 8px;
            font-size: 14px;
            line-height: 1.3;

            @media (max-width: 768px) {
              font-size: 13px;
              margin-bottom: 6px;
            }
          }

          .workout-name {
            color: #2c3e50;
            margin-bottom: 4px;
            font-size: 14px;

            @media (max-width: 768px) {
              font-size: 13px;
            }
          }

          .trainer-name {
            color: #6c757d;
            font-size: 13px;
            margin-bottom: 4px;

            @media (max-width: 768px) {
              font-size: 12px;
            }
          }

          .capacity {
            color: #6c757d;
            font-size: 13px;

            @media (max-width: 768px) {
              font-size: 12px;
            }
          }
        }
      }

      .no-schedules {
        text-align: center;
        color: #6c757d;
        padding: 16px;
        background: white;
        border-radius: 8px;
        font-size: 14px;

        @media (max-width: 768px) {
          padding: 12px;
          font-size: 13px;
        }
      }
    }

    .visit-info {
      margin-top: 10px;
      border-top: 1px solid #eee;
      padding-top: 8px;

      @media (max-width: 768px) {
        margin-top: 8px;
        padding-top: 6px;
      }
    }

    .visit-badge {
      display: flex;
      align-items: center;
      gap: 4px;
      color: #2196f3;
      font-size: 13px;

      @media (max-width: 768px) {
        font-size: 12px;
      }

      mat-icon {
        font-size: 18px;
        width: 18px;
        height: 18px;

        @media (max-width: 768px) {
          font-size: 16px;
          width: 16px;
          height: 16px;
        }
      }
    }

    .booked-dates {
      margin-top: 4px;
      width: 100%;
      display: flex;
      flex-direction: column;
      gap: 4px;
    }

    .booked-date {
      font-size: 13px;
      color: #2196f3;
      background: #e3f2fd;
      padding: 4px 8px;
      border-radius: 4px;
      text-align: center;
      width: 100%;

      @media (max-width: 768px) {
        font-size: 12px;
        padding: 3px 6px;
      }
    }

    .empty-state {
      text-align: center;
      padding: 32px;
      background: #f8f9fa;
      border-radius: 8px;
      color: #6c757d;

      @media (max-width: 768px) {
        padding: 24px;
        font-size: 14px;
      }
    }

    .loading {
      text-align: center;
      padding: 32px;
      color: #6c757d;

      @media (max-width: 768px) {
        padding: 24px;
        font-size: 14px;
      }
    }
  `]
})
export class ScheduleListComponent implements OnInit {
  private scheduleService = inject(ScheduleService);
  private locationService = inject(LocationService);
  private visitService = inject(VisitService);
  private route = inject(ActivatedRoute);
  private dialog = inject(MatDialog);

  schedules: SchedulePageItemResponse[] | null = null;
  location: LocationPageItemResponse | null = null;
  visits: VisitResponse[] = [];
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

  private currentDay: string = '';

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.tenantId = params['tenantId'];
      this.locationId = params['locationId'];
      this.loadData();
    });
  }

  loadData(): void {
    this.isLoading = true;

    combineLatest([
      this.locationService.getLocations(this.tenantId).pipe(
        map(response => response.content.find(loc => loc.id === this.locationId) || null)
      ),
      this.scheduleService.getSchedules(this.tenantId, this.locationId),
      this.visitService.getUserVisits(this.tenantId, this.locationId)
    ]).subscribe({
      next: ([location, schedules, visits]) => {
        this.location = location;
        this.schedules = schedules || [];
        this.visits = visits;
        // Clear the cache when visits are updated
        this.bookedDatesCache = {};
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        // Handle error - you might want to show a snackbar/toast here
      }
    });
  }

  onViewModeChange(mode: 'weekly' | 'calendar'): void {
    this.viewMode = mode;
  }

  getDaySchedules(day: string): SchedulePageItemResponse[] {
    if (!this.schedules) return [];
    this.currentDay = day;
    return this.schedules
      .filter(schedule => schedule.daysOfWeek.includes(day.toUpperCase()))
      .sort((a, b) => {
        // First sort by start time
        const startTimeComparison = a.startTime.localeCompare(b.startTime);
        // If start times are equal, sort by end time
        if (startTimeComparison === 0) {
          return a.endTime.localeCompare(b.endTime);
        }
        return startTimeComparison;
      });
  }

  hasVisit(scheduleId: string): boolean {
    if (!this.visits || !this.schedules) return false;

    const dayName = this.currentDay.toUpperCase();

    return this.visits.some(visit => {
      if (visit.scheduleId !== scheduleId) return false;

      const visitDate = new Date(visit.date);
      const visitDayName = this.getDayName(visitDate.getDay());

      return visitDayName === dayName;
    });
  }

  // Cache for booked dates to prevent ExpressionChangedAfterItHasBeenCheckedError
  private bookedDatesCache: { [key: string]: Date[] } = {};

  getBookedDates(scheduleId: string, day: string): Date[] {
    // Create a cache key using scheduleId and day
    const cacheKey = `${scheduleId}_${day}`;

    // Return cached result if available
    if (this.bookedDatesCache[cacheKey]) {
      return this.bookedDatesCache[cacheKey];
    }

    if (!this.visits) {
      this.bookedDatesCache[cacheKey] = [];
      return [];
    }

    const dayName = day.toUpperCase();

    let dates = this.visits
      .filter(visit => {
        if (visit.scheduleId !== scheduleId) return false;

        const visitDate = new Date(visit.date);
        const visitDayName = this.getDayName(visitDate.getDay());

        return visitDayName === dayName;
      })
      .map(visit => new Date(visit.date))
      .sort((a, b) => a.getTime() - b.getTime());

    // Cache the result
    this.bookedDatesCache[cacheKey] = dates;

    return dates;
  }

  private getDayName(dayIndex: number): string {
    const days = ['SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'];
    return days[dayIndex];
  }

  getVisit(scheduleId: string): VisitResponse | undefined {
    return this.visits.find(visit =>
      visit.scheduleId === scheduleId && visit.status === 'CONFIRMED'
    );
  }

  getVisitsForSchedule(scheduleId: string): VisitResponse[] {
    if (!this.visits) return [];
    return this.visits.filter(visit => visit.scheduleId === scheduleId);
  }

  openVisitDialog(schedule: SchedulePageItemResponse, selectedDay: string): void {
    const dialogRef = this.dialog.open(VisitDialogComponent, {
      data: {
        schedule,
        visits: this.getBookedDates(schedule.id, selectedDay).map(date => {
          const visit = this.visits.find(v =>
            v.scheduleId === schedule.id &&
            new Date(v.date).getTime() === date.getTime()
          );
          return visit!;
        }),
        tenantId: this.tenantId,
        locationId: this.locationId,
        selectedDay
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result?.success) {
        // Refresh visits after successful booking/cancellation
        this.visitService.getUserVisits(this.tenantId, this.locationId).subscribe(
          visits => {
            this.visits = visits;
            // Clear the cache when visits are updated
            this.bookedDatesCache = {};
          }
        );
      }
    });
  }
}
