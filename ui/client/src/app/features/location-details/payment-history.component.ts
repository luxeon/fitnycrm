import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { animate, style, transition, trigger } from '@angular/animations';
import { PaymentService, PaymentPageItemResponse, PaymentStatus } from '../../core/services/payment.service';
import { AuthService } from '../../core/services/auth.service';
import { Page } from '../../core/models/page.model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-payment-history',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MatCardModule,
    MatExpansionModule,
    MatIconModule,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatChipsModule,
    MatTooltipModule,
    MatProgressSpinnerModule
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
    <div class="payment-history-container" @fadeInOut>
      <mat-expansion-panel 
        [expanded]="isExpanded"
        (opened)="panelOpened()"
        (closed)="isExpanded = false">
        <mat-expansion-panel-header>
          <mat-panel-title>
            <mat-icon class="panel-icon">receipt</mat-icon>
            {{ 'payment.history.title' | translate }}
          </mat-panel-title>
          <mat-panel-description>
            {{ isExpanded ? ('payment.history.hide' | translate) : ('payment.history.show' | translate) }}
          </mat-panel-description>
        </mat-expansion-panel-header>

        <div *ngIf="isLoading" class="loading-container">
          <mat-spinner diameter="40"></mat-spinner>
        </div>

        <div *ngIf="!isLoading && (!payments || !payments.content?.length)" class="empty-state">
          {{ 'payment.history.empty' | translate }}
        </div>

        <div *ngIf="!isLoading && payments && payments.content?.length" class="payment-table-container">
          <table mat-table [dataSource]="payments.content" class="payment-table">
            <!-- Date Column -->
            <ng-container matColumnDef="date">
              <th mat-header-cell *matHeaderCellDef>{{ 'payment.history.date' | translate }}</th>
              <td mat-cell *matCellDef="let payment">{{ payment.createdAt | date:'medium' }}</td>
            </ng-container>

            <!-- Training Column -->
            <ng-container matColumnDef="training">
              <th mat-header-cell *matHeaderCellDef>{{ 'payment.history.training' | translate }}</th>
              <td mat-cell *matCellDef="let payment">
                {{ payment.training?.name || 'Unknown Training' }}
              </td>
            </ng-container>

            <!-- Units Column -->
            <ng-container matColumnDef="units">
              <th mat-header-cell *matHeaderCellDef>{{ 'payment.history.units' | translate }}</th>
              <td mat-cell *matCellDef="let payment">
                {{ payment.trainingsCount }}
                <span *ngIf="payment.validDays" class="validity">
                  ({{ 'payment.history.valid_for' | translate }} {{ payment.validDays }} {{ 'payment.history.days' | translate }})
                </span>
              </td>
            </ng-container>

            <!-- Amount Column -->
            <ng-container matColumnDef="amount">
              <th mat-header-cell *matHeaderCellDef>{{ 'payment.history.amount' | translate }}</th>
              <td mat-cell *matCellDef="let payment">{{ payment.price | currency:payment.currency:'symbol' }}</td>
            </ng-container>

            <!-- Status Column -->
            <ng-container matColumnDef="status">
              <th mat-header-cell *matHeaderCellDef>{{ 'payment.history.status' | translate }}</th>
              <td mat-cell *matCellDef="let payment">
                <mat-chip 
                  [color]="getStatusColor(payment.status)" 
                  [matTooltip]="'payment.status.' + payment.status?.toLowerCase() | translate"
                  disableRipple>
                  {{ 'payment.status.' + payment.status?.toLowerCase() | translate }}
                </mat-chip>
              </td>
            </ng-container>

            <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
            <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
          </table>

          <mat-paginator
            *ngIf="payments.totalPages > 1"
            [length]="payments.totalElements"
            [pageSize]="pageSize"
            [pageIndex]="currentPage"
            [pageSizeOptions]="[5, 10, 25]"
            (page)="onPageChange($event)">
          </mat-paginator>
        </div>
      </mat-expansion-panel>
    </div>
  `,
  styles: [`
    .payment-history-container {
      margin-top: 24px;
      margin-bottom: 24px;
    }

    .panel-icon {
      margin-right: 8px;
    }

    .loading-container {
      display: flex;
      justify-content: center;
      padding: 24px;
    }

    .empty-state {
      text-align: center;
      padding: 24px;
      color: #6c757d;
    }

    .payment-table-container {
      overflow-x: auto;
    }

    .payment-table {
      width: 100%;
    }

    .validity {
      color: #6c757d;
      font-size: 0.9em;
      margin-left: 4px;
    }

    ::ng-deep .mat-mdc-chip.mat-mdc-standard-chip {
      --mdc-chip-label-text-color: white;
      --mdc-chip-elevated-container-color: #3498db;
      height: 24px;
      font-size: 12px;
    }

    ::ng-deep .mat-primary.mat-mdc-chip.mat-mdc-standard-chip {
      --mdc-chip-elevated-container-color: #2ecc71;
    }

    ::ng-deep .mat-warn.mat-mdc-chip.mat-mdc-standard-chip {
      --mdc-chip-elevated-container-color: #e74c3c;
    }

    ::ng-deep .mat-accent.mat-mdc-chip.mat-mdc-standard-chip {
      --mdc-chip-elevated-container-color: #f39c12;
    }
  `]
})
export class PaymentHistoryComponent implements OnInit {
  private readonly paymentService = inject(PaymentService);
  private readonly authService = inject(AuthService);
  private readonly route = inject(ActivatedRoute);

  isExpanded = false;
  isLoading = false;
  tenantId = '';
  clientId = '';
  
  payments: Page<PaymentPageItemResponse> | null = null;
  displayedColumns: string[] = ['date', 'training', 'units', 'amount', 'status'];
  currentPage = 0;
  pageSize = 5;

  ngOnInit(): void {
    const claims = this.authService.getUserClaims();
    if (claims) {
      // Get the UUID id from JWT claims
      this.clientId = claims.id;
      
      // Get tenant ID from route params
      this.route.params.subscribe(params => {
        if (params['tenantId']) {
          this.tenantId = params['tenantId'];
        }
      });
    }
  }

  panelOpened(): void {
    this.isExpanded = true;
    this.fetchPayments();
  }

  fetchPayments(): void {
    if (!this.tenantId || !this.clientId) return;
    
    this.isLoading = true;
    this.paymentService.getPayments(this.tenantId, this.clientId, this.currentPage, this.pageSize)
      .subscribe({
        next: (response) => {
          this.payments = response;
          this.isLoading = false;
        },
        error: () => {
          this.isLoading = false;
        }
      });
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.fetchPayments();
  }

  getStatusColor(status: PaymentStatus): string {
    switch (status) {
      case PaymentStatus.COMPLETED:
        return 'primary';
      case PaymentStatus.CANCELED:
        return 'warn';
      case PaymentStatus.PENDING:
        return 'accent';
      default:
        return '';
    }
  }
} 