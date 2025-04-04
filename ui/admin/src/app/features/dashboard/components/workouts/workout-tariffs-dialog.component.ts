import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { TariffService, TariffResponse } from '../../../../core/services/tariff.service';
import { TrainingService } from '../../../../core/services/training.service';
import { firstValueFrom } from 'rxjs';

export interface WorkoutTariffsDialogData {
  tenantId: string;
  workoutId: string;
  workoutName: string;
}

@Component({
  selector: 'app-workout-tariffs-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatSelectModule,
    MatCheckboxModule,
    MatButtonModule,
    FormsModule,
    TranslateModule
  ],
  template: `
    <h2 mat-dialog-title>{{ 'dashboard.workouts.tariffs.title' | translate:{ name: data.workoutName } }}</h2>
    
    <mat-dialog-content>
      <div class="loading" *ngIf="isLoading">
        {{ 'common.loading' | translate }}
      </div>

      <div class="empty-state" *ngIf="!isLoading && (!allTariffs || !allTariffs.length)">
        {{ 'dashboard.workouts.tariffs.empty' | translate }}
      </div>

      <div class="tariffs-list" *ngIf="!isLoading && allTariffs && allTariffs.length">
        <div class="tariff-item" *ngFor="let tariff of allTariffs">
          <mat-checkbox 
            [checked]="isSelected(tariff)" 
            (change)="toggleTariff(tariff)"
            [attr.aria-label]="tariff.name">
            <div class="tariff-info">
              <div class="name">{{ tariff.name }}</div>
              <div class="price">{{ tariff.price | currency:tariff.currency }}</div>
            </div>
          </mat-checkbox>
        </div>
      </div>
    </mat-dialog-content>

    <mat-dialog-actions align="end">
      <button mat-button (click)="onCancel()">
        {{ 'common.cancel' | translate }}
      </button>
      <button mat-raised-button color="primary" [disabled]="isSaving" (click)="onSave()">
        {{ isSaving ? ('common.saving' | translate) : ('common.save' | translate) }}
      </button>
    </mat-dialog-actions>
  `,
  styles: [`
    mat-dialog-content {
      min-width: 400px;
      max-height: 60vh;
    }

    .loading {
      text-align: center;
      padding: 2rem;
      color: #6c757d;
    }

    .empty-state {
      text-align: center;
      padding: 2rem;
      background: #f8f9fa;
      border-radius: 8px;
      color: #6c757d;
    }

    .tariffs-list {
      display: flex;
      flex-direction: column;
      gap: 1rem;
      padding: 1rem 0;
    }

    .tariff-item {
      padding: 0.5rem;
      border-radius: 4px;
      transition: background-color 0.2s;

      &:hover {
        background-color: #f8f9fa;
      }

      .tariff-info {
        display: inline-flex;
        flex-direction: column;
        margin-left: 0.5rem;

        .name {
          font-weight: 500;
          color: #2c3e50;
        }

        .price {
          font-size: 0.9rem;
          color: #3498db;
        }
      }
    }
  `]
})
export class WorkoutTariffsDialogComponent implements OnInit {
  isLoading = true;
  isSaving = false;
  allTariffs: TariffResponse[] = [];
  selectedTariffIds: Set<string> = new Set();
  
  constructor(
    private dialogRef: MatDialogRef<WorkoutTariffsDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: WorkoutTariffsDialogData,
    private tariffService: TariffService,
    private trainingService: TrainingService
  ) {}
  
  async ngOnInit(): Promise<void> {
    await this.loadData();
  }
  
  async loadData(): Promise<void> {
    try {
      const [allTariffs, workoutTariffs] = await Promise.all([
        firstValueFrom(this.tariffService.findAll(this.data.tenantId)),
        firstValueFrom(this.trainingService.getTrainingTariffs(this.data.tenantId, this.data.workoutId))
      ]);

      this.allTariffs = allTariffs;
      this.selectedTariffIds = new Set(workoutTariffs.map((tariff: any) => tariff.id));
    } catch (error) {
      console.error('Failed to load tariffs:', error);
    } finally {
      this.isLoading = false;
    }
  }
  
  isSelected(tariff: TariffResponse): boolean {
    return this.selectedTariffIds.has(tariff.id);
  }
  
  toggleTariff(tariff: TariffResponse): void {
    if (this.selectedTariffIds.has(tariff.id)) {
      this.selectedTariffIds.delete(tariff.id);
    } else {
      this.selectedTariffIds.add(tariff.id);
    }
  }
  
  async onSave(): Promise<void> {
    this.isSaving = true;
    try {
      await firstValueFrom(this.trainingService.updateTrainingTariffs(
        this.data.tenantId,
        this.data.workoutId,
        { tariffIds: Array.from(this.selectedTariffIds) }
      ));
      this.dialogRef.close(true);
    } catch (error) {
      console.error('Failed to update tariffs:', error);
    } finally {
      this.isSaving = false;
    }
  }
  
  onCancel(): void {
    this.dialogRef.close(false);
  }
} 