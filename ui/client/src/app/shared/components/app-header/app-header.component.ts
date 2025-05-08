import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import {AuthService} from '../../../core/services/auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [MatIconModule, MatButtonModule],
  template: `
    <header class="app-header">
      <span class="app-title">FITNYAPP</span>
      <button mat-icon-button aria-label="Logout" (click)="logout()">
        <mat-icon>logout</mat-icon>
      </button>
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
      height: 56px;
    }
    .app-title {
      font-size: 1.5rem;
      font-weight: 600;
    }
    button {
      color: #fff;
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
