import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DashboardComponent } from './dashboard.component';
import { AuthService, UserDetailsResponse } from '../../core/services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { provideHttpClient } from '@angular/common/http';
import { TenantService, TenantResponse } from '../../core/services/tenant.service';
import { TranslateModule, TranslateLoader, TranslateService } from '@ngx-translate/core';
import { Observable, of } from 'rxjs';
import { By } from '@angular/platform-browser';

class FakeLoader implements TranslateLoader {
  getTranslation(): Observable<any> {
    return of({
      'dashboard.welcome': 'Welcome, {{name}}!',
      'dashboard.user': 'User',
      'dashboard.fitnessClub.label': 'Fitness Club',
      'dashboard.subtitle': 'Welcome to your dashboard'
    });
  }
}

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let tenantService: jasmine.SpyObj<TenantService>;
  let router: jasmine.SpyObj<Router>;
  let translateService: TranslateService;

  beforeEach(async () => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['isAuthenticated', 'getCurrentUser']);
    const tenantServiceSpy = jasmine.createSpyObj('TenantService', ['getById']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [
        DashboardComponent,
        CommonModule,
        TranslateModule.forRoot({
          loader: { provide: TranslateLoader, useClass: FakeLoader }
        })
      ],
      providers: [
        provideHttpClient(),
        { provide: AuthService, useValue: authServiceSpy },
        { provide: TenantService, useValue: tenantServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    tenantService = TestBed.inject(TenantService) as jasmine.SpyObj<TenantService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    translateService = TestBed.inject(TranslateService);

    // Set default language
    translateService.setDefaultLang('en');
    translateService.use('en');
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect to login if not authenticated', () => {
    authService.isAuthenticated.and.returnValue(false);

    component.ngOnInit();

    expect(router.navigate).toHaveBeenCalledWith(['/login']);
    expect(component.userDetails).toBeNull();
  });

  it('should load user details if authenticated', () => {
    const mockUserDetails: UserDetailsResponse = {
      id: '123',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      roles: ['ROLE_ADMIN'],
      tenantIds: ['tenant-123'],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };

    const mockTenantResponse: TenantResponse = {
      id: 'tenant-123',
      name: 'Test Tenant',
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };

    authService.isAuthenticated.and.returnValue(true);
    authService.getCurrentUser.and.returnValue(mockUserDetails);
    tenantService.getById.and.returnValue(of(mockTenantResponse));

    component.ngOnInit();

    expect(router.navigate).not.toHaveBeenCalled();
    expect(component.userDetails).toEqual(mockUserDetails);
    expect(component.tenantDetails).toEqual(mockTenantResponse);
    expect(tenantService.getById).toHaveBeenCalledWith('tenant-123');
  });

  it('should not load tenant details if user has no tenantIds', () => {
    const mockUserDetails: UserDetailsResponse = {
      id: '123',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      roles: ['ROLE_ADMIN'],
      tenantIds: [],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };

    authService.isAuthenticated.and.returnValue(true);
    authService.getCurrentUser.and.returnValue(mockUserDetails);

    component.ngOnInit();

    expect(tenantService.getById).not.toHaveBeenCalled();
    expect(component.tenantDetails).toBeNull();
  });

  it('should display welcome message with user name when authenticated', async () => {
    const mockUserDetails: UserDetailsResponse = {
      id: '123',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      roles: ['ROLE_ADMIN'],
      tenantIds: ['tenant-123'],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };

    const mockTenantResponse: TenantResponse = {
      id: 'tenant-123',
      name: 'Test Fitness Club',
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };

    authService.isAuthenticated.and.returnValue(true);
    authService.getCurrentUser.and.returnValue(mockUserDetails);
    tenantService.getById.and.returnValue(of(mockTenantResponse));

    component.ngOnInit();
    fixture.detectChanges();
    await fixture.whenStable();

    const welcomeMessage = fixture.debugElement.query(By.css('h1')).nativeElement;
    expect(welcomeMessage.textContent).toContain('Welcome, John!');
  });

  it('should display generic welcome message when user has no firstName', async () => {
    const mockUserDetails: UserDetailsResponse = {
      id: '123',
      firstName: '',
      lastName: 'Doe',
      email: 'john@example.com',
      roles: ['ROLE_ADMIN'],
      tenantIds: ['tenant-123'],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };

    const mockTenantResponse: TenantResponse = {
      id: 'tenant-123',
      name: 'Test Fitness Club',
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };

    authService.isAuthenticated.and.returnValue(true);
    authService.getCurrentUser.and.returnValue(mockUserDetails);
    tenantService.getById.and.returnValue(of(mockTenantResponse));

    component.ngOnInit();
    fixture.detectChanges();
    await fixture.whenStable();

    const welcomeMessage = fixture.debugElement.query(By.css('h1')).nativeElement;
    expect(welcomeMessage.textContent).toContain('Welcome, User!');
  });

  it('should show tenant name when tenant details are loaded', async () => {
    const mockUserDetails: UserDetailsResponse = {
      id: '123',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      roles: ['ROLE_ADMIN'],
      tenantIds: ['tenant-123'],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };

    const mockTenantResponse: TenantResponse = {
      id: 'tenant-123',
      name: 'Test Fitness Club',
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };

    authService.isAuthenticated.and.returnValue(true);
    authService.getCurrentUser.and.returnValue(mockUserDetails);
    tenantService.getById.and.returnValue(of(mockTenantResponse));

    component.ngOnInit();
    fixture.detectChanges();
    await fixture.whenStable();

    const tenantName = fixture.debugElement.query(By.css('.fitness-club h2')).nativeElement;
    expect(tenantName.textContent).toBe('Test Fitness Club');
  });

  it('should not show tenant section when tenant details are not loaded', () => {
    const mockUserDetails: UserDetailsResponse = {
      id: '123',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      roles: ['ROLE_ADMIN'],
      tenantIds: [],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };

    authService.isAuthenticated.and.returnValue(true);
    authService.getCurrentUser.and.returnValue(mockUserDetails);

    component.ngOnInit();
    fixture.detectChanges();

    const tenantSection = fixture.debugElement.query(By.css('.fitness-club'));
    expect(tenantSection).toBeNull();
  });

  it('should show locations component when tenant details are loaded', async () => {
    const mockUserDetails: UserDetailsResponse = {
      id: '123',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      roles: ['ROLE_ADMIN'],
      tenantIds: ['tenant-123'],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };

    const mockTenantResponse: TenantResponse = {
      id: 'tenant-123',
      name: 'Test Fitness Club',
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };

    authService.isAuthenticated.and.returnValue(true);
    authService.getCurrentUser.and.returnValue(mockUserDetails);
    tenantService.getById.and.returnValue(of(mockTenantResponse));

    component.ngOnInit();
    fixture.detectChanges();
    await fixture.whenStable();

    const locationsComponent = fixture.debugElement.query(By.css('app-locations'));
    expect(locationsComponent).toBeTruthy();
    expect(locationsComponent.componentInstance.tenantId).toBe('tenant-123');
  });
});
