import { Component, Inject, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { TranslateModule } from '@ngx-translate/core';
import { TrainerDetailsResponse, TrainerService } from '../../../../../core/services/trainer.service';
import { ErrorHandlerService } from '../../../../../core/services/error-handler.service';
import { HttpErrorResponse } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

export interface TrainerDialogData {
  trainer?: TrainerDetailsResponse;
  tenantId: string;
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
          <!-- Error message display -->
          <div *ngIf="errorMessage" class="error-banner">
            {{ errorMessage }}
          </div>

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
        <button mat-button type="button" (click)="onCancel()" [disabled]="isSubmitting">
          {{ 'common.cancel' | translate }}
        </button>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || isSubmitting">
          {{ isSubmitting ? 'common.saving' : (data.trainer ? 'common.save' : 'common.create') | translate }}
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

    .error-banner {
      background-color: #ffebee;
      color: #c62828;
      padding: 12px 16px;
      border-radius: 4px;
      border-left: 4px solid #f44336;
      margin-bottom: 16px;
      font-size: 14px;
      line-height: 1.4;
    }
  `]
})
export class TrainerDialogComponent {
  private readonly trainerService = inject(TrainerService);
  private readonly errorHandler = inject(ErrorHandlerService);

  form: FormGroup;
  errorMessage: string | null = null;
  isSubmitting = false;

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

    // Subscribe to phone number changes to auto-remove spaces and braces
    this.form.get('phoneNumber')?.valueChanges.subscribe(value => {
      if (value) {
        const cleanValue = value.replace(/[\s()]/g, '');
        if (cleanValue !== value) {
          this.form.patchValue({ phoneNumber: cleanValue }, { emitEvent: false });
        }
      }
    });
  }

  async onSubmit(): Promise<void> {
    if (this.form.valid && !this.isSubmitting) {
      this.errorMessage = null;
      this.isSubmitting = true;

      try {
        if (this.data.trainer) {
          // Update existing trainer
          await firstValueFrom(this.trainerService.updateTrainer(
            this.data.tenantId, 
            this.data.trainer.id, 
            this.form.value
          ));
        } else {
          // Create new trainer
          await firstValueFrom(this.trainerService.createTrainer(
            this.data.tenantId, 
            this.form.value
          ));
        }
        
        // Success - close dialog with success result
        this.dialogRef.close({ success: true, isUpdate: !!this.data.trainer });
      } catch (error: any) {
        // Handle error - show in dialog
        const errorMessage = this.errorHandler.processError(error as HttpErrorResponse, 'trainer');
        this.errorMessage = errorMessage.message;
        this.isSubmitting = false;
      }
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
