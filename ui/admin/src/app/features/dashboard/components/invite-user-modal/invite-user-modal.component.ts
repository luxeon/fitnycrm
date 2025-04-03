import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { FormsModule } from '@angular/forms';
import { animate, style, transition, trigger } from '@angular/animations';

@Component({
  selector: 'app-invite-user-modal',
  standalone: true,
  imports: [CommonModule, TranslateModule, FormsModule],
  animations: [
    trigger('fadeInOut', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(-20px)' }),
        animate('300ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ]),
      transition(':leave', [
        animate('300ms ease-in', style({ opacity: 0, transform: 'translateY(-20px)' }))
      ])
    ])
  ],
  template: `
    <div class="modal-overlay">
      <div class="modal-content" @fadeInOut>
        <h2>{{ 'dashboard.clients.invite.title' | translate }}</h2>
        <div class="form-content">
          <div class="form-group">
            <label for="email">{{ 'dashboard.clients.invite.email' | translate }}</label>
            <input
              type="email"
              id="email"
              [(ngModel)]="email"
              placeholder="{{ 'dashboard.clients.invite.emailPlaceholder' | translate }}"
              class="form-control"
            >
          </div>
        </div>
        <div class="modal-actions">
          <button class="cancel-button" (click)="onCancel()">
            {{ 'common.cancel' | translate }}
          </button>
          <button 
            class="invite-button" 
            [disabled]="!isValidEmail()"
            (click)="onInvite()">
            {{ 'dashboard.clients.invite.send' | translate }}
          </button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .modal-overlay {
      position: fixed;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: rgba(0, 0, 0, 0.5);
      display: flex;
      align-items: center;
      justify-content: center;
      z-index: 1000;
    }

    .modal-content {
      background: white;
      padding: 32px;
      border-radius: 8px;
      width: 100%;
      max-width: 400px;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);

      h2 {
        margin: 0 0 24px;
        color: #2c3e50;
        font-size: 20px;
      }
    }

    .form-content {
      margin-bottom: 24px;
    }

    .form-group {
      width: 100%;

      label {
        display: block;
        margin-bottom: 8px;
        color: #2c3e50;
        font-size: 14px;
      }

      .form-control {
        width: 100%;
        padding: 8px 12px;
        border: 1px solid #ddd;
        border-radius: 4px;
        font-size: 14px;
        box-sizing: border-box;

        &:focus {
          outline: none;
          border-color: #3498db;
        }
      }
    }

    .modal-actions {
      display: flex;
      justify-content: flex-end;
      gap: 12px;

      button {
        padding: 8px 16px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 14px;
        transition: background-color 0.2s;

        &:disabled {
          opacity: 0.5;
          cursor: not-allowed;
        }
      }
    }

    .cancel-button {
      background: #e9ecef;
      color: #495057;

      &:hover:not(:disabled) {
        background: #dee2e6;
      }
    }

    .invite-button {
      background: #3498db;
      color: white;

      &:hover:not(:disabled) {
        background: #2980b9;
      }
    }
  `]
})
export class InviteUserModalComponent {
  @Output() close = new EventEmitter<void>();
  @Output() invite = new EventEmitter<string>();

  email: string = '';

  onCancel(): void {
    this.close.emit();
  }

  onInvite(): void {
    if (this.isValidEmail()) {
      this.invite.emit(this.email);
    }
  }

  isValidEmail(): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(this.email);
  }
} 