import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-confirm-email',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    TranslateModule
  ],
  template: `
    <div class="confirmation-container">
      <div class="confirmation-card">
        <div *ngIf="isLoading">
          <h2>{{ 'confirmEmail.loading.title' | translate }}</h2>
          <p class="message">{{ 'confirmEmail.loading.message' | translate }}</p>
        </div>
        
        <div *ngIf="!isLoading && !error">
          <h2>{{ 'confirmEmail.success.title' | translate }}</h2>
          <p class="message">{{ 'confirmEmail.success.message' | translate }}</p>
          <p class="login-link">
            {{ 'confirmEmail.success.login.text' | translate }} 
            <a routerLink="/login">{{ 'confirmEmail.success.login.link' | translate }}</a>
          </p>
        </div>

        <div *ngIf="!isLoading && error">
          <h2>{{ 'confirmEmail.error.title' | translate }}</h2>
          <p class="message">{{ error | translate }}</p>
          <p class="login-link">
            {{ 'confirmEmail.error.retry.text' | translate }} 
            <a routerLink="/login">{{ 'confirmEmail.error.retry.link' | translate }}</a>
          </p>
        </div>
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
export class ConfirmEmailComponent implements OnInit {
  isLoading = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token');
    
    if (!token) {
      this.isLoading = false;
      this.error = 'confirmEmail.error.noToken';
      return;
    }

    this.authService.confirmEmail(token).subscribe({
      next: () => {
        this.isLoading = false;
      },
      error: (error) => {
        this.isLoading = false;
        this.error = error.error?.message || 'confirmEmail.error.generic';
      }
    });
  }
}
