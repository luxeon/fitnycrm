import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DashboardComponent } from './dashboard.component';
import { AuthService, UserDetailsResponse } from '../../core/services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['isAuthenticated', 'getCurrentUser']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [
        DashboardComponent,
        CommonModule
      ],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
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

    authService.isAuthenticated.and.returnValue(true);
    authService.getCurrentUser.and.returnValue(mockUserDetails);

    component.ngOnInit();

    expect(router.navigate).not.toHaveBeenCalled();
    expect(component.userDetails).toEqual(mockUserDetails);
  });
}); 