import { Component, Input, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { animate, style, transition, trigger } from '@angular/animations';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { catchError, finalize } from 'rxjs/operators';
import { of } from 'rxjs';
import { TrainingService, TrainingPageItemResponse } from '../../../../core/services/training.service';
import { WorkoutDialogComponent } from './workout-dialog.component';
import { WorkoutTariffsDialogComponent } from './workout-tariffs-dialog.component';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';

@Component({
  selector: 'app-workouts',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MatDialogModule,
    MatSnackBarModule,
    ConfirmationDialogComponent
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
    <div class="workouts-section" @fadeInOut>
      <div class="action-header">
        <h3>{{ 'dashboard.workouts.title' | translate }}</h3>
        <button class="action-button" (click)="onAddWorkout()">
          {{ 'dashboard.workouts.add' | translate }}
        </button>
      </div>

      <div class="loading" *ngIf="isLoading">
        {{ 'common.loading' | translate }}
      </div>

      <div class="empty-state" *ngIf="!isLoading && !workouts?.length">
        {{ 'dashboard.workouts.empty' | translate }}
      </div>

      <div class="workouts-grid" *ngIf="!isLoading && workouts?.length">
        <div class="workout-card" *ngFor="let workout of workouts">
          <div class="card-actions">
            <button class="tariff-btn" (click)="onAssignTariffs(workout)" title="{{ 'dashboard.workouts.tariffs.assign' | translate }}">
              <span class="tariff-icon">💰</span>
            </button>
            <button class="edit-btn" (click)="onEditWorkout(workout)" title="{{ 'common.edit' | translate }}">
              <span class="edit-icon">✎</span>
            </button>
            <button class="delete-btn" (click)="onDeleteClick(workout)" title="{{ 'common.delete' | translate }}">
              <span class="delete-icon">×</span>
            </button>
          </div>
          <div class="workout-info">
            <h4 class="name">{{ workout.name }}</h4>
            <div class="description" *ngIf="workout.description">{{ workout.description }}</div>
            <div class="details">
              <div class="duration">
                <span class="icon">⏱</span>
                {{ workout.durationMinutes }} {{ 'dashboard.workouts.minutes' | translate }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <app-confirmation-dialog
      *ngIf="workoutToDelete"
      [title]="'dashboard.workouts.delete.title' | translate"
      [message]="'dashboard.workouts.delete.message' | translate"
      [confirmText]="'dashboard.workouts.delete.confirm' | translate"
      (confirm)="onDeleteConfirm()"
      (cancel)="onDeleteCancel()"
    ></app-confirmation-dialog>
  `,
  styles: [`
    .workouts-section {
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

    .loading {
      text-align: center;
      padding: 48px;
      color: #6c757d;
      font-size: 16px;
    }

    .empty-state {
      text-align: center;
      padding: 48px;
      background: #f8f9fa;
      border-radius: 8px;
      color: #6c757d;
      font-size: 16px;
    }

    .workouts-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 1.5rem;
      margin-bottom: 1.5rem;
    }

    .workout-card {
      position: relative;
      padding: 1.5rem;
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
        top: 12px;
        right: 12px;
        display: flex;
        gap: 8px;
      }

      .edit-btn, .delete-btn, .tariff-btn {
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

      .tariff-btn {
        background: #f1c40f;
        color: white;

        .tariff-icon {
          font-size: 14px;
          line-height: 1;
        }

        &:hover {
          background: #f39c12;
        }
      }

      .workout-info {
        .name {
          font-size: 1.25rem;
          font-weight: 600;
          color: #2c3e50;
          margin-top: 0;
          margin-bottom: 0.5rem;
        }

        .description {
          color: #7f8c8d;
          font-size: 0.9rem;
          margin-bottom: 1rem;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
          text-overflow: ellipsis;
        }

        .details {
          display: flex;
          gap: 1rem;
          margin-top: 1rem;
          
          .duration {
            display: flex;
            align-items: center;
            color: #34495e;
            font-size: 0.875rem;
            
            .icon {
              margin-right: 0.375rem;
            }
          }
        }
      }
    }
  `]
})
export class WorkoutsComponent implements OnInit {
  @Input() tenantId!: string;
  
  private dialog = inject(MatDialog);
  private trainingService = inject(TrainingService);
  private snackBar = inject(MatSnackBar);
  private translate = inject(TranslateService);
  
  isLoading = false;
  workouts: TrainingPageItemResponse[] = [];
  workoutToDelete: TrainingPageItemResponse | null = null;
  
  ngOnInit(): void {
    this.loadWorkouts();
  }
  
  loadWorkouts(): void {
    this.isLoading = true;
    this.trainingService.getAllTrainings(this.tenantId)
      .pipe(
        finalize(() => this.isLoading = false)
      )
      .subscribe(
        workouts => this.workouts = workouts
      );
  }
  
  onAddWorkout(): void {
    const dialogRef = this.dialog.open(WorkoutDialogComponent, {
      width: '550px',
      data: {}
    });
    
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.isLoading = true;
        this.trainingService.createTraining(this.tenantId, result)
          .pipe(
            catchError(() => {
              this.snackBar.open(
                this.translate.instant('dashboard.workouts.create.error'),
                this.translate.instant('common.close'),
                { duration: 3000 }
              );
              return of(null);
            }),
            finalize(() => this.isLoading = false)
          )
          .subscribe(workout => {
            if (workout) {
              this.snackBar.open(
                this.translate.instant('dashboard.workouts.create.success'),
                this.translate.instant('common.close'),
                { duration: 3000 }
              );
              this.loadWorkouts();
            }
          });
      }
    });
  }
  
  onEditWorkout(workout: TrainingPageItemResponse): void {
    this.trainingService.getTraining(this.tenantId, workout.id).subscribe(workoutDetails => {
      const dialogRef = this.dialog.open(WorkoutDialogComponent, {
        width: '550px',
        data: { workout: workoutDetails }
      });
      
      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.isLoading = true;
          this.trainingService.updateTraining(this.tenantId, workout.id, result)
            .pipe(
              catchError(() => {
                this.snackBar.open(
                  this.translate.instant('dashboard.workouts.update.error'),
                  this.translate.instant('common.close'),
                  { duration: 3000 }
                );
                return of(null);
              }),
              finalize(() => this.isLoading = false)
            )
            .subscribe(updatedWorkout => {
              if (updatedWorkout) {
                this.snackBar.open(
                  this.translate.instant('dashboard.workouts.update.success'),
                  this.translate.instant('common.close'),
                  { duration: 3000 }
                );
                this.loadWorkouts();
              }
            });
        }
      });
    });
  }
  
  onDeleteClick(workout: TrainingPageItemResponse): void {
    this.workoutToDelete = workout;
  }
  
  onDeleteCancel(): void {
    this.workoutToDelete = null;
  }
  
  onDeleteConfirm(): void {
    if (this.workoutToDelete) {
      this.isLoading = true;
      this.trainingService.deleteTraining(this.tenantId, this.workoutToDelete.id)
        .pipe(
          catchError((error) => {
            this.snackBar.open(
              this.translate.instant('dashboard.workouts.delete.error'),
              this.translate.instant('common.close'),
              { duration: 3000 }
            );
            return of(null);
          }),
          finalize(() => {
            this.isLoading = false;
            this.workoutToDelete = null;
          })
        )
        .subscribe(result => {
          if (result !== null) {
            this.snackBar.open(
              this.translate.instant('dashboard.workouts.delete.success'),
              this.translate.instant('common.close'),
              { duration: 3000 }
            );
            this.loadWorkouts();
          }
        });
    }
  }

  onAssignTariffs(workout: TrainingPageItemResponse): void {
    const dialogRef = this.dialog.open(WorkoutTariffsDialogComponent, {
      width: '500px',
      data: {
        tenantId: this.tenantId,
        workoutId: workout.id,
        workoutName: workout.name
      }
    });
    
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.snackBar.open(
          this.translate.instant('dashboard.workouts.tariffs.success'),
          this.translate.instant('common.close'),
          { duration: 3000 }
        );
      }
    });
  }
} 