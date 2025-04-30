import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LocationListComponent } from './location-list.component';
import { LocationService } from '../../core/services/location.service';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { of } from 'rxjs';
import { Page } from '../../core/models/page.model';
import { LocationPageItemResponse } from '../../core/models/location.model';

class FakeLoader implements TranslateLoader {
  getTranslation() {
    return of({});
  }
}

describe('LocationListComponent', () => {
  let component: LocationListComponent;
  let fixture: ComponentFixture<LocationListComponent>;
  let locationService: jasmine.SpyObj<LocationService>;

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
    totalPages: 1,
    totalElements: 2,
    size: 10,
    number: 0,
    first: true,
    last: true,
    empty: false
  };

  beforeEach(async () => {
    const locationServiceSpy = jasmine.createSpyObj('LocationService', ['getLocations']);
    locationServiceSpy.getLocations.and.returnValue(of(mockLocations));

    await TestBed.configureTestingModule({
      imports: [
        LocationListComponent,
        TranslateModule.forRoot({
          loader: { provide: TranslateLoader, useClass: FakeLoader }
        })
      ],
      providers: [
        { provide: LocationService, useValue: locationServiceSpy }
      ]
    }).compileComponents();

    locationService = TestBed.inject(LocationService) as jasmine.SpyObj<LocationService>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LocationListComponent);
    component = fixture.componentInstance;
    component.tenantId = 'tenant-1';
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
    const locationCards = fixture.nativeElement.querySelectorAll('.location-card');
    expect(locationCards.length).toBe(2);

    const firstLocation = locationCards[0];
    expect(firstLocation.textContent).toContain('123 Main St');
    expect(firstLocation.textContent).toContain('New York, NY 10001');
    expect(firstLocation.textContent).toContain('USA');
  });

  it('should show loading state', () => {
    component.isLoading = true;
    fixture.detectChanges();

    const loadingElement = fixture.nativeElement.querySelector('.loading');
    expect(loadingElement).toBeTruthy();
  });

  it('should show empty state when no locations', () => {
    component.locations = {
      ...mockLocations,
      content: [],
      totalElements: 0,
      empty: true
    };
    fixture.detectChanges();

    const emptyState = fixture.nativeElement.querySelector('.empty-state');
    expect(emptyState).toBeTruthy();
  });

  it('should handle pagination', () => {
    component.locations = {
      ...mockLocations,
      totalPages: 3,
      number: 1,
      first: false,
      last: false
    };
    fixture.detectChanges();

    const prevButton = fixture.nativeElement.querySelector('.pagination button:first-child');
    const nextButton = fixture.nativeElement.querySelector('.pagination button:last-child');
    const pageInfo = fixture.nativeElement.querySelector('.page-info');

    expect(prevButton.disabled).toBeFalse();
    expect(nextButton.disabled).toBeFalse();
    expect(pageInfo.textContent).toContain('2');
    expect(pageInfo.textContent).toContain('3');

    prevButton.click();
    expect(locationService.getLocations).toHaveBeenCalledWith('tenant-1', 0);

    nextButton.click();
    expect(locationService.getLocations).toHaveBeenCalledWith('tenant-1', 2);
  });
}); 