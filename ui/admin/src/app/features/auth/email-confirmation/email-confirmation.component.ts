import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-email-confirmation',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    TranslateModule
  ],
  template: `
    <div class="confirmation-container">
      <div class="confirmation-card">
        <h2>{{ 'emailConfirmation.title' | translate }}</h2>
        <p class="message">{{ 'emailConfirmation.message' | translate }}</p>
        <p class="login-link">
          {{ 'emailConfirmation.login.text' | translate }} 
          <a routerLink="/login">{{ 'emailConfirmation.login.link' | translate }}</a>
        </p>
      </div>
    </div>
  `,
  styles: [`
    .confirmation-container {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      padding: 20px;
      background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    }

    .confirmation-card {
      background: white;
      padding: 40px;
      border-radius: 12px;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      width: 100%;
      max-width: 600px;
      text-align: center;
    }

    h2 {
      margin: 0 0 24px;
      color: #2c3e50;
      font-size: 28px;
    }

    .message {
      color: #4a5568;
      font-size: 16px;
      line-height: 1.5;
      margin-bottom: 32px;
    }

    .login-link {
      color: #4a5568;

      a {
        color: #3b82f6;
        text-decoration: none;
        font-weight: 500;

        &:hover {
          text-decoration: underline;
        }
      }
    }
  `]
})
export class EmailConfirmationComponent {} 