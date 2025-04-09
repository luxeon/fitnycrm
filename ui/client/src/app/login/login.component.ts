import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AuthService } from '../core/services/auth.service';
import { InvitationStorageService } from '../core/services/invitation-storage.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private invitationStorage = inject(InvitationStorageService);

  loginForm: FormGroup;
  isLoading = false;
  hidePassword = true;
  errorMessage = '';
  hasPendingInvitation = false;

  constructor() {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });

    // Check if there's a pending invitation
    this.hasPendingInvitation = this.invitationStorage.hasPendingInvitation();
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.authService.login(this.loginForm.value).subscribe({
      next: () => {
        this.isLoading = false;
        this.handleSuccessfulLogin();
      },
      error: (error) => {
        this.isLoading = false;
        if (error.status === 401) {
          this.errorMessage = 'Invalid email or password';
        } else {
          this.errorMessage = 'An error occurred. Please try again later.';
        }
        console.error('Login error:', error);
      }
    });
  }

  private handleSuccessfulLogin(): void {
    // Check if there's a pending invitation
    if (this.invitationStorage.hasPendingInvitation()) {
      const invitation = this.invitationStorage.getStoredInvitation();
      if (invitation) {
        // Navigate to the invitation handler component
        this.router.navigate([
          '/tenants', invitation.tenantId,
          'clients', 'signup', invitation.inviteId
        ]);
        return;
      }
    }

    // If no pending invitation, navigate to dashboard
    this.router.navigate(['/dashboard']);
  }
}
