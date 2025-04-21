import { Injectable, inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private readonly snackBar = inject(MatSnackBar);
  private readonly translate = inject(TranslateService);

  showSuccess(message: string): void {
    this.snackBar.open(
      this.translate.instant(message),
      this.translate.instant('common.close'),
      {
        duration: 5000,
        horizontalPosition: 'center',
        verticalPosition: 'bottom',
        panelClass: ['success-snackbar', 'notification-snackbar']
      }
    );
  }

  showError(message: string): void {
    this.snackBar.open(
      this.translate.instant(message),
      this.translate.instant('common.close'),
      {
        duration: 5000,
        horizontalPosition: 'center',
        verticalPosition: 'bottom',
        panelClass: ['error-snackbar', 'notification-snackbar']
      }
    );
  }
} 