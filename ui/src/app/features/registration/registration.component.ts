import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService, UserSignupRequest } from '../../core/services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent {
  registrationForm: FormGroup;
  errorMessage: string | null = null;
  isLoading = false;

  private readonly passwordPattern = '^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$';
  private readonly phonePattern = '^\\+[1-9]\\d{7,14}$';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registrationForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(255)]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(255)]],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.pattern(this.phonePattern)]],
      password: ['', [Validators.required, Validators.pattern(this.passwordPattern)]],
      confirmPassword: ['', [Validators.required]]
    }, {
      validators: this.passwordMatchValidator
    });
  }

  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password');
    const confirmPassword = form.get('confirmPassword');

    if (password && confirmPassword && password.value !== confirmPassword.value) {
      confirmPassword.setErrors({ passwordMismatch: true });
    } else {
      confirmPassword?.setErrors(null);
    }
  }

  onSubmit() {
    if (this.registrationForm.valid) {
      this.isLoading = true;
      this.errorMessage = null;

      const formValue = this.registrationForm.value;
      const request: UserSignupRequest = {
        firstName: formValue.firstName,
        lastName: formValue.lastName,
        email: formValue.email,
        password: formValue.password,
        phoneNumber: formValue.phoneNumber || undefined
      };

      this.authService.signup(request).subscribe({
        next: () => {
          this.isLoading = false;
          this.router.navigate(['/login'], { 
            queryParams: { 
              message: 'Registration successful! Please check your email to confirm your account.' 
            }
          });
        },
        error: (error: HttpErrorResponse) => {
          this.isLoading = false;
          if (error.status === 409) {
            this.errorMessage = 'An account with this email already exists.';
          } else if (error.error?.message) {
            this.errorMessage = error.error.message;
          } else {
            this.errorMessage = 'An error occurred during registration. Please try again.';
          }
        }
      });
    }
  }

  getErrorMessage(controlName: string): string {
    const control = this.registrationForm.get(controlName);
    if (!control?.errors || !control.touched) return '';

    if (control.errors['required']) return `${controlName} is required`;
    if (control.errors['email']) return 'Please enter a valid email';
    if (control.errors['minlength']) return `${controlName} must be at least ${control.errors['minlength'].requiredLength} characters`;
    if (control.errors['maxlength']) return `${controlName} cannot exceed ${control.errors['maxlength'].requiredLength} characters`;
    if (control.errors['pattern']) {
      if (controlName === 'password') {
        return 'Password must contain at least one digit, one lowercase, one uppercase, one special character and no whitespace';
      }
      if (controlName === 'phoneNumber') {
        return 'Phone number must be in E.164 format (e.g., +12125551234) with 8-15 digits including country code';
      }
    }
    if (control.errors['passwordMismatch']) return 'Passwords do not match';

    return 'Invalid input';
  }
}
