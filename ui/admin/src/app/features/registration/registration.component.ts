import { Component, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService, UserSignupRequest } from '../../core/services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { TranslateService, TranslateModule } from '@ngx-translate/core';
import { LanguageSwitcherComponent } from '../../shared/components/language-switcher/language-switcher.component';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    TranslateModule,
    LanguageSwitcherComponent
  ],
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnDestroy {
  registrationForm: FormGroup;
  errorMessage: string | null = null;
  isLoading = false;
  private destroy$ = new Subject<void>();

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
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(this.passwordPattern)
      ]],
      confirmPassword: ['', [Validators.required]],
      locale: [this.translate.currentLang || 'en', [Validators.required]]
    }, {
      validators: this.passwordMatchValidator
    });

    // Subscribe to phone number changes to auto-remove spaces
    this.registrationForm.get('phoneNumber')?.valueChanges
      .pipe(takeUntil(this.destroy$))
      .subscribe(value => {
        if (value) {
          const cleanValue = value.replace(/[\s()]/g, '');
          if (cleanValue !== value) {
            this.registrationForm.patchValue({ phoneNumber: cleanValue }, { emitEvent: false });
          }
        }
      });

    // Subscribe to locale changes from the form
    this.registrationForm.get('locale')?.valueChanges
      .pipe(takeUntil(this.destroy$))
      .subscribe(locale => {
        this.translate.use(locale);
        localStorage.setItem('selectedLanguage', locale);
      });

    // Subscribe to language changes from the language switcher
    this.translate.onLangChange
      .pipe(takeUntil(this.destroy$))
      .subscribe(event => {
        if (this.registrationForm.get('locale')?.value !== event.lang) {
          this.registrationForm.patchValue({ locale: event.lang }, { emitEvent: false });
        }
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
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
        phoneNumber: formValue.phoneNumber || undefined,
        locale: formValue.locale
      };

      this.authService.signup(request).subscribe({
        next: () => {
          this.isLoading = false;
          this.router.navigate(['/email-confirmation']);
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
    if (control.errors['pattern'] && controlName === 'password') {
      return this.translate.instant('registration.form.password.pattern');
    }
    if (control.errors['passwordMismatch']) {
      return this.translate.instant('registration.form.confirmPassword.mismatch');
    }

    return this.translate.instant('common.error.invalid');
  }
}
