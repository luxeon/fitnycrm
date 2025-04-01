import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { firstValueFrom } from 'rxjs';
import { TrainerService } from '../../core/services/trainer.service';

@Component({
  selector: 'app-create-trainer',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslateModule
  ],
  template: `
    <div class="create-trainer-container">
      <div class="create-trainer-card">
        <h2>{{ 'trainer.create.title' | translate }}</h2>
        <p class="subtitle">{{ 'trainer.create.subtitle' | translate }}</p>

        <div *ngIf="errorMessage" class="error-banner">
          {{ errorMessage }}
        </div>

        <form [formGroup]="trainerForm" (ngSubmit)="onSubmit()" class="trainer-form">
          <div class="form-row">
            <div class="form-group">
              <label for="firstName">{{ 'trainer.form.firstName.label' | translate }}</label>
              <input
                id="firstName"
                type="text"
                formControlName="firstName"
                [class.error]="trainerForm.get('firstName')?.invalid && trainerForm.get('firstName')?.touched"
                [placeholder]="'trainer.form.firstName.placeholder' | translate">
              <div class="error-message" *ngIf="trainerForm.get('firstName')?.invalid && trainerForm.get('firstName')?.touched">
                {{ 'trainer.form.firstName.required' | translate }}
              </div>
            </div>

            <div class="form-group">
              <label for="lastName">{{ 'trainer.form.lastName.label' | translate }}</label>
              <input
                id="lastName"
                type="text"
                formControlName="lastName"
                [class.error]="trainerForm.get('lastName')?.invalid && trainerForm.get('lastName')?.touched"
                [placeholder]="'trainer.form.lastName.placeholder' | translate">
              <div class="error-message" *ngIf="trainerForm.get('lastName')?.invalid && trainerForm.get('lastName')?.touched">
                {{ 'trainer.form.lastName.required' | translate }}
              </div>
            </div>
          </div>

          <div class="form-group">
            <label for="email">{{ 'trainer.form.email.label' | translate }}</label>
            <input
              id="email"
              type="email"
              formControlName="email"
              [class.error]="trainerForm.get('email')?.invalid && trainerForm.get('email')?.touched"
              [placeholder]="'trainer.form.email.placeholder' | translate">
            <div class="error-message" *ngIf="trainerForm.get('email')?.invalid && trainerForm.get('email')?.touched">
              {{ 'trainer.form.email.required' | translate }}
            </div>
          </div>

          <div class="form-group">
            <label for="phoneNumber">{{ 'trainer.form.phoneNumber.label' | translate }}</label>
            <input
              id="phoneNumber"
              type="tel"
              formControlName="phoneNumber"
              [placeholder]="'trainer.form.phoneNumber.placeholder' | translate">
          </div>

          <div class="form-group">
            <label for="specialization">{{ 'trainer.form.specialization.label' | translate }}</label>
            <input
              id="specialization"
              type="text"
              formControlName="specialization"
              [placeholder]="'trainer.form.specialization.placeholder' | translate">
          </div>

          <div class="form-actions">
            <button type="button" class="cancel-button" (click)="onCancel()">
              {{ 'common.cancel' | translate }}
            </button>
            <button type="submit" [disabled]="trainerForm.invalid || isLoading">
              {{ isLoading ? ('common.saving' | translate) : ('common.save' | translate) }}
            </button>
          </div>
        </form>
      </div>
    </div>
  `,
  styles: [`
    .create-trainer-container {
      min-height: 100vh;
      padding: 40px;
      background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .create-trainer-card {
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

    .trainer-form {
      .form-group {
        margin-bottom: 20px;

        label {
          display: block;
          margin-bottom: 8px;
          color: #2c3e50;
          font-weight: 500;
        }

        input {
          width: 100%;
          padding: 10px;
          border: 1px solid #ddd;
          border-radius: 6px;
          font-size: 16px;

          &.error {
            border-color: #e74c3c;
          }

          &:focus {
            outline: none;
            border-color: #3498db;
          }
        }
      }

      .form-row {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 32px;
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
export class CreateTrainerComponent {
  private readonly trainerService = inject(TrainerService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly fb = inject(FormBuilder);

  trainerForm: FormGroup = this.fb.group({
    firstName: ['', [Validators.required]],
    lastName: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    phoneNumber: [''],
    specialization: ['']
  });

  errorMessage: string | null = null;
  isLoading = false;

  async onSubmit(): Promise<void> {
    if (this.trainerForm.valid) {
      this.isLoading = true;
      this.errorMessage = null;

      const tenantId = this.route.snapshot.queryParams['tenantId'];
      const locationId = this.route.snapshot.queryParams['locationId'];

      if (!tenantId || !locationId) {
        this.router.navigate(['/dashboard']);
        return;
      }

      try {
        await firstValueFrom(this.trainerService.createTrainer(tenantId, this.trainerForm.value));
        await this.router.navigate(['/club-details'], {
          queryParams: { tenantId, locationId, tab: 'trainers' }
        });
      } catch (error: any) {
        this.errorMessage = error.status === 401
          ? 'Authentication failed. Please try logging in again.'
          : error.status === 409
            ? 'A trainer with this email already exists.'
            : 'Failed to create trainer. Please try again.';
      } finally {
        this.isLoading = false;
      }
    }
  }

  onCancel(): void {
    const tenantId = this.route.snapshot.queryParams['tenantId'];
    const locationId = this.route.snapshot.queryParams['locationId'];
    
    this.router.navigate(['/club-details'], {
      queryParams: { tenantId, locationId, tab: 'trainers' }
    });
  }
} 