import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LocationsComponent } from './locations.component';
import { LocationService, LocationPageItemResponse } from '../../../../core/services/location.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { Observable, of } from 'rxjs';
import { By } from '@angular/platform-browser';
import { Page } from '../../../../core/models/page.model';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';

class FakeLoader implements TranslateLoader {
  getTranslation(): Observable<any> {
    return of({
      'dashboard.locations.title': 'Club Locations',
      'dashboard.locations.noLocations': 'No locations found',
      'dashboard.locations.loading': 'Loading locations...',
      'dashboard.locations.addLocation': 'Add Location',
      'dashboard.locations.delete.title': 'Delete Location',
      'dashboard.locations.delete.message': 'Are you sure you want to delete this location?',
      'dashboard.locations.delete.confirm': 'Delete',
      'common.previous': 'Previous',
      'common.next': 'Next',
      'common.page': 'Page',
      'common.of': 'of'
    });
  }
}

describe('LocationsComponent', () => {
  let component: LocationsComponent;
  let fixture: ComponentFixture<LocationsComponent>;
  let locationService: jasmine.SpyObj<LocationService>;
  let router: jasmine.SpyObj<Router>;

  const mockLocations: Page<LocationPageItemResponse> = {
    content: [
      {
        id: '1',
        address: '123 Main St',
        city: 'New York',
        state: 'NY',
        postalCode: '10001',
        country: 'USA',
        timezone: 'America/New_York',
        createdAt: '2024-01-01T00:00:00Z',
        updatedAt: '2024-01-01T00:00:00Z'
      },
      {
        id: '2',
        address: '456 Park Ave',
        city: 'Los Angeles',
        state: 'CA',
        postalCode: '90001',
        country: 'USA',
        timezone: 'America/Los_Angeles',
        createdAt: '2024-01-01T00:00:00Z',
        updatedAt: '2024-01-01T00:00:00Z'
      }
    ],
    totalElements: 2,
    totalPages: 1,
    size: 10,
    number: 0,
    first: true,
    last: true
  };

  beforeEach(async () => {
    const locationServiceSpy = jasmine.createSpyObj('LocationService', ['getLocations', 'deleteLocation']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [
        LocationsComponent,
        CommonModule,
        TranslateModule.forRoot({
          loader: { provide: TranslateLoader, useClass: FakeLoader }
        })
      ],
      providers: [
        { provide: LocationService, useValue: locationServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    locationService = TestBed.inject(LocationService) as jasmine.SpyObj<LocationService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LocationsComponent);
    component = fixture.componentInstance;
    component.tenantId = 'tenant-1';
    locationService.getLocations.and.returnValue(of(mockLocations));
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load locations on init', () => {
    expect(locationService.getLocations).toHaveBeenCalledWith('tenant-1', 0);
    expect(component.locations).toEqual(mockLocations);
  });

  it('should display locations in the grid', () => {
    const locationCards = fixture.debugElement.queryAll(By.css('.location-card'));
    expect(locationCards.length).toBe(2);

    const firstLocation = locationCards[0].nativeElement;
    expect(firstLocation.textContent).toContain('123 Main St');
    expect(firstLocation.textContent).toContain('New York, NY 10001');
    expect(firstLocation.textContent).toContain('USA');
  });

  it('should show delete button for each location', () => {
    const deleteButtons = fixture.debugElement.queryAll(By.css('.delete-btn'));
    expect(deleteButtons.length).toBe(2);
  });

  it('should open confirmation dialog when delete button is clicked', () => {
    const deleteButton = fixture.debugElement.query(By.css('.delete-btn'));
    deleteButton.triggerEventHandler('click');
    fixture.detectChanges();

    const dialog = fixture.debugElement.query(By.directive(ConfirmationDialogComponent));
    expect(dialog).toBeTruthy();
    expect(component.locationToDelete).toEqual(mockLocations.content[0]);
  });

  it('should close confirmation dialog when cancel is clicked', () => {
    // Open dialog
    const deleteButton = fixture.debugElement.query(By.css('.delete-btn'));
    deleteButton.triggerEventHandler('click');
    fixture.detectChanges();

    // Get dialog and trigger cancel
    const dialog = fixture.debugElement.query(By.directive(ConfirmationDialogComponent));
    dialog.componentInstance.cancel.emit();
    fixture.detectChanges();

    // Check dialog is closed
    const dialogAfterCancel = fixture.debugElement.query(By.directive(ConfirmationDialogComponent));
    expect(dialogAfterCancel).toBeFalsy();
    expect(component.locationToDelete).toBeNull();
  });

  it('should delete location and reload page when confirm is clicked', () => {
    // Setup delete response
    locationService.deleteLocation.and.returnValue(of(void 0));

    // Open dialog
    const deleteButton = fixture.debugElement.query(By.css('.delete-btn'));
    deleteButton.triggerEventHandler('click');
    fixture.detectChanges();

    // Get dialog and trigger confirm
    const dialog = fixture.debugElement.query(By.directive(ConfirmationDialogComponent));
    dialog.componentInstance.confirm.emit();
    fixture.detectChanges();

    // Verify delete was called and page was reloaded
    expect(locationService.deleteLocation).toHaveBeenCalledWith('tenant-1', '1');
    expect(locationService.getLocations).toHaveBeenCalledWith('tenant-1', 0);
    expect(component.locationToDelete).toBeNull();
  });

  it('should navigate to create location page when add button is clicked', () => {
    const addButton = fixture.debugElement.query(By.css('.add-location-btn'));
    addButton.triggerEventHandler('click');

    expect(router.navigate).toHaveBeenCalledWith(
      ['/create-location'],
      { queryParams: { tenantId: 'tenant-1' } }
    );
  });
}); 