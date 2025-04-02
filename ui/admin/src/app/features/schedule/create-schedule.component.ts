import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { firstValueFrom } from 'rxjs';
import { ScheduleService, CreateScheduleRequest } from '../../core/services/schedule.service';
import { TrainingService, TrainingPageItemResponse } from '../../core/services/training.service';
import { TrainerService, TrainerPageItemResponse } from '../../core/services/trainer.service';

@Component({
  selector: 'app-create-schedule',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslateModule
  ],
  template: `
    <div class="create-schedule-container">
      <div class="create-schedule-content">
        <h2>{{ 'schedule.create.title' | translate }}</h2>
        <form [formGroup]="scheduleForm" (ngSubmit)="onSubmit()">
          <div class="form-group">
            <label for="trainingId">{{ 'schedule.form.workout' | translate }}</label>
            <select id="trainingId" formControlName="trainingId" class="form-control">
              <option value="">{{ 'schedule.form.selectWorkout' | translate }}</option>
              <option *ngFor="let workout of workouts" [value]="workout.id">
                {{ workout.name }}
              </option>
            </select>
            <div *ngIf="scheduleForm.get('trainingId')?.errors?.['required'] && scheduleForm.get('trainingId')?.touched" class="error-message">
              {{ 'schedule.form.workoutRequired' | translate }}
            </div>
          </div>

          <div class="form-group">
            <label for="defaultTrainerId">{{ 'schedule.form.trainer' | translate }}</label>
            <select id="defaultTrainerId" formControlName="defaultTrainerId" class="form-control">
              <option value="">{{ 'schedule.form.selectTrainer' | translate }}</option>
              <option *ngFor="let trainer of trainers" [value]="trainer.id">
                {{ trainer.firstName }} {{ trainer.lastName }}
              </option>
            </select>
            <div *ngIf="scheduleForm.get('defaultTrainerId')?.errors?.['required'] && scheduleForm.get('defaultTrainerId')?.touched" class="error-message">
              {{ 'schedule.form.trainerRequired' | translate }}
            </div>
          </div>

          <div class="form-group">
            <label>{{ 'schedule.form.daysOfWeek' | translate }}</label>
            <div class="days-of-week">
              <label *ngFor="let day of daysOfWeek" class="day-checkbox">
                <input type="checkbox" [value]="day" (change)="onDayChange($event)">
                {{ 'schedule.form.days.' + day.toLowerCase() | translate }}
              </label>
            </div>
            <div *ngIf="scheduleForm.get('days')?.errors?.['required'] && scheduleForm.get('days')?.touched" class="error-message">
              {{ 'schedule.form.daysRequired' | translate }}
            </div>
          </div>

          <div class="form-group">
            <label for="startTime">{{ 'schedule.form.startTime' | translate }}</label>
            <input id="startTime" type="time" formControlName="startTime" class="form-control">
            <div *ngIf="scheduleForm.get('startTime')?.errors?.['required'] && scheduleForm.get('startTime')?.touched" class="error-message">
              {{ 'schedule.form.startTimeRequired' | translate }}
            </div>
          </div>

          <div class="form-group">
            <label for="endTime">{{ 'schedule.form.endTime' | translate }}</label>
            <input id="endTime" type="time" formControlName="endTime" class="form-control">
            <div *ngIf="scheduleForm.get('endTime')?.errors?.['required'] && scheduleForm.get('endTime')?.touched" class="error-message">
              {{ 'schedule.form.endTimeRequired' | translate }}
            </div>
          </div>

          <div class="form-actions">
            <button type="button" class="btn btn-secondary" (click)="onCancel()">
              {{ 'common.cancel' | translate }}
            </button>
            <button type="submit" class="btn btn-primary" [disabled]="scheduleForm.invalid || !selectedDays.length || isLoading">
              {{ (isLoading ? 'common.saving' : 'common.save') | translate }}
            </button>
          </div>
        </form>
      </div>
    </div>
  `,
  styles: [`
    .create-schedule-container {
      min-height: 100vh;
      padding: 40px;
      background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    }

    .create-schedule-content {
      background: white;
      padding: 40px;
      border-radius: 12px;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      max-width: 600px;
      margin: 0 auto;

      h2 {
        color: #2c3e50;
        margin: 0 0 24px;
        font-size: 24px;
      }
    }

    .form-group {
      margin-bottom: 20px;

      label {
        display: block;
        margin-bottom: 8px;
        color: #2c3e50;
        font-weight: 500;
      }

      .form-control {
        width: 100%;
        padding: 8px 12px;
        border: 1px solid #dce4ec;
        border-radius: 4px;
        font-size: 16px;
        transition: border-color 0.2s;

        &:focus {
          outline: none;
          border-color: #3498db;
        }
      }

      .error-message {
        color: #e74c3c;
        font-size: 14px;
        margin-top: 4px;
      }
    }

    .days-of-week {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
      gap: 8px;
      margin-top: 8px;
    }

    .day-checkbox {
      display: flex;
      align-items: center;
      gap: 8px;
      cursor: pointer;
      user-select: none;
      padding: 4px;
      border-radius: 4px;
      transition: background-color 0.2s;

      &:hover {
        background-color: #f8f9fa;
      }

      input {
        margin: 0;
      }
    }

    .form-actions {
      display: flex;
      gap: 12px;
      justify-content: flex-end;
      margin-top: 24px;

      .btn {
        padding: 8px 16px;
        border: none;
        border-radius: 4px;
        font-size: 16px;
        cursor: pointer;
        transition: background-color 0.2s;

        &:disabled {
          opacity: 0.7;
          cursor: not-allowed;
        }
      }

      .btn-primary {
        background: #3498db;
        color: white;

        &:hover:not(:disabled) {
          background: #2980b9;
        }
      }

      .btn-secondary {
        background: #95a5a6;
        color: white;

        &:hover:not(:disabled) {
          background: #7f8c8d;
        }
      }
    }
  `]
})
export class CreateScheduleComponent {
  private readonly fb = inject(FormBuilder);
  private readonly scheduleService = inject(ScheduleService);
  private readonly trainingService = inject(TrainingService);
  private readonly trainerService = inject(TrainerService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  scheduleForm: FormGroup;
  isLoading = false;
  tenantId: string;
  locationId: string;
  workouts: TrainingPageItemResponse[] = [];
  trainers: TrainerPageItemResponse[] = [];
  selectedDays: string[] = [];
  daysOfWeek = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];

  constructor() {
    const params = this.route.snapshot.params;
    this.tenantId = params['tenantId'];
    this.locationId = params['locationId'];

    this.scheduleForm = this.fb.group({
      trainingId: ['', Validators.required],
      defaultTrainerId: ['', Validators.required],
      startTime: ['', Validators.required],
      endTime: ['', Validators.required],
      days: [[], Validators.required]
    });

    this.loadWorkouts();
    this.loadTrainers();
  }

  private async loadWorkouts(): Promise<void> {
    try {
      const response = await firstValueFrom(this.trainingService.getTrainings(this.tenantId));
      this.workouts = response.content;
    } catch (error) {
      // Handle error appropriately
    }
  }

  private async loadTrainers(): Promise<void> {
    try {
      const response = await firstValueFrom(this.trainerService.getTrainers(this.tenantId));
      this.trainers = response.content;
    } catch (error) {
      // Handle error appropriately
    }
  }

  onDayChange(event: Event): void {
    const checkbox = event.target as HTMLInputElement;
    const day = checkbox.value;

    if (checkbox.checked) {
      if (!this.selectedDays.includes(day)) {
        this.selectedDays.push(day);
        this.scheduleForm.patchValue({ days: this.selectedDays });
      }
    } else {
      const index = this.selectedDays.indexOf(day);
      if (index > -1) {
        this.selectedDays.splice(index, 1);
        this.scheduleForm.patchValue({ days: this.selectedDays });
      }
    }
  }

  async onSubmit(): Promise<void> {
    if (this.scheduleForm.valid && this.selectedDays.length > 0) {
      this.isLoading = true;
      try {
        const formValue = this.scheduleForm.value;
        const request: CreateScheduleRequest = {
          trainingId: formValue.trainingId,
          defaultTrainerId: formValue.defaultTrainerId,
          startTime: formValue.startTime,
          endTime: formValue.endTime,
          daysOfWeek: this.selectedDays
        };
        await firstValueFrom(this.scheduleService.createSchedule(this.tenantId, this.locationId, request));

        const returnUrl = history.state?.returnUrl || `/tenant/${this.tenantId}/location/${this.locationId}`;
        await this.router.navigate([returnUrl]);
      } finally {
        this.isLoading = false;
      }
    }
  }

  onCancel(): void {
    const returnUrl = history.state?.returnUrl || `/tenant/${this.tenantId}/location/${this.locationId}`;
    this.router.navigate([returnUrl]);
  }
}
