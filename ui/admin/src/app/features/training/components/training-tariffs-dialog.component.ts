import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { TranslateModule } from '@ngx-translate/core';
import { TariffService, TariffResponse } from '../../../core/services/tariff.service';
import { TrainingService } from '../../../core/services/training.service';
import { firstValueFrom } from 'rxjs';

export interface TrainingTariffsDialogData {
  tenantId: string;
  trainingId: string;
  trainingName: string;
}

@Component({
  selector: 'app-training-tariffs-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatCheckboxModule,
    TranslateModule
  ],
  template: `
    <h2 mat-dialog-title>{{ 'training.tariffs.dialog.title' | translate:{ name: data.trainingName } }}</h2>
    <mat-dialog-content>
      <div class="loading" *ngIf="isLoading">
        {{ 'common.loading' | translate }}
      </div>

      <div class="empty-state" *ngIf="!isLoading && !allTariffs?.length">
        {{ 'training.tariffs.dialog.empty' | translate }}
      </div>

      <div class="tariffs-list" *ngIf="!isLoading && allTariffs?.length">
        <div class="tariff-item" *ngFor="let tariff of allTariffs">
          <mat-checkbox
            [checked]="isSelected(tariff)"
            (change)="toggleTariff(tariff)"
            [attr.aria-label]="'training.tariffs.dialog.select' | translate:{ name: tariff.name }">
            <div class="tariff-info">
              <div class="name">{{ tariff.name }}</div>
              <div class="price">{{ tariff.price | currency:tariff.currency }}</div>
            </div>
          </mat-checkbox>
        </div>
      </div>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button type="button" (click)="onCancel()">
        {{ 'common.cancel' | translate }}
      </button>
      <button mat-raised-button color="primary" (click)="onSave()" [disabled]="isSaving">
        {{ (isSaving ? 'common.saving' : 'common.save') | translate }}
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
export class TrainingTariffsDialogComponent implements OnInit {
  isLoading = true;
  isSaving = false;
  allTariffs: TariffResponse[] = [];
  selectedTariffIds: Set<string> = new Set();

  constructor(
    private dialogRef: MatDialogRef<TrainingTariffsDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: TrainingTariffsDialogData,
    private tariffService: TariffService,
    private trainingService: TrainingService
  ) {}

  async ngOnInit() {
    try {
      const [allTariffs, trainingTariffs] = await Promise.all([
        firstValueFrom(this.tariffService.findAll(this.data.tenantId)),
        firstValueFrom(this.trainingService.getTrainingTariffs(this.data.tenantId, this.data.trainingId))
      ]);

      this.allTariffs = allTariffs;
      this.selectedTariffIds = new Set(trainingTariffs.map(t => t.id));
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

  onCancel(): void {
    this.dialogRef.close();
  }

  async onSave(): Promise<void> {
    this.isSaving = true;
    try {
      await firstValueFrom(this.trainingService.updateTrainingTariffs(
        this.data.tenantId,
        this.data.trainingId,
        { tariffIds: Array.from(this.selectedTariffIds) }
      ));
      this.dialogRef.close(true);
    } catch (error) {
      console.error('Failed to update training tariffs:', error);
    } finally {
      this.isSaving = false;
    }
  }
} 