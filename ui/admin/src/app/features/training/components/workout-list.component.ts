import { Component, Input, Output, EventEmitter, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { TrainingService, TrainingPageItemResponse } from '../../../core/services/training.service';
import { ConfirmationDialogComponent } from '../../dashboard/components/confirmation-dialog/confirmation-dialog.component';
import { TrainingTariffsDialogComponent } from './training-tariffs-dialog.component';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-workout-list',
  standalone: true,
  imports: [CommonModule, TranslateModule, ConfirmationDialogComponent],
  template: `
    <div class="workouts-grid">
      <div class="workout-card" *ngFor="let workout of workouts">
        <div class="workout-info">
          <div class="card-actions">
            <button class="prices-btn" (click)="onManagePrices(workout)" title="{{ 'training.actions.managePrices' | translate }}">
              <span class="prices-icon">💰</span>
            </button>
            <button class="edit-btn" (click)="onEditClick(workout)" title="{{ 'common.edit' | translate }}">
              <span class="edit-icon">✎</span>
            </button>
            <button class="delete-btn" (click)="onDeleteClick(workout)" title="{{ 'common.delete' | translate }}">
              <span class="delete-icon">×</span>
            </button>
          </div>
          <h3>{{ workout.name }}</h3>
          <p class="description" *ngIf="workout.description">{{ workout.description }}</p>
          <div class="details">
            <span class="detail">
              <i class="duration-icon">⏱</i>
              {{ workout.durationMinutes }} {{ 'training.minutes' | translate }}
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

    <app-confirmation-dialog
      *ngIf="workoutToDelete"
      [title]="'training.delete.title' | translate"
      [message]="'training.delete.message' | translate"
      [confirmText]="'training.delete.confirm' | translate"
      (confirm)="onDeleteConfirm()"
      (cancel)="onDeleteCancel()"
    ></app-confirmation-dialog>
  `,
  styles: [`
    .workouts-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 1rem;
      margin-bottom: 1rem;
    }

    .workout-card {
      background: #f8f9fa;
      border-radius: 8px;
      border: 1px solid #e9ecef;
      padding: 1rem;
      transition: transform 0.2s, box-shadow 0.2s;
      position: relative;

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

    .card-actions {
      position: absolute;
      top: 8px;
      right: 8px;
      display: flex;
      gap: 8px;
    }

    .edit-btn, .delete-btn, .prices-btn {
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

    .prices-btn {
      background: #f1c40f;
      color: white;

      .prices-icon {
        font-size: 14px;
        line-height: 1;
      }

      &:hover {
        background: #f39c12;
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
  private readonly trainingService = inject(TrainingService);
  private readonly router = inject(Router);
  private readonly dialog = inject(MatDialog);
  private readonly snackBar = inject(MatSnackBar);
  private readonly translate = inject(TranslateService);

  @Input() workouts: TrainingPageItemResponse[] = [];
  @Input() currentPage = 0;
  @Input() totalPages = 0;
  @Input() tenantId = '';
  @Output() pageChange = new EventEmitter<number>();
  @Output() workoutDeleted = new EventEmitter<void>();

  workoutToDelete: TrainingPageItemResponse | null = null;

  onPageChange(page: number): void {
    this.pageChange.emit(page);
  }

  onEditClick(workout: TrainingPageItemResponse): void {
    this.router.navigate([`/tenant/${this.tenantId}/workout/${workout.id}/edit`]);
  }

  onDeleteClick(workout: TrainingPageItemResponse): void {
    this.workoutToDelete = workout;
  }

  onDeleteCancel(): void {
    this.workoutToDelete = null;
  }

  async onDeleteConfirm(): Promise<void> {
    if (this.workoutToDelete && this.tenantId) {
      try {
        await firstValueFrom(this.trainingService.deleteTraining(this.tenantId, this.workoutToDelete.id));
        this.workoutToDelete = null;
        this.workoutDeleted.emit();
        this.snackBar.open(
          this.translate.instant('training.delete.success'),
          this.translate.instant('common.close'),
          { duration: 3000 }
        );
      } catch (error) {
        this.snackBar.open(
          this.translate.instant('training.delete.error'),
          this.translate.instant('common.close'),
          { duration: 3000 }
        );
      }
    }
  }

  async onManagePrices(workout: TrainingPageItemResponse): Promise<void> {
    const dialogRef = this.dialog.open(TrainingTariffsDialogComponent, {
      width: '500px',
      data: {
        tenantId: this.tenantId,
        trainingId: workout.id,
        trainingName: workout.name
      }
    });

    const result = await firstValueFrom(dialogRef.afterClosed());
    if (result) {
      this.snackBar.open('Training prices updated successfully', 'Close', { duration: 3000 });
    }
  }
}
