import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService, UserSignupRequest } from '../../core/services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { TranslateService, TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    TranslateModule
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
    private router: Router,
    private translate: TranslateService
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
              message: this.translate.instant('registration.success')
            }
          });
        },
        error: (error: HttpErrorResponse) => {
          this.isLoading = false;
          if (error.status === 409) {
            this.errorMessage = this.translate.instant('registration.error.emailExists');
          } else if (error.error?.message) {
            this.errorMessage = error.error.message;
          } else {
            this.errorMessage = this.translate.instant('registration.error.generic');
          }
        }
      });
    }
  }

  getErrorMessage(controlName: string): string {
    const control = this.registrationForm.get(controlName);
    if (!control?.errors || !control.touched) return '';

    if (control.errors['required']) {
      return this.translate.instant(`registration.form.${controlName}.required`);
    }
    if (control.errors['email']) {
      return this.translate.instant('registration.form.email.invalid');
    }
    if (control.errors['minlength']) {
      return this.translate.instant(`registration.form.${controlName}.minLength`, {
        length: control.errors['minlength'].requiredLength
      });
    }
    if (control.errors['maxlength']) {
      return this.translate.instant(`registration.form.${controlName}.maxLength`, {
        length: control.errors['maxlength'].requiredLength
      });
    }
    if (control.errors['pattern']) {
      return this.translate.instant(`registration.form.${controlName}.pattern`);
    }
    if (control.errors['passwordMismatch']) {
      return this.translate.instant('registration.form.confirmPassword.mismatch');
    }

    return this.translate.instant('common.error.invalid');
  }
}
