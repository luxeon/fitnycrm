import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { firstValueFrom } from 'rxjs';
import { TrainingService } from '../../core/services/training.service';

@Component({
  selector: 'app-create-workout',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslateModule
  ],
  template: `
    <div class="create-workout-container">
      <div class="create-workout-card">
        <h2>{{ 'training.create.title' | translate }}</h2>
        <p class="subtitle">{{ 'training.create.subtitle' | translate }}</p>

        <div *ngIf="errorMessage" class="error-banner">
          {{ errorMessage }}
        </div>

        <form [formGroup]="workoutForm" (ngSubmit)="onSubmit()" class="workout-form">
          <div class="form-group">
            <label for="name">{{ 'training.form.name.label' | translate }}</label>
            <input
              id="name"
              type="text"
              formControlName="name"
              [class.error]="workoutForm.get('name')?.invalid && workoutForm.get('name')?.touched"
              [placeholder]="'training.form.name.placeholder' | translate">
            <div class="error-message" *ngIf="workoutForm.get('name')?.invalid && workoutForm.get('name')?.touched">
              {{ 'training.form.name.required' | translate }}
            </div>
          </div>

          <div class="form-group">
            <label for="description">{{ 'training.form.description.label' | translate }}</label>
            <textarea
              id="description"
              formControlName="description"
              [placeholder]="'training.form.description.placeholder' | translate"
              rows="3">
            </textarea>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label for="durationMinutes">{{ 'training.form.duration.label' | translate }}</label>
              <input
                id="durationMinutes"
                type="number"
                min="1"
                formControlName="durationMinutes"
                [class.error]="workoutForm.get('durationMinutes')?.invalid && workoutForm.get('durationMinutes')?.touched"
                [placeholder]="'training.form.duration.placeholder' | translate">
              <div class="error-message" *ngIf="workoutForm.get('durationMinutes')?.invalid && workoutForm.get('durationMinutes')?.touched">
                {{ 'training.form.duration.required' | translate }}
              </div>
            </div>

            <div class="form-group">
              <label for="clientCapacity">{{ 'training.form.capacity.label' | translate }}</label>
              <input
                id="clientCapacity"
                type="number"
                min="1"
                formControlName="clientCapacity"
                [class.error]="workoutForm.get('clientCapacity')?.invalid && workoutForm.get('clientCapacity')?.touched"
                [placeholder]="'training.form.capacity.placeholder' | translate">
              <div class="error-message" *ngIf="workoutForm.get('clientCapacity')?.invalid && workoutForm.get('clientCapacity')?.touched">
                {{ 'training.form.capacity.required' | translate }}
              </div>
            </div>
          </div>

          <div class="form-actions">
            <button type="button" class="cancel-button" (click)="onCancel()">
              {{ 'common.cancel' | translate }}
            </button>
            <button type="submit" [disabled]="workoutForm.invalid || isLoading">
              {{ isLoading ? ('common.saving' | translate) : ('common.save' | translate) }}
            </button>
          </div>
        </form>
      </div>
    </div>
  `,
  styles: [`
    .create-workout-container {
      min-height: 100vh;
      padding: 40px;
      background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .create-workout-card {
      background: white;
      padding: 40px;
      border-radius: 12px;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      width: 100%;
      max-width: 600px;

      h2 {
        color: #2c3e50;
        margin: 0 0 8px;
        font-size: 24px;
        text-align: center;
      }

      .subtitle {
        color: #7f8c8d;
        text-align: center;
        margin-bottom: 24px;
      }
    }

    .workout-form {
      .form-group {
        margin-bottom: 20px;
        width: 100%;

        label {
          display: block;
          margin-bottom: 8px;
          color: #2c3e50;
          font-weight: 500;
        }

        input, textarea {
          width: 100%;
          padding: 10px;
          border: 1px solid #ddd;
          border-radius: 6px;
          font-size: 16px;
          box-sizing: border-box;
          background-color: #fff;

          &.error {
            border-color: #e74c3c;
          }

          &:focus {
            outline: none;
            border-color: #3498db;
            box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.1);
          }

          &::placeholder {
            color: #95a5a6;
          }
        }

        textarea {
          resize: vertical;
          min-height: 100px;
        }

        input[type="number"] {
          -moz-appearance: textfield;
          &::-webkit-outer-spin-button,
          &::-webkit-inner-spin-button {
            -webkit-appearance: none;
            margin: 0;
          }
        }
      }

      .form-row {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 20px;
      }

      .error-message {
        color: #e74c3c;
        font-size: 14px;
        margin-top: 4px;
      }
    }

    .error-banner {
      background-color: #fde8e8;
      color: #e74c3c;
      padding: 12px;
      border-radius: 6px;
      margin-bottom: 20px;
      text-align: center;
    }

    .form-actions {
      display: flex;
      gap: 12px;
      margin-top: 24px;

      button {
        flex: 1;
        padding: 12px;
        border: none;
        border-radius: 6px;
        font-size: 16px;
        cursor: pointer;
        transition: background-color 0.2s;

        &[type="submit"] {
          background-color: #3498db;
          color: white;

          &:hover:not(:disabled) {
            background-color: #2980b9;
          }

          &:disabled {
            background-color: #bdc3c7;
            cursor: not-allowed;
          }
        }
      }

      .cancel-button {
        background-color: #e9ecef;
        color: #495057;

        &:hover {
          background-color: #dee2e6;
        }
      }
    }
  `]
})
export class CreateWorkoutComponent {
  private readonly trainingService = inject(TrainingService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly fb = inject(FormBuilder);

  workoutForm: FormGroup = this.fb.group({
    name: ['', [Validators.required]],
    description: [''],
    durationMinutes: ['', [Validators.required, Validators.min(1)]],
    clientCapacity: ['', [Validators.required, Validators.min(1)]]
  });

  errorMessage: string | null = null;
  isLoading = false;

  async onSubmit(): Promise<void> {
    if (this.workoutForm.valid) {
      this.isLoading = true;
      this.errorMessage = null;

      const { tenantId } = this.route.snapshot.params;

      if (!tenantId) {
        this.router.navigate(['/dashboard']);
        return;
      }

      try {
        await firstValueFrom(this.trainingService.createTraining(tenantId, this.workoutForm.value));
        this.router.navigate(['/dashboard'], { queryParams: { tab: 'workouts' } });
      } catch (error) {
        this.errorMessage = 'Failed to create workout';
        console.error('Failed to create workout:', error);
      } finally {
        this.isLoading = false;
      }
    }
  }

  onCancel(): void {
    this.router.navigate(['/dashboard'], { queryParams: { tab: 'workouts' } });
  }
}
