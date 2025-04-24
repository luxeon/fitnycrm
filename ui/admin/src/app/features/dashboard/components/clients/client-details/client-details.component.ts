import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { UserService, ClientDetailsResponse } from '../../../../../core/services/user.service';
import { TrainingService, TrainingCreditsSummaryResponse } from '../../../../../core/services/training.service';
import { PaymentHistoryComponent } from './payment-history/payment-history.component';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatIconModule } from '@angular/material/icon';
import { PaymentDialogComponent } from './payment-dialog.component';
import { catchError, switchMap } from 'rxjs/operators';
import { EMPTY, of } from 'rxjs';

@Component({
  selector: 'app-client-details',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    PaymentHistoryComponent,
    MatDialogModule,
    MatSnackBarModule,
    MatIconModule
  ],
  template: `
    <div class="client-details" *ngIf="client">
      <div class="client-info">
        <div class="header">
          <div class="header-content">
            <button class="return-button" (click)="onReturn()">
              <mat-icon>arrow_back</mat-icon>
              <span>{{ 'common.back' | translate }}</span>
            </button>
            <h2>{{ 'dashboard.clients.details.title' | translate:{ name: client.firstName + ' ' + client.lastName } }}</h2>
          </div>
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
            <div class="info-item" *ngIf="trainingCredits?.remainingTrainings !== undefined">
              <span class="label">{{ 'common.remainingTrainings' | translate }}</span>
              <span class="value">{{ trainingCredits?.remainingTrainings }}</span>
            </div>
            <div class="info-item" *ngIf="trainingCredits?.expiresAt">
              <span class="label">{{ 'common.subscriptionExpiration' | translate }}</span>
              <span class="value">{{ trainingCredits?.expiresAt | date }}</span>
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

      .header-content {
        display: flex;
        align-items: center;
        gap: 16px;
      }

      h2 {
        margin: 0;
        color: #2c3e50;
        font-size: 24px;
      }
    }

    .return-button {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 8px 16px;
      background: transparent;
      color: #3498db;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 14px;
      transition: all 0.2s ease;

      mat-icon {
        font-size: 20px;
        width: 20px;
        height: 20px;
        transition: transform 0.2s ease;
      }

      &:hover {
        background: rgba(52, 152, 219, 0.1);
        
        mat-icon {
          transform: translateX(-4px);
        }
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
        grid-template-columns: 180px 1fr;
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
  private router = inject(Router);
  private userService = inject(UserService);
  private trainingService = inject(TrainingService);
  private snackBar = inject(MatSnackBar);
  private translate = inject(TranslateService);
  private dialog = inject(MatDialog);

  client: ClientDetailsResponse | null = null;
  trainingCredits: TrainingCreditsSummaryResponse | null = null;
  tenantId: string = '';
  clientId: string = '';

  ngOnInit(): void {
    const tenantId = this.route.snapshot.params['tenantId'];
    const clientId = this.route.snapshot.params['clientId'];

    if (tenantId && clientId) {
      this.tenantId = tenantId;
      this.clientId = clientId;
      this.loadClientDetails();
      this.loadTrainingCredits();
    }
  }

  onReturn(): void {
    this.router.navigate(['/dashboard'], {
      queryParams: { tab: 'clients' }
    });
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

  private loadTrainingCredits(): void {
    this.trainingService.getAllTrainings(this.tenantId).pipe(
      catchError(error => {
        return EMPTY;
      }),
      switchMap(trainings => {
        if (trainings && trainings.length > 0) {
          // Use the first training's ID to get credits summary
          return this.trainingService.getTrainingCreditsSummary(this.tenantId, this.clientId, trainings[0].id);
        }
        return EMPTY;
      }),
      catchError(error => {
        return of(null);
      })
    ).subscribe(response => {
      if (response) {
        this.trainingCredits = response;
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
