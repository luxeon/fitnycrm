import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegistrationComponent } from './registration.component';
import { ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';

describe('RegistrationComponent', () => {
  let component: RegistrationComponent;
  let fixture: ComponentFixture<RegistrationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistrationComponent, ReactiveFormsModule]
    }).compileComponents();

    fixture = TestBed.createComponent(RegistrationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with empty form fields', () => {
    expect(component.registrationForm.get('firstName')?.value).toBe('');
    expect(component.registrationForm.get('lastName')?.value).toBe('');
    expect(component.registrationForm.get('email')?.value).toBe('');
    expect(component.registrationForm.get('password')?.value).toBe('');
    expect(component.registrationForm.get('confirmPassword')?.value).toBe('');
  });

  it('should validate required fields', () => {
    const form = component.registrationForm;

    expect(form.valid).toBeFalsy();

    // Mark all fields as touched to trigger validation
    Object.keys(form.controls).forEach(key => {
      const control = form.get(key);
      control?.markAsTouched();
    });

    expect(form.get('firstName')?.hasError('required')).toBeTruthy();
    expect(form.get('lastName')?.hasError('required')).toBeTruthy();
    expect(form.get('email')?.hasError('required')).toBeTruthy();
    expect(form.get('password')?.hasError('required')).toBeTruthy();
    expect(form.get('confirmPassword')?.hasError('required')).toBeFalsy();

    form.get('password')?.setValue('123456789');
    expect(form.get('password')?.hasError('required')).toBeFalsy();
    expect(form.get('confirmPassword')?.hasError('passwordMismatch')).toBeTruthy();
  });

  it('should validate email format', () => {
    const emailControl = component.registrationForm.get('email');

    emailControl?.setValue('invalid-email');
    expect(emailControl?.hasError('email')).toBeTruthy();

    emailControl?.setValue('valid@email.com');
    expect(emailControl?.errors).toBeNull();
  });

  it('should validate password length', () => {
    const passwordControl = component.registrationForm.get('password');

    passwordControl?.setValue('short');
    expect(passwordControl?.hasError('minlength')).toBeTruthy();

    passwordControl?.setValue('validpassword123');
    expect(passwordControl?.errors).toBeNull();
  });

  it('should validate password match', () => {
    const form = component.registrationForm;
    const password = form.get('password');
    const confirmPassword = form.get('confirmPassword');

    password?.setValue('validpassword123');
    confirmPassword?.setValue('differentpassword');
    expect(confirmPassword?.hasError('passwordMismatch')).toBeTruthy();

    confirmPassword?.setValue('validpassword123');
    expect(confirmPassword?.errors).toBeNull();
  });

  it('should enable submit button when form is valid', () => {
    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]'));
    expect(submitButton.nativeElement.disabled).toBeTruthy();

    component.registrationForm.patchValue({
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      password: 'password123',
      confirmPassword: 'password123'
    });
    fixture.detectChanges();

    expect(submitButton.nativeElement.disabled).toBeFalsy();
  });

  it('should call onSubmit when form is submitted', () => {
    spyOn(component, 'onSubmit');
    const form = fixture.debugElement.query(By.css('form'));

    component.registrationForm.patchValue({
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      password: 'password123',
      confirmPassword: 'password123'
    });
    fixture.detectChanges();

    form.triggerEventHandler('submit', null);
    expect(component.onSubmit).toHaveBeenCalled();
  });

  it('should display error messages when fields are touched and invalid', () => {
    const firstName = component.registrationForm.get('firstName');
    firstName?.markAsTouched();
    fixture.detectChanges();

    const errorMessage = fixture.debugElement.query(By.css('#firstName + .error-message'));
    expect(errorMessage.nativeElement.textContent.trim()).toBe('First name is required');
  });
});
