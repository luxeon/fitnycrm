import { Component, Input, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { PaymentService, PaymentPageItemResponse } from '../../../../../../core/services/payment.service';
import { NotificationService } from '../../../../../../core/services/notification.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { PaymentDialogComponent } from '../payment-dialog.component';

@Component({
  selector: 'app-payment-history',
  standalone: true,
  imports: [CommonModule, TranslateModule, MatDialogModule],
  template: `
    <div class="payment-history">
      <div class="header">
        <h3>{{ 'dashboard.clients.details.payments.title' | translate }}</h3>
        <button class="action-button" (click)="onAddPayment()">
          {{ 'dashboard.clients.details.payments.add.button' | translate }}
        </button>
      </div>

      <div class="loading" *ngIf="isLoading">
        {{ 'common.loading' | translate }}
      </div>

      <div class="empty-state" *ngIf="!isLoading && !payments?.length">
        {{ 'dashboard.clients.details.payments.empty' | translate }}
      </div>

      <div class="payments-list" *ngIf="!isLoading && payments?.length">
        <table>
          <thead>
            <tr>
              <th>{{ 'common.status' | translate }}</th>
              <th>{{ 'common.trainingsCount' | translate }}</th>
              <th>{{ 'common.validDays' | translate }}</th>
              <th>{{ 'common.price' | translate }}</th>
              <th>{{ 'common.createdAt' | translate }}</th>
              <th>{{ 'common.actions' | translate }}</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let payment of payments">
              <td>
                <span class="status-badge" [class]="payment.status.toLowerCase()">
                  {{ 'payment.status.' + payment.status.toLowerCase() | translate }}
                </span>
              </td>
              <td>{{ payment.trainingsCount }}</td>
              <td>{{ payment.validDays }}</td>
              <td>{{ payment.price }} {{ payment.currency }}</td>
              <td>{{ payment.createdAt | date }}</td>
              <td>
                <button
                  *ngIf="payment.status !== 'CANCELED'"
                  class="cancel-button"
                  (click)="onCancelPayment(payment.id)">
                  {{ 'common.cancel' | translate }}
                </button>
              </td>
            </tr>
          </tbody>
        </table>

        <div class="pagination" *ngIf="totalPages > 1">
          <button
            class="pagination-button"
            [disabled]="currentPage === 0"
            (click)="onPageChange(currentPage - 1)">
            {{ 'common.previous' | translate }}
          </button>
          <span class="page-info">
            {{ 'common.page' | translate }} {{ currentPage + 1 }} {{ 'common.of' | translate }} {{ totalPages }}
          </span>
          <button
            class="pagination-button"
            [disabled]="currentPage === totalPages - 1"
            (click)="onPageChange(currentPage + 1)">
            {{ 'common.next' | translate }}
          </button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .payment-history {
      margin-top: 24px;
      background: white;
      border-radius: 8px;
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
      padding: 24px;
    }

    .header {
      margin-bottom: 24px;
      display: flex;
      justify-content: space-between;
      align-items: center;

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

    .payments-list {
      table {
        width: 100%;
        border-collapse: collapse;

        th, td {
          padding: 12px 16px;
          text-align: left;
          border-bottom: 1px solid #eee;
        }

        th {
          background: #f8f9fa;
          font-weight: 600;
          color: #2c3e50;
        }

        td {
          color: #2c3e50;
        }

        tr:last-child td {
          border-bottom: none;
        }
      }
    }

    .status-badge {
      display: inline-block;
      padding: 4px 8px;
      border-radius: 4px;
      font-size: 12px;
      font-weight: 500;
      text-transform: uppercase;

      &.completed {
        background: #e8f5e9;
        color: #2e7d32;
      }

      &.canceled {
        background: #ffebee;
        color: #c62828;
      }
    }

    .cancel-button {
      padding: 4px 8px;
      background: #ffebee;
      color: #c62828;
      border: 1px solid #ef9a9a;
      border-radius: 4px;
      cursor: pointer;
      font-size: 12px;
      transition: all 0.2s;

      &:hover {
        background: #ffcdd2;
      }
    }

    .pagination {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 16px;
      margin-top: 24px;

      .pagination-button {
        padding: 8px 16px;
        background: #f8f9fa;
        border: 1px solid #dee2e6;
        border-radius: 4px;
        color: #2c3e50;
        cursor: pointer;
        transition: all 0.2s;

        &:hover:not(:disabled) {
          background: #e9ecef;
        }

        &:disabled {
          opacity: 0.5;
          cursor: not-allowed;
        }
      }

      .page-info {
        color: #6c757d;
        font-size: 14px;
      }
    }
  `]
})
export class PaymentHistoryComponent implements OnInit {
  @Input() tenantId!: string;
  @Input() clientId!: string;

  private paymentService = inject(PaymentService);
  private notificationService = inject(NotificationService);
  private dialog = inject(MatDialog);

  isLoading = false;
  payments: PaymentPageItemResponse[] = [];
  currentPage = 0;
  totalPages = 0;

  ngOnInit(): void {
    this.loadPayments();
  }

  loadPayments(page: number = 0): void {
    this.isLoading = true;
    this.paymentService.getClientPayments(this.tenantId, this.clientId, page)
      .subscribe({
        next: (response) => {
          this.payments = response.content;
          this.currentPage = response.number;
          this.totalPages = response.totalPages;
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error loading payments:', error);
          this.isLoading = false;
        }
      });
  }

  onPageChange(page: number): void {
    this.loadPayments(page);
  }

  onCancelPayment(paymentId: string): void {
    this.paymentService.cancelPayment(this.tenantId, this.clientId, paymentId)
      .subscribe({
        next: () => {
          this.loadPayments(this.currentPage);
          this.notificationService.showSuccess('dashboard.clients.details.payments.cancel.success');
        },
        error: (error) => {
          console.error('Error canceling payment:', error);
          this.notificationService.showError('dashboard.clients.details.payments.cancel.error');
        }
      });
  }

  onAddPayment(): void {
    const dialogRef = this.dialog.open(PaymentDialogComponent, {
      width: '500px',
      data: {
        tenantId: this.tenantId,
        clientId: this.clientId
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.paymentService.createPayment(this.tenantId, this.clientId, result)
          .subscribe({
            next: () => {
              this.loadPayments(this.currentPage);
              this.notificationService.showSuccess('dashboard.clients.details.payments.add.success');
            },
            error: (error) => {
              this.notificationService.showError('dashboard.clients.details.payments.add.error');
            }
          });
      }
    });
  }
}
