import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EmailConfirmationComponent } from './email-confirmation.component';
import { By } from '@angular/platform-browser';
import { TranslateModule, TranslateLoader, TranslateService } from '@ngx-translate/core';
import { RouterTestingModule } from '@angular/router/testing';
import { Observable, of } from 'rxjs';

class FakeLoader implements TranslateLoader {
  getTranslation(): Observable<any> {
    return of({
      'emailConfirmation.title': 'Email Confirmation Required',
      'emailConfirmation.message': 'Thank you for registering! We\'ve sent a confirmation email to your address. Please check your inbox and click the confirmation link to activate your account.',
      'emailConfirmation.login.text': 'Already confirmed your email?',
      'emailConfirmation.login.link': 'Log in'
    });
  }
}

describe('EmailConfirmationComponent', () => {
  let component: EmailConfirmationComponent;
  let fixture: ComponentFixture<EmailConfirmationComponent>;
  let translateService: TranslateService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        EmailConfirmationComponent,
        TranslateModule.forRoot({
          loader: { provide: TranslateLoader, useClass: FakeLoader }
        }),
        RouterTestingModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(EmailConfirmationComponent);
    component = fixture.componentInstance;
    translateService = TestBed.inject(TranslateService);
    translateService.setDefaultLang('en');
    translateService.use('en');
    await fixture.whenStable();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display translated title', () => {
    const titleElement = fixture.debugElement.query(By.css('h2'));
    expect(titleElement.nativeElement.textContent).toBe('Email Confirmation Required');
  });

  it('should display translated message', () => {
    const messageElement = fixture.debugElement.query(By.css('.message'));
    expect(messageElement.nativeElement.textContent).toBe('Thank you for registering! We\'ve sent a confirmation email to your address. Please check your inbox and click the confirmation link to activate your account.');
  });

  it('should have login link', () => {
    const loginLink = fixture.debugElement.query(By.css('a[routerLink="/login"]'));
    expect(loginLink).toBeTruthy();
  });

  it('should display translated login text', () => {
    const loginTextElement = fixture.debugElement.query(By.css('.login-link'));
    expect(loginTextElement.nativeElement.textContent).toContain('Already confirmed your email?');
    expect(loginTextElement.nativeElement.textContent).toContain('Log in');
  });
});
