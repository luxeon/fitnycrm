import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { RegistrationComponent } from './registration.component';
import { ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { AuthService, UserDetailsResponse } from '../../core/services/auth.service';
import { provideRouter } from '@angular/router';
import { delay, of, throwError } from 'rxjs';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

describe('RegistrationComponent', () => {
  let component: RegistrationComponent;
  let fixture: ComponentFixture<RegistrationComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let translateService: TranslateService;

  beforeEach(async () => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['signup']);
    authServiceSpy.signup.and.returnValue(of({}));

    await TestBed.configureTestingModule({
      imports: [
        RegistrationComponent,
        ReactiveFormsModule,
        TranslateModule.forRoot()
      ],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        provideRouter([
          { path: 'email-confirmation', component: {} as any }
        ])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegistrationComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    translateService = TestBed.inject(TranslateService);

    // Set up translations
    translateService.setTranslation('en', {
      registration: {
        form: {
          firstName: {
            required: 'Please enter your first name',
            minLength: 'First name must be at least {{length}} characters long',
            maxLength: 'First name cannot be longer than {{length}} characters'
          },
          lastName: {
            required: 'Please enter your last name',
            minLength: 'Last name must be at least {{length}} characters long',
            maxLength: 'Last name cannot be longer than {{length}} characters'
          },
          email: {
            required: 'Please enter your email address',
            invalid: 'Please enter a valid email address'
          },
          phoneNumber: {
            pattern: 'Please enter a valid phone number starting with + followed by country code and number (e.g., +12125551234)'
          },
          password: {
            required: 'Please enter your password',
            pattern: 'Your password must include at least one number, one lowercase letter, one uppercase letter, and one special character (@#$%^&+=)'
          },
          confirmPassword: {
            required: 'Please confirm your password',
            mismatch: 'The passwords you entered do not match'
          }
        },
        success: 'Registration successful! Please check your email to confirm your account.',
        error: {
          emailExists: 'An account with this email already exists.',
          generic: 'An error occurred during registration. Please try again.'
        }
      }
    });
    translateService.use('en');

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with empty form fields', () => {
    expect(component.registrationForm.get('firstName')?.value).toBe('');
    expect(component.registrationForm.get('lastName')?.value).toBe('');
    expect(component.registrationForm.get('email')?.value).toBe('');
    expect(component.registrationForm.get('phoneNumber')?.value).toBe('');
    expect(component.registrationForm.get('password')?.value).toBe('');
    expect(component.registrationForm.get('confirmPassword')?.value).toBe('');
  });

  it('should validate required fields and show translated messages', () => {
    const form = component.registrationForm;
    expect(form.valid).toBeFalsy();

    Object.keys(form.controls).forEach(key => {
      const control = form.get(key);
      control?.markAsTouched();
    });

    expect(form.get('firstName')?.hasError('required')).toBeTruthy();
    expect(component.getErrorMessage('firstName')).toBe('Please enter your first name');

    expect(form.get('lastName')?.hasError('required')).toBeTruthy();
    expect(component.getErrorMessage('lastName')).toBe('Please enter your last name');

    expect(form.get('email')?.hasError('required')).toBeTruthy();
    expect(component.getErrorMessage('email')).toBe('Please enter your email address');

    expect(form.get('password')?.hasError('required')).toBeTruthy();
    expect(component.getErrorMessage('password')).toBe('Please enter your password');

    expect(form.get('confirmPassword')?.hasError('required')).toBeFalsy();
    expect(form.get('phoneNumber')?.hasError('required')).toBeFalsy(); // Phone is optional

    form.get('password')?.setValue('1qQ@1234');
    expect(form.get('password')?.hasError('required')).toBeFalsy();
    expect(form.get('confirmPassword')?.hasError('passwordMismatch')).toBeTruthy();
    expect(component.getErrorMessage('confirmPassword')).toBe('The passwords you entered do not match');
  });

  it('should validate name length constraints with translated messages', () => {
    const firstName = component.registrationForm.get('firstName');
    const lastName = component.registrationForm.get('lastName');

    firstName?.setValue('J');
    lastName?.setValue('D');
    firstName?.markAsTouched();
    lastName?.markAsTouched();
    expect(firstName?.hasError('minlength')).toBeTruthy();
    expect(lastName?.hasError('minlength')).toBeTruthy();
    expect(component.getErrorMessage('firstName')).toBe('First name must be at least 2 characters long');
    expect(component.getErrorMessage('lastName')).toBe('Last name must be at least 2 characters long');

    firstName?.setValue('J'.repeat(256));
    lastName?.setValue('D'.repeat(256));
    expect(firstName?.hasError('maxlength')).toBeTruthy();
    expect(lastName?.hasError('maxlength')).toBeTruthy();
    expect(component.getErrorMessage('firstName')).toBe('First name cannot be longer than 255 characters');
    expect(component.getErrorMessage('lastName')).toBe('Last name cannot be longer than 255 characters');

    firstName?.setValue('John');
    lastName?.setValue('Doe');
    expect(firstName?.errors).toBeNull();
    expect(lastName?.errors).toBeNull();
  });

  it('should validate email format with translated messages', () => {
    const emailControl = component.registrationForm.get('email');

    emailControl?.setValue('invalid-email');
    emailControl?.markAsTouched();
    expect(emailControl?.hasError('email')).toBeTruthy();
    expect(component.getErrorMessage('email')).toBe('Please enter a valid email address');

    emailControl?.setValue('valid@email.com');
    expect(emailControl?.errors).toBeNull();
  });

  it('should validate phone number format with translated messages', () => {
    const phoneControl = component.registrationForm.get('phoneNumber');
    const expectedErrorMessage = 'Please enter a valid phone number starting with + followed by country code and number (e.g., +12125551234)';

    // Invalid formats
    const invalidFormats = [
      'invalid',
      '1234567890',        // Missing plus
      '+123',              // Too short
      '+12345678901234567', // Too long
      '++1234567890',      // Double plus
      '+0234567890',       // Starting with 0
      '+abc12345678',      // Contains letters
      '+12 34567890'       // Contains spaces
    ];

    invalidFormats.forEach(format => {
      phoneControl?.setValue(format);
      expect(phoneControl?.hasError('pattern')).toBeTruthy();
      phoneControl?.markAsTouched();
      expect(component.getErrorMessage('phoneNumber')).toBe(expectedErrorMessage);
    });

    // Valid formats
    const validFormats = [
      '+12125551234',     // US number
      '+442071234567',    // UK number
      '+61291234567',     // AU number
      '+12345678',        // Minimum length
      '+123456789012345'  // Maximum length
    ];

    validFormats.forEach(format => {
      phoneControl?.setValue(format);
      expect(phoneControl?.errors).toBeNull();
    });

    // Empty phone number should be valid (optional field)
    phoneControl?.setValue('');
    expect(phoneControl?.errors).toBeNull();
  });

  it('should validate password complexity with translated messages', () => {
    const passwordControl = component.registrationForm.get('password');
    const expectedErrorMessage = 'Your password must include at least one number, one lowercase letter, one uppercase letter, and one special character (@#$%^&+=)';

    passwordControl?.setValue('weak');
    passwordControl?.markAsTouched();
    expect(passwordControl?.hasError('pattern')).toBeTruthy();
    expect(component.getErrorMessage('password')).toBe(expectedErrorMessage);

    passwordControl?.setValue('StrongP@ss123');
    expect(passwordControl?.errors).toBeNull();
  });

  it('should validate password match with translated messages', () => {
    const form = component.registrationForm;

    form.patchValue({
      password: 'StrongP@ss123',
      confirmPassword: 'DifferentP@ss123'
    });
    form.get('confirmPassword')?.markAsTouched();
    expect(form.get('confirmPassword')?.hasError('passwordMismatch')).toBeTruthy();
    expect(component.getErrorMessage('confirmPassword')).toBe('The passwords you entered do not match');

    form.patchValue({
      password: 'StrongP@ss123',
      confirmPassword: 'StrongP@ss123'
    });
    expect(form.get('confirmPassword')?.errors).toBeNull();
  });

  it('should enable submit button when form is valid', () => {
    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]'));
    expect(submitButton.nativeElement.disabled).toBeTruthy();

    component.registrationForm.patchValue({
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      password: 'StrongP@ss123',
      confirmPassword: 'StrongP@ss123',
      phoneNumber: '+1234567890'
    });
    fixture.detectChanges();

    expect(submitButton.nativeElement.disabled).toBeFalsy();
  });

  it('should handle successful registration and redirect to email confirmation', () => {
    const navigateSpy = spyOn(component['router'], 'navigate');

    component.registrationForm.patchValue({
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      password: 'StrongP@ss123',
      confirmPassword: 'StrongP@ss123',
      phoneNumber: '+1234567890'
    });

    component.onSubmit();

    expect(authService.signup).toHaveBeenCalledWith({
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      password: 'StrongP@ss123',
      phoneNumber: '+1234567890'
    });
    expect(navigateSpy).toHaveBeenCalledWith(['/email-confirmation']);
  });

  it('should handle registration error with translated messages', fakeAsync(() => {
    authService.signup.and.returnValue(throwError(() => ({
      status: 409,
      error: { message: 'An account with this email already exists.' }
    })));

    component.registrationForm.patchValue({
      firstName: 'John',
      lastName: 'Doe',
      email: 'existing@email.com',
      password: 'StrongP@ss123',
      confirmPassword: 'StrongP@ss123'
    });

    component.onSubmit();
    tick();

    expect(component.errorMessage).toBe('An account with this email already exists.');
    expect(component.isLoading).toBeFalse();
  }));

  it('should display appropriate error messages', () => {
    const form = component.registrationForm;

    // Test first name error
    form.get('firstName')?.setValue('');
    form.get('firstName')?.markAsTouched();
    expect(component.getErrorMessage('firstName')).toBe('Please enter your first name');

    // Test email error
    form.get('email')?.setValue('invalid');
    form.get('email')?.markAsTouched();
    expect(component.getErrorMessage('email')).toBe('Please enter a valid email address');

    // Test password error
    form.get('password')?.setValue('weak');
    form.get('password')?.markAsTouched();
    expect(component.getErrorMessage('password'))
      .toBe('Your password must include at least one number, one lowercase letter, one uppercase letter, and one special character (@#$%^&+=)');

    // Test phone number error
    form.get('phoneNumber')?.setValue('invalid');
    form.get('phoneNumber')?.markAsTouched();
    expect(component.getErrorMessage('phoneNumber'))
      .toBe('Please enter a valid phone number starting with + followed by country code and number (e.g., +12125551234)');

    // Test no error message when phone is valid
    form.get('phoneNumber')?.setValue('+12125551234');
    expect(component.getErrorMessage('phoneNumber')).toBe('');
  });

  it('should handle generic server error', fakeAsync(() => {
    authService.signup.and.returnValue(throwError(() => ({
      status: 500,
      error: { message: 'Internal server error' }
    })));

    component.registrationForm.patchValue({
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      password: 'StrongP@ss123',
      confirmPassword: 'StrongP@ss123'
    });

    component.onSubmit();
    tick();

    expect(component.errorMessage).toBe('Internal server error');
    expect(component.isLoading).toBeFalse();
  }));

  it('should handle server error without message', fakeAsync(() => {
    authService.signup.and.returnValue(throwError(() => ({
      status: 500
    })));

    component.registrationForm.patchValue({
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      password: 'StrongP@ss123',
      confirmPassword: 'StrongP@ss123'
    });

    component.onSubmit();
    tick();

    expect(component.errorMessage).toBe('An error occurred during registration. Please try again.');
    expect(component.isLoading).toBeFalse();
  }));

  it('should properly manage loading state during submission', fakeAsync(() => {
    // Setup delayed response
    const mockResponse: UserDetailsResponse = {
      id: '123',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      roles: ['ROLE_CLIENT'],
      tenantIds: ['tenant-123'],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };
    authService.signup.and.returnValue(of(mockResponse).pipe(delay(100)));

    // Fill form with valid data
    component.registrationForm.patchValue({
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      password: 'StrongP@ss123',
      confirmPassword: 'StrongP@ss123'
    });

    // Submit form
    component.onSubmit();

    // Should be loading immediately after submission
    expect(component.isLoading).toBeTrue();

    // Wait for the async operation to complete
    tick(100);

    // Should not be loading after completion
    expect(component.isLoading).toBeFalse();
  }));

  it('should handle submission with empty phone number', () => {

    component.registrationForm.patchValue({
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      password: 'StrongP@ss123',
      confirmPassword: 'StrongP@ss123',
      phoneNumber: ''
    });

    component.onSubmit();

    expect(authService.signup).toHaveBeenCalledWith({
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      password: 'StrongP@ss123',
      phoneNumber: undefined
    });
  });

  it('should show multiple validation errors for the same field', () => {
    const form = component.registrationForm;

    // Test first name with both minlength and maxlength errors
    form.get('firstName')?.setValue('a');
    form.get('firstName')?.markAsTouched();
    expect(component.getErrorMessage('firstName')).toBe('First name must be at least 2 characters long');

    form.get('firstName')?.setValue('a'.repeat(256));
    form.get('firstName')?.markAsTouched();
    expect(component.getErrorMessage('firstName')).toBe('First name cannot be longer than 255 characters');
  });
});
