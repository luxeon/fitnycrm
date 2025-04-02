import { Component, EventEmitter, Input, Output, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-confirmation-dialog',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  template: `
    <div class="dialog-overlay" (click)="onCancel()">
      <div class="dialog-content" (click)="$event.stopPropagation()">
        <h3>{{ title | translate }}</h3>
        <p>{{ message | translate }}</p>
        <div class="dialog-actions">
          <button class="btn-secondary" (click)="onCancel()">
            {{ 'common.cancel' | translate }}
          </button>
          <button class="btn-danger" (click)="onConfirm()">
            {{ confirmText | translate }}
          </button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .dialog-overlay {
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

    .dialog-content {
      background: white;
      padding: 24px;
      border-radius: 8px;
      min-width: 320px;
      max-width: 90%;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);

      h3 {
        margin: 0 0 16px;
        color: #2c3e50;
      }

      p {
        margin: 0 0 24px;
        color: #34495e;
      }
    }

    .dialog-actions {
      display: flex;
      justify-content: flex-end;
      gap: 12px;

      button {
        padding: 8px 16px;
        border: none;
        border-radius: 4px;
        font-size: 14px;
        cursor: pointer;
        transition: background-color 0.2s;

        &.btn-secondary {
          background: #95a5a6;
          color: white;

          &:hover {
            background: #7f8c8d;
          }
        }

        &.btn-danger {
          background: #e74c3c;
          color: white;

          &:hover {
            background: #c0392b;
          }
        }
      }
    }
  `]
})
export class ConfirmationDialogComponent {
  @Input() title = '';
  @Input() message = '';
  @Input() confirmText = '';
  @Output() confirm = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();

  @HostListener('document:keydown.escape')
  onEscapePressed(): void {
    this.onCancel();
  }

  onConfirm(): void {
    this.confirm.emit();
  }

  onCancel(): void {
    this.cancel.emit();
  }
} 