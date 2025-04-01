import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { TrainingPageItemResponse } from '../../../core/services/training.service';

@Component({
  selector: 'app-workout-list',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  template: `
    <div class="workouts-grid">
      <div class="workout-card" *ngFor="let workout of workouts">
        <div class="workout-info">
          <h3>{{ workout.name }}</h3>
          <p *ngIf="workout.description" class="description">{{ workout.description }}</p>
          <div class="details">
            <span class="detail">
              <i class="duration-icon">⏱</i>
              {{ workout.durationMinutes }} min
            </span>
            <span class="detail">
              <i class="capacity-icon">👥</i>
              {{ workout.clientCapacity }} people
            </span>
          </div>
        </div>
      </div>
    </div>

    <div class="pagination" *ngIf="totalPages > 1">
      <button 
        [disabled]="currentPage === 0"
        (click)="onPageChange(currentPage - 1)">
        {{ 'common.previous' | translate }}
      </button>
      <span class="page-info">
        {{ 'common.page' | translate }} {{ currentPage + 1 }} {{ 'common.of' | translate }} {{ totalPages }}
      </span>
      <button 
        [disabled]="currentPage === totalPages - 1"
        (click)="onPageChange(currentPage + 1)">
        {{ 'common.next' | translate }}
      </button>
    </div>
  `,
  styles: [`
    .workouts-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
      gap: 1rem;
      margin-bottom: 1rem;
    }

    .workout-card {
      background: #f8f9fa;
      border-radius: 8px;
      border: 1px solid #e9ecef;
      padding: 1rem;
      transition: transform 0.2s, box-shadow 0.2s;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        background: #f1f3f5;
      }
    }

    .workout-info {
      h3 {
        color: #2c3e50;
        margin: 0 0 0.5rem;
        font-size: 1.1rem;
        font-weight: 600;
      }

      .description {
        color: #495057;
        font-size: 0.9rem;
        margin: 0 0 1rem;
        line-height: 1.4;
      }

      .details {
        display: flex;
        gap: 1rem;
        color: #6c757d;
        font-size: 0.9rem;

        .detail {
          display: flex;
          align-items: center;
          gap: 0.25rem;

          i {
            font-style: normal;
          }
        }
      }
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
export class WorkoutListComponent {
  @Input() workouts: TrainingPageItemResponse[] = [];
  @Input() currentPage = 0;
  @Input() totalPages = 0;
  @Output() pageChange = new EventEmitter<number>();

  onPageChange(page: number): void {
    this.pageChange.emit(page);
  }
} 