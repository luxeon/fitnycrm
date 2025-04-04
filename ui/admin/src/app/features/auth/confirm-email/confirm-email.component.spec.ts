import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ConfirmEmailComponent } from './confirm-email.component';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { RouterTestingModule } from '@angular/router/testing';
import { Observable, of, throwError } from 'rxjs';
import { By } from '@angular/platform-browser';

class FakeLoader implements TranslateLoader {
  getTranslation(): Observable<any> {
    return of({
      confirmEmail: {
        loading: {
          title: 'Confirming Email',
          message: 'Please wait while we confirm your email address...'
        },
        success: {
          title: 'Email Confirmed',
          message: 'Your email has been successfully confirmed.',
          login: {
            text: 'You can now',
            link: 'log in'
          }
        },
        error: {
          title: 'Error',
          noToken: 'No confirmation token provided.',
          generic: 'An error occurred while confirming your email.',
          retry: {
            text: 'Please try again or',
            link: 'contact support'
          }
        }
      }
    });
  }
}

describe('ConfirmEmailComponent', () => {
  let component: ConfirmEmailComponent;
  let fixture: ComponentFixture<ConfirmEmailComponent>;
  let authService: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['confirmEmail']);
    authServiceSpy.confirmEmail.and.returnValue(of(void 0));

    await TestBed.configureTestingModule({
      imports: [
        ConfirmEmailComponent,
        RouterTestingModule,
        TranslateModule.forRoot({
          loader: { provide: TranslateLoader, useClass: FakeLoader }
        })
      ],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              queryParamMap: {
                get: (param: string) => param === 'token' ? 'test-token' : null
              }
            }
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ConfirmEmailComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should show loading state initially', () => {
    const loadingTitle = fixture.debugElement.query(By.css('h2'));
    expect(loadingTitle.nativeElement.textContent).toBe('Confirming Email');
  });

  it('should show success message after successful confirmation', () => {
    component.isLoading = false;
    fixture.detectChanges();

    const successTitle = fixture.debugElement.query(By.css('h2'));
    expect(successTitle.nativeElement.textContent).toBe('Email Confirmed');
    expect(authService.confirmEmail).toHaveBeenCalledWith('test-token');
  });

  it('should show error message when token is missing', () => {
    TestBed.resetTestingModule();
    TestBed.configureTestingModule({
      imports: [
        ConfirmEmailComponent,
        RouterTestingModule,
        TranslateModule.forRoot({
          loader: { provide: TranslateLoader, useClass: FakeLoader }
        })
      ],
      providers: [
        { provide: AuthService, useValue: { confirmEmail: () => of(void 0) } },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              queryParamMap: {
                get: () => null
              }
            }
          }
        }
      ]
    });

    fixture = TestBed.createComponent(ConfirmEmailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    const errorTitle = fixture.debugElement.query(By.css('h2'));
    expect(errorTitle.nativeElement.textContent).toBe('Error');
    expect(component.error).toBe('confirmEmail.error.noToken');
  });

  it('should show error message when confirmation fails', () => {
    const errorMessage = 'Invalid or expired token';
    authService.confirmEmail.and.returnValue(throwError(() => ({ error: { message: errorMessage } })));

    component.ngOnInit();
    fixture.detectChanges();

    expect(component.isLoading).toBeFalse();
    expect(component.error).toBe(errorMessage);
  });
}); 