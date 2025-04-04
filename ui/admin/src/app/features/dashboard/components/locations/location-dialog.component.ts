import { Component, Inject, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { TranslateModule } from '@ngx-translate/core';
import { LocationPageItemResponse } from '../../../../core/services/location.service';
import { TimezoneService } from '../../../../core/services/timezone.service';

export interface LocationDialogData {
  location?: LocationPageItemResponse;
}

@Component({
  selector: 'app-location-dialog',
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
    <h2 mat-dialog-title>{{ (data.location ? 'location.edit.title' : 'location.create.title') | translate }}</h2>
    <form [formGroup]="form" (ngSubmit)="onSubmit()">
      <mat-dialog-content>
        <div class="form-container">
          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'location.form.address' | translate }}</mat-label>
            <input matInput formControlName="address" required>
            <mat-error *ngIf="form.get('address')?.hasError('required')">
              {{ 'location.form.addressRequired' | translate }}
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'location.form.city' | translate }}</mat-label>
            <input matInput formControlName="city" required>
            <mat-error *ngIf="form.get('city')?.hasError('required')">
              {{ 'location.form.cityRequired' | translate }}
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'location.form.state' | translate }}</mat-label>
            <input matInput formControlName="state" required>
            <mat-error *ngIf="form.get('state')?.hasError('required')">
              {{ 'location.form.stateRequired' | translate }}
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'location.form.postalCode' | translate }}</mat-label>
            <input matInput formControlName="postalCode" required>
            <mat-error *ngIf="form.get('postalCode')?.hasError('required')">
              {{ 'location.form.postalCodeRequired' | translate }}
            </mat-error>
            <mat-error *ngIf="form.get('postalCode')?.hasError('pattern')">
              {{ 'location.form.postalCodeInvalid' | translate }}
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'location.form.country' | translate }}</mat-label>
            <input matInput formControlName="country" required>
            <mat-error *ngIf="form.get('country')?.hasError('required')">
              {{ 'location.form.countryRequired' | translate }}
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'location.form.timezone' | translate }}</mat-label>
            <mat-select formControlName="timezone" required>
              <mat-option *ngFor="let timezone of timezones" [value]="timezone.value">
                {{ timezone.label }}
              </mat-option>
            </mat-select>
            <mat-error *ngIf="form.get('timezone')?.hasError('required')">
              {{ 'location.form.timezoneRequired' | translate }}
            </mat-error>
          </mat-form-field>
        </div>
      </mat-dialog-content>
      <mat-dialog-actions align="end">
        <button mat-button type="button" (click)="onCancel()">
          {{ 'common.cancel' | translate }}
        </button>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid">
          {{ (data.location ? 'common.save' : 'common.create') | translate }}
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
export class LocationDialogComponent {
  private readonly fb = inject(FormBuilder);
  private readonly timezoneService = inject(TimezoneService);
  form: FormGroup;
  timezones = this.timezoneService.getTimezones();

  constructor(
    private dialogRef: MatDialogRef<LocationDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: LocationDialogData
  ) {
    this.form = this.fb.group({
      address: [data.location?.address ?? '', Validators.required],
      city: [data.location?.city ?? '', Validators.required],
      state: [data.location?.state ?? '', Validators.required],
      postalCode: [data.location?.postalCode ?? '', [
        Validators.required,
        Validators.pattern('^[A-Z0-9]{3,10}(?:[-\s]?[A-Z0-9]+)*$')
      ]],
      country: [data.location?.country ?? '', Validators.required],
      timezone: [data.location?.timezone ?? this.timezoneService.getUserTimezone(), Validators.required]
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