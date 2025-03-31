import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EditLocationComponent } from './edit-location.component';
import { LocationService, LocationPageItemResponse } from '../../core/services/location.service';
import { Router, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { Observable, of } from 'rxjs';
import { By } from '@angular/platform-browser';

class FakeLoader implements TranslateLoader {
  getTranslation(): Observable<any> {
    return of({
      'location.edit.title': 'Edit Location',
      'location.form.address': 'Address',
      'location.form.city': 'City',
      'location.form.state': 'State',
      'location.form.postalCode': 'Postal Code',
      'location.form.country': 'Country',
      'location.form.timezone': 'Timezone',
      'location.form.addressRequired': 'Address is required',
      'location.form.cityRequired': 'City is required',
      'location.form.stateRequired': 'State is required',
      'location.form.postalCodeRequired': 'Postal code is required',
      'location.form.countryRequired': 'Country is required',
      'location.form.timezoneRequired': 'Timezone is required',
      'common.save': 'Save',
      'common.saving': 'Saving...',
      'common.cancel': 'Cancel'
    });
  }
}

describe('EditLocationComponent', () => {
  let component: EditLocationComponent;
  let fixture: ComponentFixture<EditLocationComponent>;
  let locationService: jasmine.SpyObj<LocationService>;
  let router: jasmine.SpyObj<Router>;
  let route: jasmine.SpyObj<ActivatedRoute>;

  const mockLocation: LocationPageItemResponse = {
    id: '1',
    address: '123 Main St',
    city: 'New York',
    state: 'NY',
    postalCode: '10001',
    country: 'USA',
    timezone: 'America/New_York',
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z'
  };

  beforeEach(async () => {
    const locationServiceSpy = jasmine.createSpyObj('LocationService', ['getLocation', 'updateLocation']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    const routeSpy = jasmine.createSpyObj('ActivatedRoute', [], {
      queryParams: of({ tenantId: 'tenant-1', locationId: '1' })
    });

    await TestBed.configureTestingModule({
      imports: [
        EditLocationComponent,
        CommonModule,
        ReactiveFormsModule,
        TranslateModule.forRoot({
          loader: { provide: TranslateLoader, useClass: FakeLoader }
        })
      ],
      providers: [
        { provide: LocationService, useValue: locationServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: ActivatedRoute, useValue: routeSpy }
      ]
    }).compileComponents();

    locationService = TestBed.inject(LocationService) as jasmine.SpyObj<LocationService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    route = TestBed.inject(ActivatedRoute) as jasmine.SpyObj<ActivatedRoute>;
  });

  beforeEach(() => {
    locationService.getLocation.and.returnValue(of(mockLocation));
    fixture = TestBed.createComponent(EditLocationComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should load location data on init', async () => {
    fixture.detectChanges();
    await fixture.whenStable();
    
    expect(locationService.getLocation).toHaveBeenCalledWith('tenant-1', '1');
    
    const form = component.locationForm;
    expect(form.get('address')?.value).toBe('123 Main St');
    expect(form.get('city')?.value).toBe('New York');
    expect(form.get('state')?.value).toBe('NY');
    expect(form.get('postalCode')?.value).toBe('10001');
    expect(form.get('country')?.value).toBe('USA');
    expect(form.get('timezone')?.value).toBe('America/New_York');
  });

  it('should show validation errors when form is submitted with empty fields', async () => {
    component.locationForm.patchValue({
      address: '',
      city: '',
      state: '',
      postalCode: '',
      country: '',
      timezone: ''
    });
    component.locationForm.markAllAsTouched();
    fixture.detectChanges();

    const errorMessages = fixture.debugElement.queryAll(By.css('.error-message'));
    expect(errorMessages.length).toBe(6);
  });

  it('should update location when form is valid and submitted', async () => {
    // Initialize component and wait for location data to load
    fixture.detectChanges();
    await fixture.whenStable();

    // Setup the update response
    locationService.updateLocation.and.returnValue(of(mockLocation));

    // Trigger form submission
    const form = fixture.debugElement.query(By.css('form'));
    form.triggerEventHandler('ngSubmit', null);
    
    // Wait for async operations
    fixture.detectChanges();
    await fixture.whenStable();

    // Verify the service was called with correct data
    expect(locationService.updateLocation).toHaveBeenCalledWith('tenant-1', '1', {
      address: '123 Main St',
      city: 'New York',
      state: 'NY',
      postalCode: '10001',
      country: 'USA',
      timezone: 'America/New_York'
    });
    expect(router.navigate).toHaveBeenCalledWith(['/dashboard']);
  });

  it('should navigate to dashboard when cancel is clicked', () => {
    const cancelButton = fixture.debugElement.query(By.css('.btn-secondary'));
    cancelButton.nativeElement.click();
    fixture.detectChanges();

    expect(router.navigate).toHaveBeenCalledWith(['/dashboard']);
  });

  it('should disable submit button when form is invalid', () => {
    component.locationForm.patchValue({
      address: '',
      city: '',
      state: '',
      postalCode: '',
      country: '',
      timezone: ''
    });
    fixture.detectChanges();

    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]'));
    expect(submitButton.nativeElement.disabled).toBe(true);
  });

  it('should disable submit button while loading', () => {
    component.isLoading = true;
    fixture.detectChanges();

    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]'));
    expect(submitButton.nativeElement.disabled).toBe(true);
  });
}); 