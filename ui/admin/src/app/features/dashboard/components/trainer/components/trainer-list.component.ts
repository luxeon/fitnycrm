import { Component, Input, Output, EventEmitter, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { TrainerService, TrainerPageItemResponse } from '../../../../../core/services/trainer.service';
import { ConfirmationDialogComponent } from '../../confirmation-dialog/confirmation-dialog.component';
import { firstValueFrom } from 'rxjs';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatTooltipModule } from '@angular/material/tooltip';
import { TrainerDialogComponent } from './trainer-dialog.component';

@Component({
  selector: 'app-trainer-list',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    ConfirmationDialogComponent,
    MatSnackBarModule,
    MatDialogModule,
    MatTooltipModule
  ],
  template: `
    <div class="trainers-grid">
      <div class="trainer-card" *ngFor="let trainer of trainers">
        <div class="trainer-info">
          <div class="card-actions">
            <button class="edit-btn" (click)="onEditClick(trainer)" matTooltip="{{ 'common.edit' | translate }}">
              <span class="edit-icon">✎</span>
            </button>
            <button class="delete-btn" (click)="onDeleteClick(trainer)" matTooltip="{{ 'common.delete' | translate }}">
              <span class="delete-icon">×</span>
            </button>
          </div>
          <h3>{{ trainer.firstName }} {{ trainer.lastName }}</h3>
          <p class="email">{{ trainer.email }}</p>
          <div class="details">
            <span class="detail" *ngIf="trainer.phoneNumber">
              <i class="phone-icon">📱</i>
              {{ trainer.phoneNumber }}
            </span>
            <span class="detail" *ngIf="trainer.specialization">
              <i class="specialization-icon">🎯</i>
              {{ trainer.specialization }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <div class="pagination" *ngIf="totalPages > 1">
      <button
        [disabled]="currentPage === 0"
        (click)="onPageChange(currentPage - 1)">
        {{ 'common.previous' | translate }}
      </button>
      <span class="page-info">
        {{ 'common.page' | translate }} {{ currentPage + 1 }} {{ 'common.of' | translate }} {{ totalPages }}
      </span>
      <button
        [disabled]="currentPage === totalPages - 1"
        (click)="onPageChange(currentPage + 1)">
        {{ 'common.next' | translate }}
      </button>
    </div>

    <app-confirmation-dialog
      *ngIf="trainerToDelete"
      [title]="'trainer.delete.title' | translate"
      [message]="'trainer.delete.message' | translate"
      [confirmText]="'trainer.delete.confirm' | translate"
      (confirm)="onDeleteConfirm()"
      (cancel)="onDeleteCancel()"
    ></app-confirmation-dialog>
  `,
  styles: [`
    .trainers-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
      gap: 1rem;
      margin-bottom: 1rem;
    }

    .trainer-card {
      background: #f8f9fa;
      border-radius: 8px;
      border: 1px solid #e9ecef;
      padding: 1rem;
      transition: transform 0.2s, box-shadow 0.2s;
      position: relative;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        background: #f1f3f5;
      }
    }

    .trainer-info {
      h3 {
        color: #2c3e50;
        margin: 0 0 0.5rem;
        font-size: 1.1rem;
        font-weight: 600;
      }

      .email {
        color: #495057;
        font-size: 0.9rem;
        margin: 0 0 1rem;
      }

      .details {
        display: flex;
        gap: 1rem;
        color: #6c757d;
        font-size: 0.9rem;

        .detail {
          display: flex;
          align-items: center;
          gap: 0.25rem;

          i {
            font-style: normal;
          }
        }
      }
    }

    .card-actions {
      position: absolute;
      top: 8px;
      right: 8px;
      display: flex;
      gap: 8px;
    }

    .edit-btn, .delete-btn {
      width: 24px;
      height: 24px;
      border-radius: 50%;
      border: none;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      transition: background-color 0.2s;
    }

    .edit-btn {
      background: #3498db;
      color: white;

      .edit-icon {
        font-size: 14px;
        line-height: 1;
      }

      &:hover {
        background: #2980b9;
      }
    }

    .delete-btn {
      background: #e74c3c;
      color: white;

      .delete-icon {
        font-size: 18px;
        line-height: 1;
      }

      &:hover {
        background: #c0392b;
      }
    }

    .pagination {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 1rem;
      margin-top: 1rem;

      button {
        padding: 0.5rem 1rem;
        background: #e9ecef;
        border: none;
        border-radius: 4px;
        color: #495057;
        cursor: pointer;
        transition: background-color 0.2s;

        &:hover:not(:disabled) {
          background: #dee2e6;
        }

        &:disabled {
          opacity: 0.5;
          cursor: not-allowed;
        }
      }

      .page-info {
        color: #6c757d;
      }
    }
  `]
})
export class TrainerListComponent {
  private readonly trainerService = inject(TrainerService);
  private readonly snackBar = inject(MatSnackBar);
  private readonly translate = inject(TranslateService);
  private readonly dialog = inject(MatDialog);

  @Input() trainers: TrainerPageItemResponse[] = [];
  @Input() currentPage = 0;
  @Input() totalPages = 0;
  @Input() tenantId = '';
  @Input() locationId = '';
  @Output() pageChange = new EventEmitter<number>();
  @Output() trainerDeleted = new EventEmitter<void>();
  @Output() trainerCreated = new EventEmitter<void>();
  @Output() trainerUpdated = new EventEmitter<void>();

  trainerToDelete: TrainerPageItemResponse | null = null;

  onPageChange(page: number): void {
    this.pageChange.emit(page);
  }

  onAddClick(): void {
    const dialogRef = this.dialog.open(TrainerDialogComponent, {
      data: {}
    });

    dialogRef.afterClosed().subscribe(async result => {
      if (result) {
        try {
          await firstValueFrom(this.trainerService.createTrainer(this.tenantId, result));
          this.snackBar.open(
            this.translate.instant('trainer.create.success'),
            this.translate.instant('common.close'),
            { duration: 3000 }
          );
          this.trainerCreated.emit();
        } catch (error: any) {
          this.snackBar.open(
            this.translate.instant('trainer.create.error'),
            this.translate.instant('common.close'),
            { duration: 3000 }
          );
        }
      }
    });
  }

  onEditClick(trainer: TrainerPageItemResponse): void {
    const dialogRef = this.dialog.open(TrainerDialogComponent, {
      data: { trainer }
    });

    dialogRef.afterClosed().subscribe(async result => {
      if (result) {
        try {
          await firstValueFrom(this.trainerService.updateTrainer(this.tenantId, trainer.id, result));
          this.snackBar.open(
            this.translate.instant('trainer.update.success'),
            this.translate.instant('common.close'),
            { duration: 3000 }
          );
          this.trainerUpdated.emit();
        } catch (error: any) {
          this.snackBar.open(
            this.translate.instant('trainer.update.error'),
            this.translate.instant('common.close'),
            { duration: 3000 }
          );
        }
      }
    });
  }

  onDeleteClick(trainer: TrainerPageItemResponse): void {
    this.trainerToDelete = trainer;
  }

  onDeleteCancel(): void {
    this.trainerToDelete = null;
  }

  async onDeleteConfirm(): Promise<void> {
    if (this.trainerToDelete && this.tenantId) {
      try {
        await firstValueFrom(this.trainerService.deleteTrainer(this.tenantId, this.trainerToDelete.id));
        const trainerName = `${this.trainerToDelete.firstName} ${this.trainerToDelete.lastName}`;
        const params = { name: trainerName };
        this.snackBar.open(
          this.translate.instant('trainer.delete.success', params),
          this.translate.instant('common.close'),
          { duration: 3000 }
        );
        this.trainerToDelete = null;
        this.trainerDeleted.emit();
      } catch (error: any) {
        const trainerName = this.trainerToDelete ? `${this.trainerToDelete.firstName} ${this.trainerToDelete.lastName}` : '';
        const params = { name: trainerName };
        this.snackBar.open(
          this.translate.instant('trainer.delete.error', params),
          this.translate.instant('common.close'),
          { duration: 3000 }
        );
        this.trainerToDelete = null;
      }
    }
  }
}
