import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { LocationService, UpdateLocationRequest } from '../../core/services/location.service';
import { TranslateModule } from '@ngx-translate/core';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-edit-location',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslateModule
  ],
  template: `
    <div class="edit-location-container">
      <div class="edit-location-content">
        <h2>{{ 'location.edit.title' | translate }}</h2>
        <form [formGroup]="locationForm" (ngSubmit)="onSubmit()">
          <div class="form-group">
            <label for="address">{{ 'location.form.address' | translate }}</label>
            <input id="address" type="text" formControlName="address" class="form-control">
            <div *ngIf="locationForm.get('address')?.errors?.['required'] && locationForm.get('address')?.touched" class="error-message">
              {{ 'location.form.addressRequired' | translate }}
            </div>
          </div>

          <div class="form-group">
            <label for="city">{{ 'location.form.city' | translate }}</label>
            <input id="city" type="text" formControlName="city" class="form-control">
            <div *ngIf="locationForm.get('city')?.errors?.['required'] && locationForm.get('city')?.touched" class="error-message">
              {{ 'location.form.cityRequired' | translate }}
            </div>
          </div>

          <div class="form-group">
            <label for="state">{{ 'location.form.state' | translate }}</label>
            <input id="state" type="text" formControlName="state" class="form-control">
            <div *ngIf="locationForm.get('state')?.errors?.['required'] && locationForm.get('state')?.touched" class="error-message">
              {{ 'location.form.stateRequired' | translate }}
            </div>
          </div>

          <div class="form-group">
            <label for="postalCode">{{ 'location.form.postalCode' | translate }}</label>
            <input id="postalCode" type="text" formControlName="postalCode" class="form-control">
            <div *ngIf="locationForm.get('postalCode')?.errors?.['required'] && locationForm.get('postalCode')?.touched" class="error-message">
              {{ 'location.form.postalCodeRequired' | translate }}
            </div>
          </div>

          <div class="form-group">
            <label for="country">{{ 'location.form.country' | translate }}</label>
            <input id="country" type="text" formControlName="country" class="form-control">
            <div *ngIf="locationForm.get('country')?.errors?.['required'] && locationForm.get('country')?.touched" class="error-message">
              {{ 'location.form.countryRequired' | translate }}
            </div>
          </div>

          <div class="form-group">
            <label for="timezone">{{ 'location.form.timezone' | translate }}</label>
            <input id="timezone" type="text" formControlName="timezone" class="form-control">
            <div *ngIf="locationForm.get('timezone')?.errors?.['required'] && locationForm.get('timezone')?.touched" class="error-message">
              {{ 'location.form.timezoneRequired' | translate }}
            </div>
          </div>

          <div class="form-actions">
            <button type="button" class="btn btn-secondary" (click)="onCancel()">
              {{ 'common.cancel' | translate }}
            </button>
            <button type="submit" class="btn btn-primary" [disabled]="locationForm.invalid || isLoading">
              {{ (isLoading ? 'common.saving' : 'common.save') | translate }}
            </button>
          </div>
        </form>
      </div>
    </div>
  `,
  styles: [`
    .edit-location-container {
      min-height: 100vh;
      padding: 40px;
      background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    }

    .edit-location-content {
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
export class EditLocationComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly locationService = inject(LocationService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  locationForm: FormGroup;
  isLoading = false;
  tenantId: string | null = null;
  locationId: string | null = null;

  constructor() {
    this.locationForm = this.fb.group({
      address: ['', Validators.required],
      city: ['', Validators.required],
      state: ['', Validators.required],
      postalCode: ['', Validators.required],
      country: ['', Validators.required],
      timezone: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const params = this.route.snapshot.params;
    this.tenantId = params['tenantId'];
    this.locationId = params['locationId'];
    if (this.tenantId && this.locationId) {
      this.loadLocation();
    }
  }

  private async loadLocation(): Promise<void> {
    if (this.tenantId && this.locationId) {
      this.isLoading = true;
      try {
        const location = await firstValueFrom(this.locationService.getLocation(this.tenantId, this.locationId));
        this.locationForm.patchValue({
          address: location.address,
          city: location.city,
          state: location.state,
          postalCode: location.postalCode,
          country: location.country,
          timezone: location.timezone
        });
      } finally {
        this.isLoading = false;
      }
    }
  }

  async onSubmit(): Promise<void> {
    if (this.locationForm.valid && this.tenantId && this.locationId) {
      this.isLoading = true;
      try {
        const request: UpdateLocationRequest = this.locationForm.value;
        await firstValueFrom(this.locationService.updateLocation(this.tenantId, this.locationId, request));
        await this.router.navigate(['/dashboard']);
      } finally {
        this.isLoading = false;
      }
    }
  }

  onCancel(): void {
    this.router.navigate(['/dashboard']);
  }
} 