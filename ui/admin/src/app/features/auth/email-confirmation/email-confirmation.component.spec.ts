import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EmailConfirmationComponent } from './email-confirmation.component';
import { By } from '@angular/platform-browser';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { RouterTestingModule } from '@angular/router/testing';

describe('EmailConfirmationComponent', () => {
  let component: EmailConfirmationComponent;
  let fixture: ComponentFixture<EmailConfirmationComponent>;
  let translateService: TranslateService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        EmailConfirmationComponent,
        TranslateModule.forRoot(),
        RouterTestingModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(EmailConfirmationComponent);
    component = fixture.componentInstance;
    translateService = TestBed.inject(TranslateService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display translated title', () => {
    const translatedTitle = 'Email Confirmation Required';
    spyOn(translateService, 'instant').and.returnValue(translatedTitle);

    fixture.detectChanges();
    const titleElement = fixture.debugElement.query(By.css('h2'));

    expect(titleElement.nativeElement.textContent).toBe(translatedTitle);
  });

  it('should display translated message', () => {
    const translatedMessage = 'Thank you for registering! We\'ve sent a confirmation email to your address. Please check your inbox and click the confirmation link to activate your account.';
    spyOn(translateService, 'instant').and.returnValue(translatedMessage);

    fixture.detectChanges();
    const messageElement = fixture.debugElement.query(By.css('.message'));

    expect(messageElement.nativeElement.textContent).toBe(translatedMessage);
  });

  it('should have login link', () => {
    const loginLink = fixture.debugElement.query(By.css('a[routerLink="/login"]'));
    expect(loginLink).toBeTruthy();
  });

  it('should display translated login text', () => {
    const translatedLoginText = 'Already confirmed your email?';
    const translatedLoginLink = 'Log in';

    const translateSpy = spyOn(translateService, 'instant');
    translateSpy.and.returnValues(translatedLoginText, translatedLoginLink);

    fixture.detectChanges();
    const loginTextElement = fixture.debugElement.query(By.css('.login-link'));

    expect(loginTextElement.nativeElement.textContent).toContain(translatedLoginText);
    expect(loginTextElement.nativeElement.textContent).toContain(translatedLoginLink);
  });
});
