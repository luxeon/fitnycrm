import { Component, inject, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { TenantService, CreateTenantRequest } from '../../core/services/tenant.service';
import { firstValueFrom, Subject, takeUntil } from 'rxjs';

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
        <h2>{{ 'tenant.create.title' | translate }}</h2>
        <p class="subtitle">{{ 'tenant.create.subtitle' | translate }}</p>

        <div *ngIf="errorMessage" class="error-banner">
          {{ errorMessage | translate }}
        </div>

        <form [formGroup]="tenantForm" (ngSubmit)="onSubmit()" class="tenant-form">
          <div class="form-group">
            <label for="name">{{ 'tenant.form.name.label' | translate }}</label>
            <input
              id="name"
              type="text"
              formControlName="name"
              [class.error]="tenantForm.get('name')?.invalid && tenantForm.get('name')?.touched"
              [placeholder]="'tenant.form.name.placeholder' | translate">
            <div class="error-message" *ngIf="tenantForm.get('name')?.invalid && tenantForm.get('name')?.touched">
              {{ 'tenant.form.name.required' | translate }}
            </div>
          </div>

          <div class="form-group">
            <label for="description">{{ 'tenant.form.description.label' | translate }}</label>
            <textarea
              id="description"
              formControlName="description"
              [placeholder]="'tenant.form.description.placeholder' | translate"
              rows="3">
            </textarea>
          </div>

          <div class="form-group">
            <label for="locale">{{ 'tenant.form.locale.label' | translate }}</label>
            <select
              id="locale"
              formControlName="locale"
              [class.error]="tenantForm.get('locale')?.invalid && tenantForm.get('locale')?.touched">
              <option value="en">English</option>
              <option value="uk">Українська</option>
            </select>
            <div class="error-message" *ngIf="tenantForm.get('locale')?.invalid && tenantForm.get('locale')?.touched">
              {{ 'tenant.form.locale.required' | translate }}
            </div>
          </div>

          <button type="submit" [disabled]="tenantForm.invalid || isLoading">
            {{ isLoading ? ('tenant.form.button.creating' | translate) : ('tenant.form.button.create' | translate) }}
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

        input, textarea, select {
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

        select {
          background-color: white;
          cursor: pointer;
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
export class CreateTenantComponent implements OnDestroy {
  private readonly tenantService = inject(TenantService);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);
  private readonly translate = inject(TranslateService);
  private readonly destroy$ = new Subject<void>();

  tenantForm: FormGroup = this.fb.group({
    name: ['', [Validators.required]],
    description: [''],
    locale: [this.translate.currentLang || 'en', [Validators.required]]
  });

  errorMessage: string | null = null;
  isLoading = false;

  constructor() {
    // Subscribe to locale changes from the form
    this.tenantForm.get('locale')?.valueChanges
      .pipe(takeUntil(this.destroy$))
      .subscribe(locale => {
        if (this.translate.currentLang !== locale) {
          this.translate.use(locale);
          localStorage.setItem('selectedLanguage', locale);
        }
      });

    // Subscribe to language changes from the language switcher
    this.translate.onLangChange
      .pipe(takeUntil(this.destroy$))
      .subscribe(event => {
        if (this.tenantForm.get('locale')?.value !== event.lang) {
          this.tenantForm.patchValue({ locale: event.lang }, { emitEvent: false });
        }
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  async onSubmit(): Promise<void> {
    if (this.tenantForm.valid) {
      this.isLoading = true;
      this.errorMessage = null;

      try {
        const request: CreateTenantRequest = {
          name: this.tenantForm.get('name')?.value,
          description: this.tenantForm.get('description')?.value,
          locale: this.tenantForm.get('locale')?.value
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
