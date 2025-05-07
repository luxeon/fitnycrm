import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { ScheduleListItemResponse } from '../../../core/services/schedule.service';
import { animate, style, transition, trigger } from '@angular/animations';

interface SchedulesByDay {
  [day: string]: ScheduleListItemResponse[];
}

@Component({
  selector: 'app-schedule-list',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  animations: [
    trigger('fadeInOut', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('300ms ease-out', style({ opacity: 1 }))
      ])
    ])
  ],
  template: `
    <div class="schedule-list" @fadeInOut>
      <div class="day-tabs" *ngIf="viewMode === 'daily'">
        @for (day of weekDays; track day) {
          <button
            class="day-tab"
            [class.active]="selectedDay === day"
            (click)="selectDay(day)">
            {{ day }}
          </button>
        }
      </div>

      <div class="weekly-view" *ngIf="viewMode === 'weekly'">
        <div class="day-columns">
          @for (day of weekDays; track day) {
            <div class="day-column">
              <div class="day-header">{{ day }}</div>
              <div class="day-schedules">
                @if (schedulesByDay[day].length) {
                  @for (schedule of schedulesByDay[day]; track schedule.id) {
                    <div class="schedule-card">
                      <div class="card-actions">
                        <button class="edit-btn" (click)="onEditClick(schedule)" aria-label="Edit">
                          <span class="edit-icon">✎</span>
                        </button>
                        <button class="delete-btn" (click)="onDeleteClick(schedule)" aria-label="Delete">
                          <span class="delete-icon">×</span>
                        </button>
                      </div>
                      <div class="schedule-info">
                        <div class="time-slot">
                          {{ schedule.startTime }} - {{ schedule.endTime }}
                        </div>
                        <div class="capacity">
                          {{ 'schedule.capacity' | translate }}: {{ schedule.clientCapacity }}
                        </div>
                      </div>
                    </div>
                  }
                } @else {
                  <div class="no-schedules">
                    {{ 'schedule.no_schedules' | translate }}
                  </div>
                }
              </div>
            </div>
          }
        </div>
      </div>

      <div class="daily-view" *ngIf="viewMode === 'daily'">
        <h3 class="day-title">{{ selectedDay }}</h3>
        <div class="day-schedules-list">
          @if (schedulesByDay[selectedDay].length) {
            @for (schedule of schedulesByDay[selectedDay]; track schedule.id) {
              <div class="schedule-card">
                <div class="card-actions">
                  <button class="edit-btn" (click)="onEditClick(schedule)">
                    <span class="edit-icon">✎</span>
                  </button>
                  <button class="delete-btn" (click)="onDeleteClick(schedule)">
                    <span class="delete-icon">×</span>
                  </button>
                </div>
                <div class="schedule-info">
                  <div class="time-slot">
                    {{ schedule.startTime }} - {{ schedule.endTime }}
                  </div>
                  <div class="capacity">
                    {{ 'schedule.capacity' | translate }}: {{ schedule.clientCapacity }}
                  </div>
                </div>
              </div>
            }
          } @else {
            <div class="no-schedules">
              {{ 'schedule.no_schedules' | translate }}
            </div>
          }
        </div>
      </div>
    </div>
  `,
  styles: [`
    .schedule-list {
      margin-top: 1rem;
    }

    .day-tabs {
      display: flex;
      overflow-x: auto;
      margin-bottom: 1rem;
      border-bottom: 1px solid #e9ecef;
    }

    .day-tab {
      padding: 0.75rem 1.5rem;
      background: transparent;
      border: none;
      border-bottom: 3px solid transparent;
      cursor: pointer;
      font-weight: 500;
      color: #6c757d;
      transition: all 0.2s;

      &:hover {
        color: #3498db;
      }

      &.active {
        color: #3498db;
        border-bottom-color: #3498db;
      }
    }

    .weekly-view {
      margin-bottom: 1.5rem;

      .day-column {
        min-width: 130px;
      }

      .day-schedules {
        padding: 0.5rem;
      }

      .schedule-card {
        padding-top: 1.5rem;
        min-height: 70px;

        .time-slot {
          font-size: 0.8rem;
          white-space: normal;
          line-height: 1.2;
        }

        .capacity {
          font-size: 0.75rem;
          margin-top: 3px;
        }
      }
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

    .daily-view {
      margin-bottom: 1.5rem;
    }

    .day-title {
      margin-top: 0;
      margin-bottom: 1rem;
      color: #2c3e50;
      font-size: 1.5rem;
    }

    .day-schedules-list {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
      gap: 1rem;
    }

    .schedule-card {
      position: relative;
      padding: 1.5rem 0.75rem 0.75rem;
      background: white;
      border-radius: 8px;
      border: 1px solid #e9ecef;
      margin-bottom: 0.75rem;
      transition: transform 0.2s, box-shadow 0.2s;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      }
    }

    .card-actions {
      position: absolute;
      top: 5px;
      right: 5px;
      display: flex;
      gap: 4px;
      z-index: 5;
    }

    .edit-btn, .delete-btn {
      width: 20px;
      height: 20px;
      border-radius: 50%;
      border: none;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      transition: background-color 0.2s;
      padding: 0;
    }

    .edit-btn {
      background: #3498db;
      color: white;

      .edit-icon {
        font-size: 12px;
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
        font-size: 14px;
        line-height: 1;
      }

      &:hover {
        background: #c0392b;
      }
    }

    .schedule-info {
      margin-top: 0.5rem;
    }

    .time-slot {
      font-weight: 500;
      margin-bottom: 0.25rem;
      color: #2c3e50;
      word-break: break-word;
      font-size: 0.85rem;
      line-height: 1.3;
    }

    .capacity {
      font-size: 0.8rem;
      color: #7f8c8d;
    }

    .no-schedules {
      padding: 1rem;
      text-align: center;
      color: #6c757d;
      font-style: italic;
    }

    // Specific styles for daily view (larger cards)
    .daily-view {
      .schedule-card {
        padding: 1.25rem 1rem 1rem;

        .time-slot {
          font-size: 1rem;
          margin-bottom: 0.5rem;
        }

        .capacity {
          font-size: 0.9rem;
        }
      }

      .card-actions {
        top: 10px;
        right: 10px;
        gap: 6px;
      }

      .edit-btn, .delete-btn {
        width: 24px;
        height: 24px;
      }

      .edit-icon {
        font-size: 14px;
      }

      .delete-icon {
        font-size: 16px;
      }
    }
  `]
})
export class ScheduleListComponent implements OnInit {
  @Input() schedules: ScheduleListItemResponse[] = [];
  @Input() viewMode: 'weekly' | 'daily' = 'weekly';
  @Output() scheduleDeleted = new EventEmitter<string>();
  @Output() scheduleEdit = new EventEmitter<ScheduleListItemResponse>();
  @Output() viewModeChange = new EventEmitter<'weekly' | 'daily'>();

  weekDays: string[] = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
  schedulesByDay: SchedulesByDay = {};
  selectedDay: string = 'Monday';

  ngOnInit(): void {
    this.organizeSchedulesByDay();
  }

  ngOnChanges(): void {
    this.organizeSchedulesByDay();
  }

  organizeSchedulesByDay(): void {
    // Initialize empty arrays for each day
    this.schedulesByDay = {};
    this.weekDays.forEach(day => {
      this.schedulesByDay[day] = [];
    });

    // Create a map for case-insensitive day matching
    const dayMap: { [key: string]: string } = {};
    this.weekDays.forEach(day => {
      dayMap[day.toLowerCase()] = day;
    });

    // Organize schedules by day
    if (this.schedules && this.schedules.length > 0) {
      this.schedules.forEach(schedule => {
        if (schedule.daysOfWeek && Array.isArray(schedule.daysOfWeek)) {
          schedule.daysOfWeek.forEach(day => {

            // Try to match the day (case-insensitive)
            const normalizedDay = day.toLowerCase();
            const matchedDay = dayMap[normalizedDay];

            if (matchedDay) {
              this.schedulesByDay[matchedDay].push(schedule);
            } else {
              // Try to match by first letter or partial match
              const possibleMatches = this.weekDays.filter(weekDay =>
                weekDay.toLowerCase().startsWith(normalizedDay) ||
                normalizedDay.startsWith(weekDay.toLowerCase())
              );

              if (possibleMatches.length > 0) {
                possibleMatches.forEach(matchedDay => {
                  this.schedulesByDay[matchedDay].push(schedule);
                });
              } else {
                console.log('No matching day found for:', day);
              }
            }
          });
        } else {
          console.log('Schedule has no daysOfWeek or it is not an array:', schedule);
        }
      });
    } else {
      console.log('No schedules to organize');
    }

    // Sort schedules by start time for each day
    Object.keys(this.schedulesByDay).forEach(day => {
      this.schedulesByDay[day].sort((a, b) => {
        return a.startTime.localeCompare(b.startTime);
      });
    });

  }

  selectDay(day: string): void {
    this.selectedDay = day;
  }

  setViewMode(mode: 'weekly' | 'daily'): void {
    this.viewMode = mode;
    this.viewModeChange.emit(mode);
  }

  onDeleteClick(schedule: ScheduleListItemResponse): void {
    this.scheduleDeleted.emit(schedule.id);
  }

  onEditClick(schedule: ScheduleListItemResponse): void {
    this.scheduleEdit.emit(schedule);
  }
}
