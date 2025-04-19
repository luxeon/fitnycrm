import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { UserService, ClientDetailsResponse } from '../../../../../core/services/user.service';

@Component({
  selector: 'app-client-details',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  template: `
    <div class="client-details" *ngIf="client">
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
  `]
})
export class ClientDetailsComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private userService = inject(UserService);

  client: ClientDetailsResponse | null = null;

  ngOnInit(): void {
    const tenantId = this.route.snapshot.params['tenantId'];
    const clientId = this.route.snapshot.params['clientId'];
    
    if (tenantId && clientId) {
      this.userService.getClientById(tenantId, clientId).subscribe({
        next: (response: ClientDetailsResponse) => {
          this.client = response;
        },
        error: (error: Error) => {
          console.error('Error loading client details:', error);
        }
      });
    }
  }
} 