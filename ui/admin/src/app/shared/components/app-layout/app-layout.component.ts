import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { AppHeaderComponent } from '../app-header/app-header.component';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, AppHeaderComponent],
  template: `
    <div class="app-layout">
      <app-header></app-header>
      <div class="content">
        <router-outlet></router-outlet>
      </div>
    </div>
  `,
  styles: [`
    .app-layout {
      display: flex;
      flex-direction: column;
      min-height: 100vh;
    }
    
    .content {
      flex: 1;
    }
  `]
})
export class AppLayoutComponent {} 