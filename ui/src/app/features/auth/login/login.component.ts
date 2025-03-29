import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { firstValueFrom } from 'rxjs';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { LanguageSwitcherComponent } from '../../../shared/components/language-switcher/language-switcher.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    TranslateModule,
    LanguageSwitcherComponent
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);
  private readonly translate = inject(TranslateService);

  loginForm: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]]
  });

  errorMessage: string | null = null;
  isLoading = false;

  constructor() {
    // Set default language
    this.translate.setDefaultLang('en');
    // Use browser language if available, otherwise use default
    const browserLang = this.translate.getBrowserLang();
    this.translate.use(browserLang?.match(/en|es/) ? browserLang : 'en');
  }

  async onSubmit(): Promise<void> {
    if (this.loginForm.valid) {
      this.isLoading = true;
      this.errorMessage = null;

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
      }
    }
  }

  async getErrorMessage(controlName: string): Promise<string> {
    const control = this.loginForm.get(controlName);
    if (!control?.errors || !control.touched) return '';

    if (control.errors['required']) {
      return await firstValueFrom(this.translate.get(`login.${controlName}.required`));
    }
    if (controlName === 'email' && control.errors['email']) {
      return await firstValueFrom(this.translate.get(`login.${controlName}.invalid`));
    }

    return 'Invalid input';
  }
}
