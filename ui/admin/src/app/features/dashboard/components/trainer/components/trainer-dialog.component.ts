import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { TranslateModule } from '@ngx-translate/core';
import { TrainerDetailsResponse } from '../../../../../core/services/trainer.service';

export interface TrainerDialogData {
  trainer?: TrainerDetailsResponse;
}

@Component({
  selector: 'app-trainer-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    TranslateModule
  ],
  template: `
    <h2 mat-dialog-title>{{ (data.trainer ? 'trainer.edit.title' : 'trainer.create.title') | translate }}</h2>
    <form [formGroup]="form" (ngSubmit)="onSubmit()">
      <mat-dialog-content>
        <div class="form-container">
          <div class="form-row">
            <mat-form-field appearance="outline">
              <mat-label>{{ 'trainer.form.firstName.label' | translate }}</mat-label>
              <input matInput formControlName="firstName" required>
              <mat-error *ngIf="form.get('firstName')?.hasError('required')">
                {{ 'common.validation.required' | translate }}
              </mat-error>
            </mat-form-field>

            <mat-form-field appearance="outline">
              <mat-label>{{ 'trainer.form.lastName.label' | translate }}</mat-label>
              <input matInput formControlName="lastName" required>
              <mat-error *ngIf="form.get('lastName')?.hasError('required')">
                {{ 'common.validation.required' | translate }}
              </mat-error>
            </mat-form-field>
          </div>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'trainer.form.email.label' | translate }}</mat-label>
            <input matInput type="email" formControlName="email" required>
            <mat-error *ngIf="form.get('email')?.hasError('required')">
              {{ 'common.validation.required' | translate }}
            </mat-error>
            <mat-error *ngIf="form.get('email')?.hasError('email')">
              {{ 'common.validation.email' | translate }}
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'trainer.form.phoneNumber.label' | translate }}</mat-label>
            <input matInput formControlName="phoneNumber" required [placeholder]="'trainer.form.phoneNumber.placeholder' | translate">
            <mat-error *ngIf="form.get('phoneNumber')?.hasError('required')">
              {{ 'trainer.form.phoneNumber.required' | translate }}
            </mat-error>
            <mat-error *ngIf="form.get('phoneNumber')?.hasError('pattern')">
              {{ 'trainer.form.phoneNumber.invalid' | translate }}
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>{{ 'trainer.form.specialization.label' | translate }}</mat-label>
            <input matInput formControlName="specialization">
          </mat-form-field>
        </div>
      </mat-dialog-content>
      <mat-dialog-actions align="end">
        <button mat-button type="button" (click)="onCancel()">
          {{ 'common.cancel' | translate }}
        </button>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid">
          {{ (data.trainer ? 'common.save' : 'common.create') | translate }}
        </button>
      </mat-dialog-actions>
    </form>
  `,
  styles: [`
    :host {
      display: block;
      width: 100%;
      max-width: 500px;
    }

    .form-container {
      display: flex;
      flex-direction: column;
      gap: 1rem;
      padding: 1rem 0;
    }

    .form-row {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 1rem;
    }

    .full-width {
      width: 100%;
    }

    mat-dialog-content {
      width: 100%;
      box-sizing: border-box;
      padding: 0 1rem;
    }

    mat-dialog-actions {
      padding: 1rem;
    }
  `]
})
export class TrainerDialogComponent {
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<TrainerDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: TrainerDialogData
  ) {
    this.form = this.fb.group({
      firstName: [data.trainer?.firstName ?? '', Validators.required],
      lastName: [data.trainer?.lastName ?? '', Validators.required],
      email: [data.trainer?.email ?? '', [Validators.required, Validators.email]],
      phoneNumber: [data.trainer?.phoneNumber ?? '', [
        Validators.required,
        Validators.minLength(10),
        Validators.maxLength(15),
        Validators.pattern(/^\+[0-9]{9,14}$/)
      ]],
      specialization: [data.trainer?.specialization ?? '']
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
