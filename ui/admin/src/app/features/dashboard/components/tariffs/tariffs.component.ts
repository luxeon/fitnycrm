import { Component, Input, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { animate, style, transition, trigger } from '@angular/animations';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { TariffService, TariffResponse } from '../../../../core/services/tariff.service';
import { TariffDialogComponent } from './tariff-dialog.component';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { catchError, finalize } from 'rxjs/operators';
import { of } from 'rxjs';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';

@Component({
  selector: 'app-tariffs',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MatDialogModule,
    MatSnackBarModule,
    ConfirmationDialogComponent
  ],
  animations: [
    trigger('fadeInOut', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('300ms ease-out', style({ opacity: 1 }))
      ])
    ])
  ],
  template: `
    <div class="tariffs-section" @fadeInOut>
      <div class="action-header">
        <h3>{{ 'dashboard.tariffs.title' | translate }}</h3>
        <button class="action-button" (click)="onAddTariff()">
          {{ 'dashboard.tariffs.add' | translate }}
        </button>
      </div>

      <div class="loading" *ngIf="isLoading">
        {{ 'common.loading' | translate }}
      </div>

      <div class="empty-state" *ngIf="!isLoading && !tariffs?.length">
        {{ 'dashboard.tariffs.empty' | translate }}
      </div>

      <div class="tariffs-grid" *ngIf="!isLoading && tariffs?.length">
        <div class="tariff-card" *ngFor="let tariff of tariffs">
          <div class="card-actions">
            <button class="edit-btn" (click)="onEditTariff(tariff)">
              <span class="edit-icon">✎</span>
            </button>
            <button class="delete-btn" (click)="onDeleteClick(tariff)">
              <span class="delete-icon">×</span>
            </button>
          </div>
          <div class="tariff-info">
            <h4 class="name">{{ tariff.name }}</h4>
            <div class="price">{{ tariff.price | currency:tariff.currency }}</div>
            <div class="duration">{{ tariff.validDays }} {{ 'common.days' | translate }}</div>
            <div class="trainings">{{ tariff.trainingsCount }} {{ 'dashboard.tariffs.trainings' | translate }}</div>
          </div>
        </div>
      </div>
    </div>

    <app-confirmation-dialog
      *ngIf="tariffToDelete"
      [title]="'dashboard.tariffs.delete.title' | translate"
      [message]="'dashboard.tariffs.delete.message' | translate"
      [confirmText]="'dashboard.tariffs.delete.confirm' | translate"
      (confirm)="onDeleteConfirm()"
      (cancel)="onDeleteCancel()"
    ></app-confirmation-dialog>
  `,
  styles: [`
    .tariffs-section {
      padding: 0;
    }

    .action-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 24px;

      h3 {
        margin: 0;
        color: #2c3e50;
        font-size: 20px;
      }
    }

    .action-button {
      padding: 8px 16px;
      background: #3498db;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 14px;
      transition: background-color 0.2s;

      &:hover {
        background: #2980b9;
      }
    }

    .loading {
      text-align: center;
      padding: 48px;
      color: #6c757d;
      font-size: 16px;
    }

    .empty-state {
      text-align: center;
      padding: 48px;
      background: #f8f9fa;
      border-radius: 8px;
      color: #6c757d;
      font-size: 16px;
    }

    .tariffs-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 1.5rem;
      margin-bottom: 1.5rem;
    }

    .tariff-card {
      position: relative;
      padding: 1.5rem;
      background: #f8f9fa;
      border-radius: 8px;
      border: 1px solid #e9ecef;
      transition: transform 0.2s, box-shadow 0.2s;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        background: #f1f3f5;
      }

      .card-actions {
        position: absolute;
        top: 12px;
        right: 12px;
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

      .tariff-info {
        .name {
          font-size: 1.25rem;
          font-weight: 600;
          color: #2c3e50;
          margin: 0 0 0.5rem;
        }

        .price {
          font-size: 1.5rem;
          font-weight: 700;
          color: #3498db;
          margin-bottom: 0.5rem;
        }

        .duration, .trainings {
          color: #495057;
          font-size: 0.9rem;
          margin-bottom: 0.5rem;
        }
      }
    }
  `]
})
export class TariffsComponent implements OnInit {
  @Input() tenantId!: string;

  private dialog = inject(MatDialog);
  private tariffService = inject(TariffService);
  private snackBar = inject(MatSnackBar);

  isLoading = false;
  tariffs: TariffResponse[] = [];
  tariffToDelete: TariffResponse | null = null;

  ngOnInit(): void {
    this.loadTariffs();
  }

  loadTariffs(): void {
    this.isLoading = true;
    this.tariffService.findAll(this.tenantId)
      .pipe(
        catchError(() => {
          this.snackBar.open('Error loading tariffs', 'Close', { duration: 3000 });
          return of([]);
        }),
        finalize(() => this.isLoading = false)
      )
      .subscribe(tariffs => this.tariffs = tariffs);
  }

  onAddTariff(): void {
    const dialogRef = this.dialog.open(TariffDialogComponent, {
      width: '500px',
      data: {}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.tariffService.create(this.tenantId, result)
          .pipe(
            catchError(() => {
              this.snackBar.open('Error creating tariff', 'Close', { duration: 3000 });
              return of(null);
            })
          )
          .subscribe(tariff => {
            if (tariff) {
              this.snackBar.open('Tariff created successfully', 'Close', { duration: 3000 });
              this.loadTariffs();
            }
          });
      }
    });
  }

  onEditTariff(tariff: TariffResponse): void {
    const dialogRef = this.dialog.open(TariffDialogComponent, {
      width: '500px',
      data: { tariff }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.tariffService.update(this.tenantId, tariff.id, result)
          .pipe(
            catchError(() => {
              this.snackBar.open('Error updating tariff', 'Close', { duration: 3000 });
              return of(null);
            })
          )
          .subscribe(updatedTariff => {
            if (updatedTariff) {
              this.snackBar.open('Tariff updated successfully', 'Close', { duration: 3000 });
              this.loadTariffs();
            }
          });
      }
    });
  }

  onDeleteClick(tariff: TariffResponse): void {
    this.tariffToDelete = tariff;
  }

  onDeleteCancel(): void {
    this.tariffToDelete = null;
  }

  onDeleteConfirm(): void {
    if (this.tariffToDelete) {
      this.tariffService.delete(this.tenantId, this.tariffToDelete.id)
        .pipe(
          catchError(() => {
            this.snackBar.open('Error deleting tariff', 'Close', { duration: 3000 });
            return of(null);
          })
        )
        .subscribe(() => {
          this.snackBar.open('Tariff deleted successfully', 'Close', { duration: 3000 });
          this.tariffToDelete = null;
          this.loadTariffs();
        });
    }
  }
}
