import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../../core/services/auth.service';
import { TranslateModule } from '@ngx-translate/core';
import { LanguageSwitcherComponent } from '../language-switcher/language-switcher.component';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [MatIconModule, MatButtonModule, TranslateModule, LanguageSwitcherComponent],
  template: `
    <header class="app-header">
      <span class="app-title">FITAVERA APP</span>
      <div class="actions-section">
        <app-language-switcher></app-language-switcher>
        <button mat-icon-button aria-label="Logout" (click)="logout()">
          <mat-icon>logout</mat-icon>
        </button>
      </div>
    </header>
  `,
  styles: [`
    .app-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      background: #3f51b5;
      color: #fff;
      padding: 0 24px;
      height: 60px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }

    .app-title {
      font-size: 1.5rem;
      font-weight: 600;
      letter-spacing: 0.5px;
    }

    button {
      color: #fff;
    }

    .actions-section {
      display: flex;
      align-items: center;
      gap: 16px;
    }
  `]
})
export class AppHeaderComponent {
  private router = inject(Router);
  private authService = inject(AuthService);


  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
