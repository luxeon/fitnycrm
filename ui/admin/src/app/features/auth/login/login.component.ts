import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { firstValueFrom } from 'rxjs';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    TranslateModule,
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);
  private readonly translate = inject(TranslateService);
  private readonly cdr = inject(ChangeDetectorRef);

  loginForm: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]]
  });

  errorMessage: string | null = null;
  isLoading = false;
  errorMessages: { [key: string]: string } = {};

  constructor() {
    // Set default language
    this.translate.setDefaultLang('en');
    // Use browser language if available, otherwise use default
    const browserLang = this.translate.getBrowserLang();
    this.translate.use(browserLang?.match(/en|es/) ? browserLang : 'en');
  }

  async ngOnInit() {
    // Wait for translations to be loaded
    await firstValueFrom(this.translate.get('login'));
    
    // Pre-load error messages
    await this.preloadErrorMessages();

    // Handle autofill changes
    setTimeout(() => {
      if (this.loginForm.get('email')?.value || this.loginForm.get('password')?.value) {
        this.cdr.detectChanges();
      }
    }, 100);
  }

  private async preloadErrorMessages() {
    this.errorMessages = {
      emailRequired: await firstValueFrom(this.translate.get('login.email.required')),
      emailInvalid: await firstValueFrom(this.translate.get('login.email.invalid')),
      passwordRequired: await firstValueFrom(this.translate.get('login.password.required'))
    };
  }

  async onSubmit(): Promise<void> {
    if (this.loginForm.valid) {
      this.isLoading = true;
      this.errorMessage = null;
      this.cdr.detectChanges();

      try {
        const response = await firstValueFrom(this.authService.login(this.loginForm.value));
        if (response) {
          localStorage.setItem('accessToken', response.accessToken);
          localStorage.setItem('refreshToken', response.refreshToken);
          await this.router.navigate(['/dashboard']);
        }
      } catch (error) {
        this.errorMessage = await firstValueFrom(this.translate.get('login.error.invalidCredentials'));
      } finally {
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    }
  }

  getErrorMessage(controlName: string): string {
    const control = this.loginForm.get(controlName);
    if (!control?.errors || !control.touched) return '';

    if (control.errors['required']) {
      return this.errorMessages[`${controlName}Required`];
    }
    if (controlName === 'email' && control.errors['email']) {
      return this.errorMessages['emailInvalid'];
    }

    return '';
  }
}
