import { Injectable, inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { TranslateService } from '@ngx-translate/core';

export interface ErrorMessage {
  message: string;
  isLocalized: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {
  private readonly translate = inject(TranslateService);

  /**
   * Processes HTTP error and returns a user-friendly localized message
   */
  processError(error: HttpErrorResponse, context?: string): ErrorMessage {
    // Handle specific error cases
    if (error.status === 409 && error.error?.message) {
      return this.handleConflictError(error.error.message, context);
    }

    if (error.status === 400 && error.error?.message) {
      return this.handleBadRequestError(error.error.message, context);
    }

    if (error.status === 404) {
      return {
        message: this.translate.instant('common.error.notFound'),
        isLocalized: true
      };
    }

    if (error.status === 403) {
      return {
        message: this.translate.instant('common.error.forbidden'),
        isLocalized: true
      };
    }

    if (error.status === 500) {
      return {
        message: this.translate.instant('common.error.serverError'),
        isLocalized: true
      };
    }

    // Default error message
    return {
      message: this.translate.instant('common.error.generic'),
      isLocalized: true
    };
  }

  private handleConflictError(backendMessage: string, context?: string): ErrorMessage {
    // Check if it's an email already exists error
    if (backendMessage.includes('already exists')) {
      if (context === 'trainer') {
        return {
          message: this.translate.instant('trainer.create.error.emailExists'),
          isLocalized: true
        };
      }
      // Generic email exists error
      return {
        message: this.translate.instant('common.error.emailExists'),
        isLocalized: true
      };
    }

    // Return the backend message as fallback
    return {
      message: backendMessage,
      isLocalized: false
    };
  }

  private handleBadRequestError(backendMessage: string, context?: string): ErrorMessage {
    // For validation errors, we might want to show the backend message
    // or map it to a localized one based on context
    return {
      message: backendMessage,
      isLocalized: false
    };
  }

  /**
   * Extracts email from backend error message if present
   */
  extractEmailFromErrorMessage(message: string): string | null {
    const emailRegex = /([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,})/;
    const match = message.match(emailRegex);
    return match ? match[1] : null;
  }
} 