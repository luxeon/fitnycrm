import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClientSignupService, SignupClientRequest } from '../core/services/client-signup.service';
import { InvitationStorageService } from '../core/services/invitation-storage.service';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-client-signup',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    TranslateModule
  ],
  templateUrl: './client-signup.component.html',
  styleUrls: ['./client-signup.component.scss']
})
export class ClientSignupComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private clientSignupService = inject(ClientSignupService);
  private invitationStorage = inject(InvitationStorageService);

  tenantId: string = '';
  inviteId: string = '';

  signupForm: FormGroup;

  isSubmitting = false;
  submitError: string | null = null;
  submitSuccess = false;
  hidePassword = true;
  hideConfirmPassword = true;

  ngOnInit(): void {
    // First check URL parameters
    this.route.paramMap.subscribe(params => {
      const tenantIdParam = params.get('tenantId');
      const inviteIdParam = params.get('inviteId');

      if (tenantIdParam) this.tenantId = tenantIdParam;
      if (inviteIdParam) this.inviteId = inviteIdParam;
    });

    // Then check if we have stored invitation data
    if (!this.route.snapshot.paramMap.has('tenantId') && this.invitationStorage.hasPendingInvitation()) {
      const invitation = this.invitationStorage.getStoredInvitation();
      if (invitation) {
        this.tenantId = invitation.tenantId;
        this.inviteId = invitation.inviteId;
      }
    }

    // Initialize the form
    this.signupForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      phoneNumber: ['', [Validators.pattern(/^\+[1-9]\d{1,14}$/)]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', [Validators.required]]
    }, {
      validators: this.passwordMatchValidator
    });
  }

  constructor() {
    this.signupForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(255)]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(255)]],
      phoneNumber: ['', [Validators.pattern('^\\+?[1-9]\\d{1,14}$')]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(255)]],
      confirmPassword: ['', [Validators.required]]
    }, {
      validators: this.passwordMatchValidator
    });
  }

  passwordMatchValidator(formGroup: FormGroup) {
    const password = formGroup.get('password')?.value;
    const confirmPassword = formGroup.get('confirmPassword')?.value;

    if (password !== confirmPassword) {
      formGroup.get('confirmPassword')?.setErrors({ passwordMismatch: true });
      return { passwordMismatch: true };
    } else {
      formGroup.get('confirmPassword')?.setErrors(null);
      return null;
    }
  }

  onSubmit() {
    if (this.signupForm.invalid) {
      this.markFormGroupTouched(this.signupForm);
      return;
    }

    this.isSubmitting = true;
    this.submitError = null;

    const request: SignupClientRequest = {
      firstName: this.signupForm.get('firstName')!.value,
      lastName: this.signupForm.get('lastName')!.value,
      phoneNumber: this.signupForm.get('phoneNumber')!.value,
      password: this.signupForm.get('password')!.value
    };

    this.clientSignupService.signup(this.tenantId, this.inviteId, request)
      .subscribe({
        next: () => {
          this.isSubmitting = false;
          this.submitSuccess = true;

          // Clear any stored invitation data
          this.invitationStorage.clearStoredInvitation();

          // Redirect to login page after 3 seconds
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 3000);
        },
        error: (error) => {
          this.isSubmitting = false;

          if (error.status === 404) {
            this.submitError = 'The invitation was not found or has expired.';
          } else if (error.status === 400) {
            this.submitError = 'Please check your information and try again.';
          } else {
            this.submitError = 'An error occurred during signup. Please try again later.';
          }
        }
      });
  }

  // Helper method to mark all controls as touched
  private markFormGroupTouched(formGroup: FormGroup) {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();

      if ((control as any).controls) {
        this.markFormGroupTouched(control as FormGroup);
      }
    });
  }

  // Helper getters for form validation
  get firstNameControl() { return this.signupForm.get('firstName'); }
  get lastNameControl() { return this.signupForm.get('lastName'); }
  get phoneNumberControl() { return this.signupForm.get('phoneNumber'); }
  get passwordControl() { return this.signupForm.get('password'); }
  get confirmPasswordControl() { return this.signupForm.get('confirmPassword'); }
}
