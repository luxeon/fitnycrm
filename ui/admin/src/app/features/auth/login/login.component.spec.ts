import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { AuthService } from '../../../core/services/auth.service';
import { provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { provideHttpClient } from '@angular/common/http';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let translateService: TranslateService;

  const translations = {
    login: {
      email: {
        required: 'Email is required',
        invalid: 'Please enter a valid email',
        label: 'Email',
        placeholder: 'Enter your email'
      },
      password: {
        required: 'Password is required',
        label: 'Password',
        placeholder: 'Enter your password'
      },
      error: {
        invalidCredentials: 'Invalid email or password'
      },
      button: {
        login: 'Login',
        loading: 'Logging in...'
      }
    }
  };

  beforeEach(async () => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['login']);
    authServiceSpy.login.and.returnValue(of({
      accessToken: 'mock-access-token',
      refreshToken: 'mock-refresh-token',
      expiresIn: 3600
    }));

    await TestBed.configureTestingModule({
      imports: [
        LoginComponent,
        ReactiveFormsModule,
        TranslateModule.forRoot()
      ],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        provideRouter([
          { path: 'dashboard', component: {} as any }
        ]),
        provideHttpClient()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    translateService = TestBed.inject(TranslateService);

    // Set up translations
    translateService.setTranslation('en', translations);
    translateService.use('en');

    // Wait for translations to be loaded and error messages to be preloaded
    await component.ngOnInit();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with empty form fields', () => {
    expect(component.loginForm.get('email')?.value).toBe('');
    expect(component.loginForm.get('password')?.value).toBe('');
  });

  it('should validate required fields', () => {
    const form = component.loginForm;
    expect(form.valid).toBeFalsy();

    Object.keys(form.controls).forEach(key => {
      const control = form.get(key);
      control?.markAsTouched();
    });

    expect(form.get('email')?.hasError('required')).toBeTruthy();
    expect(form.get('password')?.hasError('required')).toBeTruthy();

    form.patchValue({
      email: 'test@example.com',
      password: 'password123'
    });

    expect(form.valid).toBeTruthy();
  });

  it('should validate email format', () => {
    const emailControl = component.loginForm.get('email');

    emailControl?.setValue('invalid-email');
    expect(emailControl?.hasError('email')).toBeTruthy();

    emailControl?.setValue('valid@email.com');
    expect(emailControl?.errors).toBeNull();
  });

  it('should enable submit button when form is valid', () => {
    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]'));
    expect(submitButton.nativeElement.disabled).toBeTruthy();

    component.loginForm.patchValue({
      email: 'test@example.com',
      password: 'password123'
    });
    fixture.detectChanges();

    expect(submitButton.nativeElement.disabled).toBeFalsy();
  });

  it('should handle successful login', fakeAsync(() => {
    const navigateSpy = spyOn(component['router'], 'navigate');
    spyOn(localStorage, 'setItem');

    component.loginForm.patchValue({
      email: 'test@example.com',
      password: 'password123'
    });

    component.onSubmit();
    tick();

    expect(authService.login).toHaveBeenCalledWith({
      email: 'test@example.com',
      password: 'password123'
    });

    expect(localStorage.setItem).toHaveBeenCalledWith('accessToken', 'mock-access-token');
    expect(localStorage.setItem).toHaveBeenCalledWith('refreshToken', 'mock-refresh-token');
    expect(navigateSpy).toHaveBeenCalledWith(['/dashboard']);
  }));

  it('should handle login error and subsequent retry', fakeAsync(() => {
    // First attempt - trigger an error
    authService.login.and.returnValue(throwError(() => new Error('Invalid credentials')));

    component.loginForm.patchValue({
      email: 'test@example.com',
      password: 'wrongpassword'
    });

    component.onSubmit();
    tick();

    expect(component.errorMessage).toBe(translations.login.error.invalidCredentials);
    expect(component.isLoading).toBeFalse();

    // Second attempt - successful login
    authService.login.and.returnValue(of({
      accessToken: 'mock-access-token',
      refreshToken: 'mock-refresh-token',
      expiresIn: 3600
    }));

    component.loginForm.patchValue({
      password: 'correctpassword'
    });
    component.onSubmit();
    tick();

    expect(component.errorMessage).toBeNull();
    expect(component.isLoading).toBeFalse();
  }));

  it('should display appropriate error messages', fakeAsync(async () => {
    const form = component.loginForm;

    // Wait for error messages to be preloaded
    await component.ngOnInit();
    tick();
    fixture.detectChanges();

    // Test email required error
    form.get('email')?.setValue('');
    form.get('email')?.markAsTouched();
    fixture.detectChanges();
    expect(component.getErrorMessage('email')).toBe(translations.login.email.required);

    // Test email invalid error
    form.get('email')?.setValue('invalid');
    form.get('email')?.markAsTouched();
    fixture.detectChanges();
    expect(component.getErrorMessage('email')).toBe(translations.login.email.invalid);

    // Test password required error
    form.get('password')?.setValue('');
    form.get('password')?.markAsTouched();
    fixture.detectChanges();
    expect(component.getErrorMessage('password')).toBe(translations.login.password.required);

    // Test no error message when fields are valid
    form.patchValue({
      email: 'test@example.com',
      password: 'password123'
    });
    fixture.detectChanges();
    expect(component.getErrorMessage('email')).toBe('');
    expect(component.getErrorMessage('password')).toBe('');
  }));

  it('should properly manage loading state during submission', fakeAsync(() => {
    component.loginForm.patchValue({
      email: 'test@example.com',
      password: 'password123'
    });

    component.onSubmit();
    expect(component.isLoading).toBeTrue();

    tick();
    expect(component.isLoading).toBeFalse();
  }));

  it('should not submit if form is invalid', () => {
    component.onSubmit();
    expect(authService.login).not.toHaveBeenCalled();
  });

  it('should initialize with correct translations', () => {
    const emailLabel = fixture.debugElement.query(By.css('label[for="email"]'));
    const passwordLabel = fixture.debugElement.query(By.css('label[for="password"]'));
    
    expect(emailLabel.nativeElement.textContent).toBe(translations.login.email.label);
    expect(passwordLabel.nativeElement.textContent).toBe(translations.login.password.label);
  });
});
