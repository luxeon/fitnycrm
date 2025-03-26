import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { RegistrationComponent } from './registration.component';
import { ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { AuthService } from '../../core/services/auth.service';
import { provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';

describe('RegistrationComponent', () => {
  let component: RegistrationComponent;
  let fixture: ComponentFixture<RegistrationComponent>;
  let authService: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['signup']);
    authServiceSpy.signup.and.returnValue(of({}));

    await TestBed.configureTestingModule({
      imports: [
        RegistrationComponent,
        ReactiveFormsModule
      ],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegistrationComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
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

  it('should validate required fields', () => {
    const form = component.registrationForm;
    expect(form.valid).toBeFalsy();

    Object.keys(form.controls).forEach(key => {
      const control = form.get(key);
      control?.markAsTouched();
    });

    expect(form.get('firstName')?.hasError('required')).toBeTruthy();
    expect(form.get('lastName')?.hasError('required')).toBeTruthy();
    expect(form.get('email')?.hasError('required')).toBeTruthy();
    expect(form.get('password')?.hasError('required')).toBeTruthy();
    expect(form.get('confirmPassword')?.hasError('required')).toBeFalsy();
    expect(form.get('phoneNumber')?.hasError('required')).toBeFalsy(); // Phone is optional

    form.get('password')?.setValue('1qQ@1234');
    expect(form.get('password')?.hasError('required')).toBeFalsy();
    expect(form.get('confirmPassword')?.hasError('passwordMismatch')).toBeTruthy();
  });

  it('should validate name length constraints', () => {
    const firstName = component.registrationForm.get('firstName');
    const lastName = component.registrationForm.get('lastName');

    firstName?.setValue('J');
    lastName?.setValue('D');
    expect(firstName?.hasError('minlength')).toBeTruthy();
    expect(lastName?.hasError('minlength')).toBeTruthy();

    firstName?.setValue('J'.repeat(256));
    lastName?.setValue('D'.repeat(256));
    expect(firstName?.hasError('maxlength')).toBeTruthy();
    expect(lastName?.hasError('maxlength')).toBeTruthy();

    firstName?.setValue('John');
    lastName?.setValue('Doe');
    expect(firstName?.errors).toBeNull();
    expect(lastName?.errors).toBeNull();
  });

  it('should validate email format', () => {
    const emailControl = component.registrationForm.get('email');

    emailControl?.setValue('invalid-email');
    expect(emailControl?.hasError('email')).toBeTruthy();

    emailControl?.setValue('valid@email.com');
    expect(emailControl?.errors).toBeNull();
  });

  it('should validate phone number format', () => {
    const phoneControl = component.registrationForm.get('phoneNumber');

    phoneControl?.setValue('invalid');
    expect(phoneControl?.hasError('pattern')).toBeTruthy();

    phoneControl?.setValue('+1234567890');
    expect(phoneControl?.errors).toBeNull();

    // Empty phone number should be valid (optional field)
    phoneControl?.setValue('');
    expect(phoneControl?.errors).toBeNull();
  });

  it('should validate password complexity', () => {
    const passwordControl = component.registrationForm.get('password');

    passwordControl?.setValue('weak');
    expect(passwordControl?.hasError('pattern')).toBeTruthy();

    passwordControl?.setValue('StrongP@ss123');
    expect(passwordControl?.errors).toBeNull();
  });

  it('should validate password match', () => {
    const form = component.registrationForm;

    form.patchValue({
      password: 'StrongP@ss123',
      confirmPassword: 'DifferentP@ss123'
    });
    expect(form.get('confirmPassword')?.hasError('passwordMismatch')).toBeTruthy();

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

  it('should handle successful registration', () => {
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
    expect(navigateSpy).toHaveBeenCalledWith(['/login'], {
      queryParams: {
        message: 'Registration successful! Please check your email to confirm your account.'
      }
    });
  });

  it('should handle registration error', fakeAsync(() => {
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
    tick(); // Allow async operation to complete

    expect(component.errorMessage).toBe('An account with this email already exists.');
    expect(component.isLoading).toBeFalse();
  }));

  it('should display appropriate error messages', () => {
    const form = component.registrationForm;

    // Test first name error
    form.get('firstName')?.setValue('');
    form.get('firstName')?.markAsTouched();
    expect(component.getErrorMessage('firstName')).toBe('firstName is required');

    // Test email error
    form.get('email')?.setValue('invalid');
    form.get('email')?.markAsTouched();
    expect(component.getErrorMessage('email')).toBe('Please enter a valid email');

    // Test password error
    form.get('password')?.setValue('weak');
    form.get('password')?.markAsTouched();
    expect(component.getErrorMessage('password'))
      .toBe('Password must contain at least one digit, one lowercase, one uppercase, one special character and no whitespace');

    // Test phone number error
    form.get('phoneNumber')?.setValue('invalid');
    form.get('phoneNumber')?.markAsTouched();
    expect(component.getErrorMessage('phoneNumber'))
      .toBe('Phone number must be in E.164 format (e.g., +1234567890)');
  });
});
