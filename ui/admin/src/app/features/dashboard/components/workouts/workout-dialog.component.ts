import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { TranslateModule } from '@ngx-translate/core';
import { TrainingDetailsResponse } from '../../../../core/services/training.service';

export interface WorkoutDialogData {
  workout?: TrainingDetailsResponse;
}

@Component({
  selector: 'app-workout-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    TranslateModule
  ],
  template: `
    <h2 mat-dialog-title>{{ (data.workout ? 'dashboard.workouts.edit' : 'dashboard.workouts.add') | translate }}</h2>
    <form [formGroup]="form" (ngSubmit)="onSubmit()">
      <mat-dialog-content>
        <div class="form-container">
          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'dashboard.workouts.name' | translate }}</mat-label>
            <input matInput formControlName="name" required>
            <mat-error *ngIf="form.get('name')?.hasError('required')">
              {{ 'common.validation.required' | translate }}
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'dashboard.workouts.description' | translate }}</mat-label>
            <textarea matInput formControlName="description" rows="3" required></textarea>
            <mat-error *ngIf="form.get('description')?.hasError('required')">
              {{ 'common.validation.required' | translate }}
            </mat-error>
          </mat-form-field>

          <div class="form-row">
            <mat-form-field appearance="outline">
              <mat-label>{{ 'dashboard.workouts.duration' | translate }}</mat-label>
              <input matInput type="number" formControlName="durationMinutes" required min="1">
              <mat-error *ngIf="form.get('durationMinutes')?.hasError('required')">
                {{ 'common.validation.required' | translate }}
              </mat-error>
              <mat-error *ngIf="form.get('durationMinutes')?.hasError('min')">
                {{ 'common.validation.min' | translate:{ min: 1 } }}
              </mat-error>
            </mat-form-field>
          </div>
        </div>
      </mat-dialog-content>
      <mat-dialog-actions align="end">
        <button mat-button type="button" (click)="onCancel()">
          {{ 'common.cancel' | translate }}
        </button>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid">
          {{ (data.workout ? 'common.save' : 'common.create') | translate }}
        </button>
      </mat-dialog-actions>
    </form>
  `,
  styles: [`
    .form-container {
      display: flex;
      flex-direction: column;
      gap: 1rem;
      padding: 1rem 0;
    }

    .form-row {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 1rem;
    }

    .full-width {
      width: 100%;
    }

    mat-dialog-content {
      min-width: 500px;
    }
  `]
})
export class WorkoutDialogComponent {
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<WorkoutDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: WorkoutDialogData
  ) {
    this.form = this.fb.group({
      name: [data.workout?.name ?? '', Validators.required],
      description: [data.workout?.description ?? '', Validators.required],
      durationMinutes: [data.workout?.durationMinutes ?? 60, [Validators.required, Validators.min(1)]],
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.dialogRef.close(this.form.value);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
} 