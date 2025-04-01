import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { TranslateModule, TranslateLoader, TranslateService } from '@ngx-translate/core';
import { Observable, of } from 'rxjs';
import { provideRouter, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { Component } from '@angular/core';

// Create dummy components for testing
@Component({ template: '' })
class DummyLoginComponent {}

@Component({ template: '' })
class DummyHomeComponent {}

// Create a fake loader for translations
class FakeLoader implements TranslateLoader {
  getTranslation(): Observable<any> {
    return of({
      'common.title': 'FitNYC',
      'login.pageTitle': 'Login',
      'registration.pageTitle': 'Register',
      'emailConfirmation.pageTitle': 'Email Confirmation',
      'tenant.create.pageTitle': 'Create Fitness Club',
      'location.create.pageTitle': 'Add Location',
      'location.edit.pageTitle': 'Edit Location',
      'location.details.pageTitle': 'Location Details',
      'training.create.pageTitle': 'Create Workout',
      'training.edit.pageTitle': 'Edit Workout',
      'trainer.create.pageTitle': 'Add Trainer',
      'dashboard.pageTitle': 'Dashboard'
    });
  }
}

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let titleService: Title;
  let router: Router;
  let translateService: TranslateService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        AppComponent,
        TranslateModule.forRoot({
          loader: { provide: TranslateLoader, useClass: FakeLoader }
        })
      ],
      providers: [
        provideRouter([
          { 
            path: 'login',
            component: DummyLoginComponent,
            data: { titleKey: 'login.pageTitle' }
          },
          {
            path: '',
            component: DummyHomeComponent,
            data: { titleKey: 'common.title' }
          }
        ]),
        Title
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    titleService = TestBed.inject(Title);
    router = TestBed.inject(Router);
    translateService = TestBed.inject(TranslateService);

    // Set up translations
    translateService.setDefaultLang('en');
    translateService.use('en');
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should update title on navigation', fakeAsync(() => {
    const spy = spyOn(titleService, 'setTitle');
    
    // Initialize the component
    fixture.detectChanges();
    tick(); // Wait for initial navigation
    tick(); // Wait for translation

    // Navigate to login page
    router.navigate(['/login']);
    tick(); // Wait for navigation
    tick(); // Wait for translation

    // Verify the title was set with the translated value
    expect(spy).toHaveBeenCalledWith('Login - FitNYC');
  }));

  it('should set default title for root path', fakeAsync(() => {
    const spy = spyOn(titleService, 'setTitle');
    
    // Initialize the component
    fixture.detectChanges();
    tick(); // Wait for initial navigation
    tick(); // Wait for translation

    // Navigate to root path
    router.navigate(['/']);
    tick(); // Wait for navigation
    tick(); // Wait for translation

    // Verify the default title was set
    expect(spy).toHaveBeenCalledWith('FitNYC - FitNYC');
  }));
});
