import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { ScheduleListItemResponse } from '../../../core/services/schedule.service';
import { animate, style, transition, trigger } from '@angular/animations';

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
      <div class="schedule-grid">
        @for (schedule of schedules; track schedule.id) {
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
              <div class="days">
                @for (day of schedule.daysOfWeek; track day) {
                  <span class="day-badge">{{ day }}</span>
                }
              </div>
            </div>
          </div>
        }
      </div>
    </div>
  `,
  styles: [`
    .schedule-list {
      margin-top: 1rem;
    }

    .schedule-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
      gap: 1rem;
    }

    .schedule-card {
      position: relative;
      padding: 1rem;
      background: #f8f9fa;
      border-radius: 8px;
      border: 1px solid #e9ecef;
      transition: transform 0.2s, box-shadow 0.2s;

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
    }

    .schedule-info {
      .time-slot {
        font-weight: 500;
        color: #2c3e50;
        margin-bottom: 0.5rem;
        font-size: 1.1rem;
      }

      .days {
        display: flex;
        flex-wrap: wrap;
        gap: 0.5rem;
      }

      .day-badge {
        background: #3498db;
        color: white;
        padding: 0.25rem 0.5rem;
        border-radius: 4px;
        font-size: 0.8rem;
      }
    }
  `]
})
export class ScheduleListComponent {
  @Input() schedules: ScheduleListItemResponse[] = [];
  @Output() scheduleDeleted = new EventEmitter<string>();
  @Output() scheduleEdit = new EventEmitter<ScheduleListItemResponse>();

  onDeleteClick(schedule: ScheduleListItemResponse): void {
    this.scheduleDeleted.emit(schedule.id);
  }

  onEditClick(schedule: ScheduleListItemResponse): void {
    this.scheduleEdit.emit(schedule);
  }
} 