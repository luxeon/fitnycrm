import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Router, RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { LanguageSwitcherComponent } from '../language-switcher/language-switcher.component';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    CommonModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    RouterModule,
    TranslateModule,
    LanguageSwitcherComponent
  ],
  template: `
    <mat-toolbar color="primary">
      <div class="toolbar-container">
        <div class="logo-section">
          <a routerLink="/dashboard">
            <span class="app-title">FitNYC Admin</span>
          </a>
        </div>
        
        <div class="actions-section">
          <app-language-switcher></app-language-switcher>
          
          <button mat-icon-button 
                  (click)="logout()" 
                  aria-label="Logout" 
                  [matTooltip]="'header.logout' | translate">
            <mat-icon>exit_to_app</mat-icon>
          </button>
        </div>
      </div>
    </mat-toolbar>
  `,
  styles: [`
    .toolbar-container {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 100%;
      padding: 0 16px;
    }
    
    .logo-section {
      display: flex;
      align-items: center;
      
      a {
        text-decoration: none;
        color: white;
      }
      
      .app-title {
        font-size: 1.2rem;
        font-weight: 500;
      }
    }
    
    .actions-section {
      display: flex;
      align-items: center;
      gap: 16px;
    }
  `]
})
export class AppHeaderComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  
  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
} 