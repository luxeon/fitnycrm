import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { firstValueFrom } from 'rxjs';
import { TrainerService } from '../../core/services/trainer.service';

@Component({
  selector: 'app-edit-trainer',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslateModule
  ],
  template: `
    <div class="edit-trainer-container">
      <div class="edit-trainer-card">
        <h2>{{ 'trainer.edit.title' | translate }}</h2>
        <p class="subtitle">{{ 'trainer.edit.subtitle' | translate }}</p>

        <div *ngIf="errorMessage" class="error-banner">
          {{ errorMessage }}
        </div>

        <div class="loading" *ngIf="isLoading">
          {{ 'common.loading' | translate }}
        </div>

        <form *ngIf="!isLoading" [formGroup]="trainerForm" (ngSubmit)="onSubmit()" class="trainer-form">
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
                <span *ngIf="trainerForm.get('firstName')?.errors?.['required']">
                  {{ 'trainer.form.firstName.required' | translate }}
                </span>
                <span *ngIf="trainerForm.get('firstName')?.errors?.['minlength'] || trainerForm.get('firstName')?.errors?.['maxlength']">
                  {{ 'trainer.form.firstName.length' | translate }}
                </span>
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
                <span *ngIf="trainerForm.get('lastName')?.errors?.['required']">
                  {{ 'trainer.form.lastName.required' | translate }}
                </span>
                <span *ngIf="trainerForm.get('lastName')?.errors?.['minlength'] || trainerForm.get('lastName')?.errors?.['maxlength']">
                  {{ 'trainer.form.lastName.length' | translate }}
                </span>
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
              <span *ngIf="trainerForm.get('email')?.errors?.['required']">
                {{ 'trainer.form.email.required' | translate }}
              </span>
              <span *ngIf="trainerForm.get('email')?.errors?.['email']">
                {{ 'trainer.form.email.invalid' | translate }}
              </span>
            </div>
          </div>

          <div class="form-group">
            <label for="phoneNumber">{{ 'trainer.form.phoneNumber.label' | translate }}</label>
            <input
              id="phoneNumber"
              type="tel"
              formControlName="phoneNumber"
              [class.error]="trainerForm.get('phoneNumber')?.invalid && trainerForm.get('phoneNumber')?.touched"
              [placeholder]="'trainer.form.phoneNumber.placeholder' | translate">
            <div class="error-message" *ngIf="trainerForm.get('phoneNumber')?.invalid && trainerForm.get('phoneNumber')?.touched">
              <span *ngIf="trainerForm.get('phoneNumber')?.errors?.['required']">
                {{ 'trainer.form.phoneNumber.required' | translate }}
              </span>
              <span *ngIf="trainerForm.get('phoneNumber')?.errors?.['minlength'] || trainerForm.get('phoneNumber')?.errors?.['maxlength']">
                {{ 'trainer.form.phoneNumber.length' | translate }}
              </span>
            </div>
          </div>

          <div class="form-actions">
            <button type="button" class="cancel-button" (click)="onCancel()">
              {{ 'common.cancel' | translate }}
            </button>
            <button type="submit" [disabled]="trainerForm.invalid || isSaving">
              {{ isSaving ? ('common.saving' | translate) : ('common.save' | translate) }}
            </button>
          </div>
        </form>
      </div>
    </div>
  `,
  styles: [`
    .edit-trainer-container {
      min-height: 100vh;
      padding: 40px;
      background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .edit-trainer-card {
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
        width: 100%;

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

    .loading {
      text-align: center;
      padding: 1rem;
      color: #6c757d;
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
export class EditTrainerComponent implements OnInit {
  private readonly trainerService = inject(TrainerService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly fb = inject(FormBuilder);

  trainerForm: FormGroup = this.fb.group({
    firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
    lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
    email: ['', [Validators.required, Validators.email]],
    phoneNumber: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(15)]],
    specialization: ['']
  });

  errorMessage: string | null = null;
  isLoading = true;
  isSaving = false;
  locationId: string | null = null;

  ngOnInit(): void {
    const { tenantId, trainerId } = this.route.snapshot.params;
    this.locationId = this.route.snapshot.queryParams['locationId'];

    if (!tenantId || !trainerId) {
      this.router.navigate(['/dashboard']);
      return;
    }

    this.loadTrainer(tenantId, trainerId);
  }

  private async loadTrainer(tenantId: string, trainerId: string): Promise<void> {
    try {
      const trainer = await firstValueFrom(this.trainerService.getTrainer(tenantId, trainerId));
      this.trainerForm.patchValue(trainer);
    } catch (error) {
      this.errorMessage = 'Failed to load trainer details';
      console.error('Failed to load trainer:', error);
    } finally {
      this.isLoading = false;
    }
  }

  async onSubmit(): Promise<void> {
    if (this.trainerForm.valid) {
      this.isSaving = true;
      this.errorMessage = null;

      const { tenantId, trainerId } = this.route.snapshot.params;

      if (!tenantId || !trainerId) {
        this.router.navigate(['/dashboard']);
        return;
      }

      try {
        await firstValueFrom(this.trainerService.updateTrainer(tenantId, trainerId, this.trainerForm.value));
        if (this.locationId) {
          this.router.navigate([`/tenant/${tenantId}/location/${this.locationId}/details`], { queryParams: { tab: 'trainers' } });
        } else {
          this.router.navigate(['/dashboard'], { queryParams: { tab: 'trainers' } });
        }
      } catch (error: any) {
        this.errorMessage = error.status === 401
          ? 'Authentication failed. Please try logging in again.'
          : 'Failed to update trainer. Please try again.';
        console.error('Failed to update trainer:', error);
      } finally {
        this.isSaving = false;
      }
    }
  }

  onCancel(): void {
    const { tenantId } = this.route.snapshot.params;
    if (this.locationId) {
      this.router.navigate([`/tenant/${tenantId}/location/${this.locationId}/details`], { queryParams: { tab: 'trainers' } });
    } else {
      this.router.navigate(['/dashboard'], { queryParams: { tab: 'trainers' } });
    }
  }
} 