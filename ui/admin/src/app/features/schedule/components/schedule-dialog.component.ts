import { Component, Inject, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { TranslateModule } from '@ngx-translate/core';
import { firstValueFrom } from 'rxjs';
import { ScheduleDetailsResponse } from '../../../core/services/schedule.service';
import { TrainingService, TrainingPageItemResponse } from '../../../core/services/training.service';
import { TrainerService, TrainerPageItemResponse } from '../../../core/services/trainer.service';

export interface ScheduleDialogData {
  tenantId: string;
  locationId: string;
  schedule?: ScheduleDetailsResponse;
}

@Component({
  selector: 'app-schedule-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatCheckboxModule,
    TranslateModule
  ],
  template: `
    <h2 mat-dialog-title>{{ (data.schedule ? 'schedule.edit.title' : 'schedule.create.title') | translate }}</h2>
    <div class="loading" *ngIf="isLoading">
      {{ 'common.loading' | translate }}
    </div>
    <form [formGroup]="scheduleForm" (ngSubmit)="onSubmit()" *ngIf="!isLoading">
      <mat-dialog-content>
        <div class="form-container">
          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'schedule.form.workout' | translate }}</mat-label>
            <mat-select formControlName="trainingId" required>
              <mat-option value="">{{ 'schedule.form.selectWorkout' | translate }}</mat-option>
              <mat-option *ngFor="let workout of workouts" [value]="workout.id">
                {{ workout.name }}
              </mat-option>
            </mat-select>
            <mat-error *ngIf="scheduleForm.get('trainingId')?.hasError('required')">
              {{ 'schedule.form.workoutRequired' | translate }}
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'schedule.form.trainer' | translate }}</mat-label>
            <mat-select formControlName="defaultTrainerId" required>
              <mat-option value="">{{ 'schedule.form.selectTrainer' | translate }}</mat-option>
              <mat-option *ngFor="let trainer of trainers" [value]="trainer.id">
                {{ trainer.firstName }} {{ trainer.lastName }}
              </mat-option>
            </mat-select>
            <mat-error *ngIf="scheduleForm.get('defaultTrainerId')?.hasError('required')">
              {{ 'schedule.form.trainerRequired' | translate }}
            </mat-error>
          </mat-form-field>

          <div class="days-section">
            <p class="section-label">{{ 'schedule.form.daysOfWeek' | translate }}</p>
            <div class="days-of-week">
              <mat-checkbox 
                *ngFor="let day of daysOfWeek" 
                [checked]="selectedDays.includes(day)" 
                (change)="onDayChange($event, day)"
                class="day-checkbox">
                {{ 'schedule.form.days.' + day.toLowerCase() | translate }}
              </mat-checkbox>
            </div>
            <div class="error-message" *ngIf="!selectedDays.length && scheduleForm.get('days')?.touched">
              {{ 'schedule.form.daysRequired' | translate }}
            </div>
          </div>

          <div class="time-container">
            <mat-form-field appearance="outline">
              <mat-label>{{ 'schedule.form.startTime' | translate }}</mat-label>
              <input matInput type="time" formControlName="startTime" required>
              <mat-error *ngIf="scheduleForm.get('startTime')?.hasError('required')">
                {{ 'schedule.form.startTimeRequired' | translate }}
              </mat-error>
            </mat-form-field>

            <mat-form-field appearance="outline">
              <mat-label>{{ 'schedule.form.endTime' | translate }}</mat-label>
              <input matInput type="time" formControlName="endTime" required>
              <mat-error *ngIf="scheduleForm.get('endTime')?.hasError('required')">
                {{ 'schedule.form.endTimeRequired' | translate }}
              </mat-error>
            </mat-form-field>
          </div>

          <mat-form-field appearance="outline">
            <mat-label>{{ 'schedule.form.capacity' | translate }}</mat-label>
            <input matInput type="number" min="1" formControlName="clientCapacity" required>
            <mat-error *ngIf="scheduleForm.get('clientCapacity')?.hasError('required')">
              {{ 'schedule.form.capacityRequired' | translate }}
            </mat-error>
            <mat-error *ngIf="scheduleForm.get('clientCapacity')?.hasError('min')">
              {{ 'schedule.form.capacityMin' | translate }}
            </mat-error>
          </mat-form-field>
        </div>
      </mat-dialog-content>
      <mat-dialog-actions align="end">
        <button mat-button type="button" (click)="onCancel()">
          {{ 'common.cancel' | translate }}
        </button>
        <button mat-raised-button color="primary" type="submit" [disabled]="scheduleForm.invalid || !selectedDays.length || isSaving">
          {{ (isSaving ? 'common.saving' : 'common.save') | translate }}
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

    .full-width {
      width: 100%;
    }

    mat-dialog-content {
      min-width: 500px;
      max-height: 80vh;
    }

    .loading {
      text-align: center;
      padding: 1rem;
      color: #6c757d;
    }

    .days-section {
      margin-bottom: 1rem;
    }

    .section-label {
      margin-bottom: 0.5rem;
      color: rgba(0, 0, 0, 0.6);
      font-size: 14px;
    }

    .days-of-week {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
      gap: 0.5rem;
    }

    .day-checkbox {
      margin-right: 0;
    }

    .time-container {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 1rem;
    }

    .error-message {
      color: #f44336;
      font-size: 12px;
      margin-top: 0.25rem;
    }
  `]
})
export class ScheduleDialogComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly trainingService = inject(TrainingService);
  private readonly trainerService = inject(TrainerService);

  scheduleForm: FormGroup;
  isLoading = true;
  isSaving = false;
  workouts: TrainingPageItemResponse[] = [];
  trainers: TrainerPageItemResponse[] = [];
  selectedDays: string[] = [];
  daysOfWeek = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];

  constructor(
    private dialogRef: MatDialogRef<ScheduleDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ScheduleDialogData
  ) {
    this.scheduleForm = this.fb.group({
      trainingId: ['', Validators.required],
      defaultTrainerId: ['', Validators.required],
      startTime: ['', Validators.required],
      endTime: ['', Validators.required],
      days: [[], Validators.required],
      clientCapacity: [10, [Validators.required, Validators.min(1)]]
    });
  }

  async ngOnInit(): Promise<void> {
    try {
      await Promise.all([
        this.loadWorkouts(),
        this.loadTrainers()
      ]);

      if (this.data.schedule) {
        this.patchFormWithScheduleData();
      }
    } finally {
      this.isLoading = false;
    }
  }

  private async loadWorkouts(): Promise<void> {
    this.workouts = await firstValueFrom(this.trainingService.getAllTrainings(this.data.tenantId));
  }

  private async loadTrainers(): Promise<void> {
    this.trainers = await firstValueFrom(this.trainerService.getAllTrainers(this.data.tenantId));
  }

  private patchFormWithScheduleData(): void {
    if (this.data.schedule) {
      this.scheduleForm.patchValue({
        trainingId: this.data.schedule.trainingId,
        defaultTrainerId: this.data.schedule.defaultTrainerId,
        startTime: this.data.schedule.startTime,
        endTime: this.data.schedule.endTime,
        clientCapacity: this.data.schedule.clientCapacity
      });
      this.selectedDays = this.data.schedule.daysOfWeek;
    }
  }

  onDayChange(event: any, day: string): void {
    if (event.checked && !this.selectedDays.includes(day)) {
      this.selectedDays.push(day);
    } else if (!event.checked && this.selectedDays.includes(day)) {
      this.selectedDays = this.selectedDays.filter(d => d !== day);
    }

    this.scheduleForm.patchValue({ days: this.selectedDays });
  }

  onSubmit(): void {
    if (this.scheduleForm.valid && this.selectedDays.length > 0) {
      this.isSaving = true;
      
      const result = {
        ...this.scheduleForm.value,
        daysOfWeek: this.selectedDays
      };
      
      this.dialogRef.close(result);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
} 