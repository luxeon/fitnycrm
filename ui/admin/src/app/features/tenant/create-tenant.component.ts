import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { TenantService, CreateTenantRequest } from '../../core/services/tenant.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-create-tenant',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslateModule
  ],
  template: `
    <div class="create-tenant-container">
      <div class="create-tenant-card">
        <h2>Create Your Fitness Club</h2>
        <p class="subtitle">Set up your fitness club to get started</p>

        <div *ngIf="errorMessage" class="error-banner">
          {{ errorMessage }}
        </div>

        <form [formGroup]="tenantForm" (ngSubmit)="onSubmit()" class="tenant-form">
          <div class="form-group">
            <label for="name">Club Name</label>
            <input
              id="name"
              type="text"
              formControlName="name"
              [class.error]="tenantForm.get('name')?.invalid && tenantForm.get('name')?.touched"
              placeholder="Enter your club name">
            <div class="error-message" *ngIf="tenantForm.get('name')?.invalid && tenantForm.get('name')?.touched">
              Club name is required
            </div>
          </div>

          <div class="form-group">
            <label for="description">Description (Optional)</label>
            <textarea
              id="description"
              formControlName="description"
              placeholder="Enter club description"
              rows="3">
            </textarea>
          </div>

          <button type="submit" [disabled]="tenantForm.invalid || isLoading">
            {{ isLoading ? 'Creating...' : 'Create Club' }}
          </button>
        </form>
      </div>
    </div>
  `,
  styles: [`
    .create-tenant-container {
      min-height: 100vh;
      padding: 40px;
      background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .create-tenant-card {
      background: white;
      padding: 40px;
      border-radius: 12px;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      width: 100%;
      max-width: 500px;

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

    .tenant-form {
      .form-group {
        margin-bottom: 20px;

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

          &.error {
            border-color: #e74c3c;
          }

          &:focus {
            outline: none;
            border-color: #3498db;
          }
        }

        textarea {
          resize: vertical;
        }
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

    button {
      width: 100%;
      padding: 12px;
      background-color: #3498db;
      color: white;
      border: none;
      border-radius: 6px;
      font-size: 16px;
      cursor: pointer;
      transition: background-color 0.2s;

      &:hover:not(:disabled) {
        background-color: #2980b9;
      }

      &:disabled {
        background-color: #bdc3c7;
        cursor: not-allowed;
      }
    }
  `]
})
export class CreateTenantComponent {
  private readonly tenantService = inject(TenantService);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);

  tenantForm: FormGroup = this.fb.group({
    name: ['', [Validators.required]],
    description: ['']
  });

  errorMessage: string | null = null;
  isLoading = false;

  async onSubmit(): Promise<void> {
    if (this.tenantForm.valid) {
      this.isLoading = true;
      this.errorMessage = null;

      try {
        const request: CreateTenantRequest = {
          name: this.tenantForm.get('name')?.value,
          description: this.tenantForm.get('description')?.value
        };

        await firstValueFrom(this.tenantService.createTenant(request));
        await this.router.navigate(['/dashboard']);
      } catch (error: any) {
        this.errorMessage = error.status === 401
          ? 'Authentication failed. Please try logging in again.'
          : 'Failed to create tenant. Please try again.';
      } finally {
        this.isLoading = false;
      }
    }
  }
}
