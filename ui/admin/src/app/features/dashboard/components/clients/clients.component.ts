import { Component, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { InviteUserModalComponent } from '../invite-user-modal/invite-user-modal.component';
import { UserService } from '../../../../core/services/user.service';
import { animate, style, transition, trigger } from '@angular/animations';

@Component({
  selector: 'app-clients',
  standalone: true,
  imports: [CommonModule, TranslateModule, InviteUserModalComponent],
  animations: [
    trigger('fadeInOut', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('300ms ease-out', style({ opacity: 1 }))
      ]),
      transition(':leave', [
        animate('300ms ease-in', style({ opacity: 0 }))
      ])
    ])
  ],
  template: `
    <div class="clients-section" @fadeInOut>
      <div class="action-header">
        <h3>{{ 'dashboard.clients.title' | translate }}</h3>
        <button class="action-button" (click)="showInviteModal = true">
          {{ 'dashboard.clients.invite.button.text' | translate }}
        </button>
      </div>

      <div class="empty-state">
        {{ 'dashboard.clients.empty' | translate }}
      </div>

      <app-invite-user-modal
        *ngIf="showInviteModal"
        (close)="showInviteModal = false"
        (invite)="onInviteUser($event)">
      </app-invite-user-modal>
    </div>
  `,
  styles: [`
    .clients-section {
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

    .empty-state {
      text-align: center;
      padding: 48px;
      background: #f8f9fa;
      border-radius: 8px;
      color: #6c757d;
      font-size: 16px;
    }
  `]
})
export class ClientsComponent {
  @Input() tenantId!: string;

  private readonly userService = inject(UserService);

  showInviteModal = false;

  onInviteUser(email: string): void {
    this.userService.inviteUser(this.tenantId, email)
      .subscribe({
        next: () => {
          this.showInviteModal = false;
          // TODO: Show success notification
        },
        error: (error) => {
          console.error('Error inviting user:', error);
          // TODO: Show error notification
        }
      });
  }
}
