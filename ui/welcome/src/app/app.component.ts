import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Fitavera - Fitness Management Platform';

  currentYear = new Date().getFullYear();

  navigateToAdmin(): void {
    window.location.href = '/admin/';
  }

  navigateToClient(): void {
    window.location.href = '/client/';
  }
}
