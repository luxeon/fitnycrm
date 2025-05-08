import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { TranslateModule } from '@ngx-translate/core';
import { firstValueFrom } from 'rxjs';
import { TrainingService, TrainingPageItemResponse } from '../../../../../core/services/training.service';
import { TariffService, TariffResponse } from '../../../../../core/services/tariff.service';

export interface PaymentDialogData {
  tenantId: string;
  clientId: string;
}

interface CurrencyOption {
  value: string;
  label: string;
}

@Component({
  selector: 'app-payment-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    TranslateModule
  ],
  template: `
    <h2 mat-dialog-title>{{ 'dashboard.clients.details.payments.add.title' | translate }}</h2>
    <form [formGroup]="form" (ngSubmit)="onSubmit()">
      <mat-dialog-content>
        <div class="form-container">
          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'dashboard.clients.details.payments.add.training' | translate }}</mat-label>
            <mat-select formControlName="trainingId" required>
              <mat-option *ngFor="let training of trainings" [value]="training.id">
                {{ training.name }}
              </mat-option>
            </mat-select>
            <mat-error *ngIf="form.get('trainingId')?.hasError('required')">
              {{ 'common.validation.required' | translate }}
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'dashboard.clients.details.payments.add.tariff' | translate }}</mat-label>
            <mat-select formControlName="tariffId" (selectionChange)="onTariffSelected()">
              <mat-option [value]="null">{{ 'common.custom' | translate }}</mat-option>
              <mat-option *ngFor="let tariff of tariffs" [value]="tariff.id">
                {{ tariff.name }}
              </mat-option>
            </mat-select>
          </mat-form-field>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'dashboard.clients.details.payments.add.trainingsCount' | translate }}</mat-label>
            <input matInput type="number" formControlName="trainingsCount" required min="1">
            <mat-error *ngIf="form.get('trainingsCount')?.hasError('required')">
              {{ 'common.validation.required' | translate }}
            </mat-error>
            <mat-error *ngIf="form.get('trainingsCount')?.hasError('min')">
              {{ 'common.validation.min' | translate:{ min: 1 } }}
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'dashboard.clients.details.payments.add.validDays' | translate }}</mat-label>
            <input matInput type="number" formControlName="validDays" required min="1">
            <mat-error *ngIf="form.get('validDays')?.hasError('required')">
              {{ 'common.validation.required' | translate }}
            </mat-error>
            <mat-error *ngIf="form.get('validDays')?.hasError('min')">
              {{ 'common.validation.min' | translate:{ min: 1 } }}
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'dashboard.clients.details.payments.add.price' | translate }}</mat-label>
            <input matInput type="number" formControlName="price" required min="0" step="1">
            <mat-error *ngIf="form.get('price')?.hasError('required')">
              {{ 'common.validation.required' | translate }}
            </mat-error>
            <mat-error *ngIf="form.get('price')?.hasError('min')">
              {{ 'common.validation.min' | translate:{ min: 0 } }}
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'dashboard.clients.details.payments.add.currency' | translate }}</mat-label>
            <mat-select formControlName="currency" required>
              <mat-option *ngFor="let currency of currencies" [value]="currency.value">
                {{ currency.label }}
              </mat-option>
            </mat-select>
            <mat-error *ngIf="form.get('currency')?.hasError('required')">
              {{ 'common.validation.required' | translate }}
            </mat-error>
          </mat-form-field>
        </div>
      </mat-dialog-content>
      <mat-dialog-actions align="end">
        <button mat-button type="button" (click)="onCancel()">
          {{ 'common.cancel' | translate }}
        </button>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || isLoading">
          {{ (isLoading ? 'common.saving' : 'common.save') | translate }}
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
      min-width: 400px;
    }
  `]
})
export class PaymentDialogComponent implements OnInit {
  form: FormGroup;
  trainings: TrainingPageItemResponse[] = [];
  tariffs: TariffResponse[] = [];
  isLoading = false;
  currencies: CurrencyOption[] = [
    { value: 'USD', label: 'USD - US Dollar' },
    { value: 'EUR', label: 'EUR - Euro' },
    { value: 'UAH', label: 'UAH - Ukrainian Hryvnia' }
  ];

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<PaymentDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: PaymentDialogData,
    private trainingService: TrainingService,
    private tariffService: TariffService
  ) {
    this.form = this.fb.group({
      trainingId: ['', Validators.required],
      tariffId: [null],
      trainingsCount: [10, [Validators.required, Validators.min(1)]],
      validDays: [30, [Validators.required, Validators.min(1)]],
      price: [100, [Validators.required, Validators.min(0)]],
      currency: ['UAH', Validators.required]
    });
  }

  async ngOnInit(): Promise<void> {
    try {
      this.isLoading = true;
      const [trainingsResponse, tariffsResponse] = await Promise.all([
        firstValueFrom(this.trainingService.findAll(this.data.tenantId)),
        firstValueFrom(this.tariffService.findAll(this.data.tenantId))
      ]);

      this.trainings = trainingsResponse || [];
      this.tariffs = tariffsResponse || [];
    } catch (error) {
      console.error('Failed to load data:', error);
      this.trainings = [];
      this.tariffs = [];
    } finally {
      this.isLoading = false;
    }
  }

  onTariffSelected(): void {
    const tariffId = this.form.get('tariffId')?.value;
    if (!tariffId) {
      return;
    }

    const selectedTariff = this.tariffs.find(t => t.id === tariffId);
    if (selectedTariff) {
      this.form.patchValue({
        trainingsCount: selectedTariff.trainingsCount,
        validDays: selectedTariff.validDays,
        price: selectedTariff.price,
        currency: selectedTariff.currency
      });
    }
  }

  onSubmit(): void {
    if (this.form.valid) {
      // Remove tariffId from the submitted form data
      const formData = { ...this.form.value };
      delete formData.tariffId;

      this.dialogRef.close(formData);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
