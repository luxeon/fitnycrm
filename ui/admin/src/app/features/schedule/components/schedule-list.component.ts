import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { ScheduleListItemResponse } from '../../../core/services/schedule.service';
import { animate, style, transition, trigger } from '@angular/animations';
import { TrainingService } from '../../../core/services/training.service';
import { TrainerService } from '../../../core/services/trainer.service';
import { inject } from '@angular/core';
import { firstValueFrom } from 'rxjs';

interface SchedulesByDay {
  [day: string]: (ScheduleListItemResponse & {
    trainingName?: string;
    trainerName?: string;
  })[];
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
            {{ 'schedule.days.' + day.toLowerCase() | translate }}
          </button>
        }
      </div>

      <div class="weekly-view" *ngIf="viewMode === 'weekly'">
        <div class="day-columns">
          @for (day of weekDays; track day) {
            <div class="day-column">
              <div class="day-header">
                <span class="day-name">{{ 'schedule.days.' + day.toLowerCase() | translate }}</span>
                <span class="schedule-count" *ngIf="schedulesByDay[day].length">
                  {{ 'schedule.classes' | translate }} {{ schedulesByDay[day].length }}
                </span>
              </div>
              <div class="day-schedules">
                @if (schedulesByDay[day].length) {
                  @for (schedule of schedulesByDay[day]; track schedule.id) {
                    <div class="schedule-card" [class.has-trainer]="schedule.trainerName">
                      <div class="time-badge">
                        {{ schedule.startTime }} - {{ schedule.endTime }}
                      </div>
                      <div class="card-content">
                        <div class="training-info">
                          <h4 class="training-name" *ngIf="schedule.trainingName">
                            {{ schedule.trainingName }}
                          </h4>
                          <div class="trainer-info" *ngIf="schedule.trainerName">
                            <span class="trainer-icon">👤</span>
                            {{ schedule.trainerName }}
                          </div>
                          <div class="capacity-badge">
                            <span class="capacity-icon">👥</span>
                            {{ schedule.clientCapacity }}
                          </div>
                        </div>
                        <div class="card-actions">
                          <button class="action-btn edit-btn" (click)="onEditClick(schedule)" title="{{ 'common.edit' | translate }}">
                            <span class="material-icons">edit</span>
                          </button>
                          <button class="action-btn delete-btn" (click)="onDeleteClick(schedule)" title="{{ 'common.delete' | translate }}">
                            <span class="material-icons">delete</span>
                          </button>
                        </div>
                      </div>
                    </div>
                  }
                } @else {
                  <div class="no-schedules">
                    <span>{{ 'schedule.no_schedules' | translate }}</span>
                  </div>
                }
              </div>
            </div>
          }
        </div>
      </div>

      <div class="daily-view" *ngIf="viewMode === 'daily'">
        <div class="daily-header">
          <h3 class="day-title">{{ 'schedule.days.' + selectedDay.toLowerCase() | translate }}</h3>
          <span class="schedule-count" *ngIf="schedulesByDay[selectedDay].length">
            {{ 'schedule.classes' | translate }} {{ schedulesByDay[selectedDay].length }}
          </span>
        </div>
        <div class="daily-schedules">
          @if (schedulesByDay[selectedDay].length) {
            @for (schedule of schedulesByDay[selectedDay]; track schedule.id) {
              <div class="schedule-card" [class.has-trainer]="schedule.trainerName">
                <div class="time-badge">
                  {{ schedule.startTime }} - {{ schedule.endTime }}
                </div>
                <div class="card-content">
                  <div class="training-info">
                    <h4 class="training-name" *ngIf="schedule.trainingName">
                      {{ schedule.trainingName }}
                    </h4>
                    <div class="trainer-info" *ngIf="schedule.trainerName">
                      <span class="trainer-icon">👤</span>
                      {{ schedule.trainerName }}
                    </div>
                    <div class="capacity-badge">
                      <span class="capacity-icon">👥</span>
                      {{ schedule.clientCapacity }}
                    </div>
                  </div>
                  <div class="card-actions">
                    <button class="action-btn edit-btn" (click)="onEditClick(schedule)" title="{{ 'common.edit' | translate }}">
                      <span class="material-icons">edit</span>
                    </button>
                    <button class="action-btn delete-btn" (click)="onDeleteClick(schedule)" title="{{ 'common.delete' | translate }}">
                      <span class="material-icons">delete</span>
                    </button>
                  </div>
                </div>
              </div>
            }
          } @else {
            <div class="no-schedules">
              <span>{{ 'schedule.no_schedules' | translate }}</span>
            </div>
          }
        </div>
      </div>
    </div>
  `,
  styles: [`
    .schedule-list {
      margin: 0;
      max-width: 1400px;
      padding: 0;
    }

    .day-tabs {
      display: flex;
      justify-content: center;
      gap: 0.5rem;
      margin-bottom: 2rem;
      flex-wrap: wrap;
    }

    .day-tab {
      padding: 0.75rem 1.5rem;
      background: white;
      border: 2px solid #e9ecef;
      border-radius: 8px;
      cursor: pointer;
      font-weight: 500;
      color: #6c757d;
      transition: all 0.2s ease;

      &:hover {
        border-color: #3498db;
        color: #3498db;
      }

      &.active {
        background: #3498db;
        border-color: #3498db;
        color: white;
      }
    }

    .weekly-view {
      margin-bottom: 2rem;
      overflow-x: hidden;
    }

    .day-columns {
      display: grid;
      grid-template-columns: repeat(7, 1fr);
      gap: 0.5rem;

      @media (max-width: 1400px) {
        grid-template-columns: repeat(7, minmax(0, 1fr));
      }

      @media (max-width: 1200px) {
        grid-template-columns: repeat(4, minmax(0, 1fr));
      }

      @media (max-width: 768px) {
        grid-template-columns: repeat(2, minmax(0, 1fr));
      }

      @media (max-width: 480px) {
        grid-template-columns: 1fr;
      }
    }

    .day-column {
      background: white;
      border-radius: 12px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
      border: 1px solid #e9ecef;
      overflow: hidden;
      min-width: 0; /* Prevent overflow */
    }

    .day-header {
      padding: 0.75rem;
      background: #f8f9fa;
      border-bottom: 1px solid #e9ecef;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .day-name {
      font-weight: 600;
      color: #2c3e50;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .schedule-count {
      font-size: 0.75rem;
      color: #6c757d;
      background: #e9ecef;
      padding: 0.25rem 0.5rem;
      border-radius: 4px;
      white-space: nowrap;
      margin-left: 0.25rem;
      flex-shrink: 0;
    }

    .day-schedules {
      padding: 0.75rem;
      min-height: 150px;
      max-height: 500px;
      overflow-y: auto;
    }

    .schedule-card {
      background: white;
      border-radius: 8px;
      border: 1px solid #e9ecef;
      margin-bottom: 0.75rem;
      transition: all 0.2s ease;
      overflow: hidden;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      }

      &.has-trainer {
        border-left: 4px solid #3498db;
      }
    }

    .time-badge {
      background: #f8f9fa;
      padding: 0.5rem 0.75rem;
      font-weight: 500;
      color: #2c3e50;
      font-size: 0.8rem;
      border-bottom: 1px solid #e9ecef;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .card-content {
      padding: 0.75rem;
    }

    .training-info {
      margin-bottom: 0.5rem;
    }

    .training-name {
      margin: 0 0 0.5rem 0;
      font-size: 0.9rem;
      color: #2c3e50;
      font-weight: 600;
      word-break: break-word;
    }

    .trainer-info {
      display: flex;
      align-items: center;
      gap: 0.25rem;
      color: #6c757d;
      font-size: 0.8rem;
      margin-bottom: 0.5rem;
      word-break: break-word;
    }

    .capacity-badge {
      display: inline-flex;
      align-items: center;
      gap: 0.25rem;
      background: #e9ecef;
      padding: 0.25rem 0.5rem;
      border-radius: 4px;
      font-size: 0.75rem;
      color: #6c757d;
    }

    .card-actions {
      display: flex;
      gap: 0.5rem;
      margin-top: 0.75rem;
      padding-top: 0.5rem;
      border-top: 1px solid #e9ecef;
    }

    .action-btn {
      padding: 0.4rem;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.2s ease;
      background: transparent;

      &.edit-btn {
        color: #3498db;

        &:hover {
          background: rgba(52, 152, 219, 0.1);
        }
      }

      &.delete-btn {
        color: #e74c3c;

        &:hover {
          background: rgba(231, 76, 60, 0.1);
        }
      }

      .material-icons {
        font-size: 16px;
      }
    }

    .daily-view {
      max-width: 800px;
      margin: 0 auto;
    }

    .daily-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 2rem;
    }

    .day-title {
      margin: 0;
      font-size: 1.5rem;
      color: #2c3e50;
      font-weight: 600;
    }

    .daily-schedules {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 1rem;
    }

    .no-schedules {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      gap: 1rem;
      padding: 2rem;
      color: #6c757d;
      text-align: center;
      background: #f8f9fa;
      border-radius: 8px;
      min-height: 130px;
    }

    ::-webkit-scrollbar {
      width: 8px;
      height: 8px;
    }

    ::-webkit-scrollbar-track {
      background: #f1f1f1;
      border-radius: 4px;
    }

    ::-webkit-scrollbar-thumb {
      background: #c1c1c1;
      border-radius: 4px;

      &:hover {
        background: #a8a8a8;
      }
    }
  `]
})
export class ScheduleListComponent implements OnInit {
  @Input() schedules: ScheduleListItemResponse[] = [];
  @Input() viewMode: 'weekly' | 'daily' = 'weekly';
  @Input() tenantId!: string;
  @Output() editClick = new EventEmitter<ScheduleListItemResponse>();
  @Output() deleteClick = new EventEmitter<string>();

  private readonly trainingService = inject(TrainingService);
  private readonly trainerService = inject(TrainerService);

  weekDays: string[] = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
  schedulesByDay: SchedulesByDay = {};
  selectedDay: string = 'Monday';

  ngOnInit(): void {
    this.organizeSchedulesByDay();
  }

  ngOnChanges(): void {
    this.organizeSchedulesByDay();
  }

  private async organizeSchedulesByDay(): Promise<void> {
    // Initialize empty arrays for each day
    this.schedulesByDay = {};
    this.weekDays.forEach(day => {
      this.schedulesByDay[day] = [];
    });

    // Create a map for case-insensitive day matching
    const dayMap = new Map(this.weekDays.map(day => [day.toUpperCase(), day]));

    // Group schedules by day
    for (const schedule of this.schedules) {
      for (const day of schedule.daysOfWeek) {
        const normalizedDay = dayMap.get(day.toUpperCase());
        if (normalizedDay) {
          const enrichedSchedule = { ...schedule };
          this.schedulesByDay[normalizedDay].push(enrichedSchedule);
        }
      }
    }

    // Sort schedules by start time
    for (const day in this.schedulesByDay) {
      this.schedulesByDay[day].sort((a, b) => a.startTime.localeCompare(b.startTime));
    }

    if (this.schedules.length > 0) {
      // Collect unique training and trainer IDs
      const trainingIds = new Set(this.schedules.map(s => s.trainingId));
      const trainerIds = new Set(this.schedules.map(s => s.defaultTrainerId));

      try {
        // Fetch training and trainer details
        const [trainings, trainers] = await Promise.all([
          Promise.all(Array.from(trainingIds).map(id =>
            firstValueFrom(this.trainingService.getTraining(this.tenantId, id))
          )),
          Promise.all(Array.from(trainerIds).map(id =>
            firstValueFrom(this.trainerService.getTrainer(this.tenantId, id))
          ))
        ]);

        // Create lookup maps
        const trainingMap = new Map(trainings.map(t => [t!.id, t!.name]));
        const trainerMap = new Map(trainers.map(t => [t!.id, `${t!.firstName} ${t!.lastName}`]));

        // Enrich schedules with names
        for (const day in this.schedulesByDay) {
          this.schedulesByDay[day] = this.schedulesByDay[day].map(schedule => ({
            ...schedule,
            trainingName: trainingMap.get(schedule.trainingId),
            trainerName: trainerMap.get(schedule.defaultTrainerId)
          }));
        }
      } catch (error) {
        console.error('Error fetching training or trainer details:', error);
      }
    }
  }

  selectDay(day: string): void {
    this.selectedDay = day;
  }

  onEditClick(schedule: ScheduleListItemResponse): void {
    this.editClick.emit(schedule);
  }

  onDeleteClick(schedule: ScheduleListItemResponse): void {
    this.deleteClick.emit(schedule.id);
  }
}


