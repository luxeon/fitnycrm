import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { TranslateModule } from '@ngx-translate/core';
import { TariffResponse } from '../../../../core/services/tariff.service';

export interface TariffDialogData {
  tariff?: TariffResponse;
}

@Component({
  selector: 'app-tariff-dialog',
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
    <h2 mat-dialog-title>{{ (data.tariff ? 'dashboard.tariffs.edit' : 'dashboard.tariffs.add') | translate }}</h2>
    <form [formGroup]="form" (ngSubmit)="onSubmit()">
      <mat-dialog-content>
        <div class="form-container">
          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'dashboard.tariffs.name' | translate }}</mat-label>
            <input matInput formControlName="name" required>
            <mat-error *ngIf="form.get('name')?.hasError('required')">
              {{ 'common.validation.required' | translate }}
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'dashboard.tariffs.trainingsCount' | translate }}</mat-label>
            <input matInput type="number" formControlName="trainingsCount" required min="1">
            <mat-error *ngIf="form.get('trainingsCount')?.hasError('required')">
              {{ 'common.validation.required' | translate }}
            </mat-error>
            <mat-error *ngIf="form.get('trainingsCount')?.hasError('min')">
              {{ 'common.validation.min' | translate:{ min: 1 } }}
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'dashboard.tariffs.validDays' | translate }}</mat-label>
            <input matInput type="number" formControlName="validDays" required min="1">
            <mat-error *ngIf="form.get('validDays')?.hasError('required')">
              {{ 'common.validation.required' | translate }}
            </mat-error>
            <mat-error *ngIf="form.get('validDays')?.hasError('min')">
              {{ 'common.validation.min' | translate:{ min: 1 } }}
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'dashboard.tariffs.price' | translate }}</mat-label>
            <input matInput type="number" formControlName="price" required min="0.01" step="0.01">
            <mat-error *ngIf="form.get('price')?.hasError('required')">
              {{ 'common.validation.required' | translate }}
            </mat-error>
            <mat-error *ngIf="form.get('price')?.hasError('min')">
              {{ 'common.validation.min' | translate:{ min: 0.01 } }}
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'dashboard.tariffs.currency' | translate }}</mat-label>
            <mat-select formControlName="currency" required>
              <mat-option value="USD">USD</mat-option>
              <mat-option value="EUR">EUR</mat-option>
              <mat-option value="GBP">GBP</mat-option>
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
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid">
          {{ (data.tariff ? 'common.save' : 'common.create') | translate }}
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
export class TariffDialogComponent {
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<TariffDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: TariffDialogData
  ) {
    this.form = this.fb.group({
      name: [data.tariff?.name ?? '', Validators.required],
      trainingsCount: [data.tariff?.trainingsCount ?? 1, [Validators.required, Validators.min(1)]],
      validDays: [data.tariff?.validDays ?? 30, [Validators.required, Validators.min(1)]],
      price: [data.tariff?.price ?? 0.01, [Validators.required, Validators.min(0.01)]],
      currency: [data.tariff?.currency ?? 'USD', Validators.required]
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