import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { RegistrationComponent } from './features/auth/registration/registration.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RegistrationComponent],
  template: '<app-registration></app-registration>',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'ui';
}
