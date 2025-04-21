import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { UserService, ClientDetailsResponse } from '../../../../../core/services/user.service';
import { PaymentHistoryComponent } from './payment-history/payment-history.component';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { PaymentDialogComponent } from './payment-dialog.component';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-client-details',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    PaymentHistoryComponent,
    MatDialogModule,
    MatSnackBarModule
  ],
  template: `
    <div class="client-details" *ngIf="client">
      <div class="client-info">
        <div class="header">
          <h2>{{ 'dashboard.clients.details.title' | translate:{ name: client.firstName + ' ' + client.lastName } }}</h2>
        </div>

        <div class="content">
          <div class="info-section">
            <div class="info-item">
              <span class="label">{{ 'common.email' | translate }}</span>
              <span class="value">{{ client.email }}</span>
            </div>
            <div class="info-item">
              <span class="label">{{ 'common.phone' | translate }}</span>
              <span class="value">{{ client.phoneNumber || '-' }}</span>
            </div>
            <div class="info-item">
              <span class="label">{{ 'common.joinedAt' | translate }}</span>
              <span class="value">{{ client.createdAt | date }}</span>
            </div>
          </div>
        </div>
      </div>

      <app-payment-history
        [tenantId]="tenantId"
        [clientId]="client.id">
      </app-payment-history>
    </div>
  `,
  styles: [`
    .client-details {
      padding: 24px;
      background: white;
      border-radius: 8px;
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    }

    .header {
      margin-bottom: 24px;

      h2 {
        margin: 0;
        color: #2c3e50;
        font-size: 24px;
      }
    }

    .content {
      .info-section {
        display: grid;
        gap: 16px;
        margin-bottom: 32px;
      }

      .info-item {
        display: grid;
        grid-template-columns: 120px 1fr;
        gap: 16px;
        align-items: center;
        padding: 12px;
        background: #f8f9fa;
        border-radius: 4px;

        .label {
          color: #6c757d;
          font-weight: 500;
        }

        .value {
          color: #2c3e50;
        }
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
  `]
})
export class ClientDetailsComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private userService = inject(UserService);
  private snackBar = inject(MatSnackBar);
  private translate = inject(TranslateService);
  private dialog = inject(MatDialog);

  client: ClientDetailsResponse | null = null;
  tenantId: string = '';
  clientId: string = '';

  ngOnInit(): void {
    const tenantId = this.route.snapshot.params['tenantId'];
    const clientId = this.route.snapshot.params['clientId'];

    if (tenantId && clientId) {
      this.tenantId = tenantId;
      this.clientId = clientId;
      this.loadClientDetails();
    }
  }

  private loadClientDetails(): void {
    this.userService.getClientById(this.tenantId, this.clientId).pipe(
      catchError(error => {
        console.error('Error loading client details:', error);
        this.snackBar.open(
          this.translate.instant('dashboard.clients.details.error.loading'),
          this.translate.instant('common.close'),
          { duration: 5000 }
        );
        return of(null);
      })
    ).subscribe(response => {
      if (response) {
        this.client = response;
      }
    });
  }

  onAddPayment(): void {
    const dialogRef = this.dialog.open(PaymentDialogComponent, {
      width: '500px',
      data: {
        tenantId: this.tenantId,
        clientId: this.clientId,
        clientName: this.client ? `${this.client.firstName} ${this.client.lastName}` : ''
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // Refresh the payment history component
        const paymentHistory = document.querySelector('app-payment-history');
        if (paymentHistory) {
          (paymentHistory as any).loadPayments();
        }
      }
    });
  }
}
