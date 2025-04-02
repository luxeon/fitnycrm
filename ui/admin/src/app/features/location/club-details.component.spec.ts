import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CommonModule } from '@angular/common';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { Router, ActivatedRoute } from '@angular/router';
import { LocationService } from '../../core/services/location.service';
import { TrainingService } from '../../core/services/training.service';
import { TrainerService } from '../../core/services/trainer.service';
import { ClubDetailsComponent } from './club-details.component';
import { Observable, of } from 'rxjs';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideHttpClient, withFetch } from '@angular/common/http';

class FakeLoader implements TranslateLoader {
  getTranslation(): Observable<any> {
    return of({
      'location.details.title': 'Location Details',
      'common.back': 'Back',
      'location.details.tabs.workouts': 'Workouts',
      'location.details.tabs.trainers': 'Trainers',
      'location.details.tabs.schedule': 'Schedule',
      'location.details.workouts.title': 'Workouts',
      'location.details.workouts.add': 'Add Workout',
      'location.details.workouts.empty': 'No workouts found',
      'location.details.trainers.title': 'Trainers',
      'location.details.trainers.add': 'Add Trainer',
      'location.details.trainers.empty': 'No trainers found',
      'location.details.schedule.title': 'Schedule',
      'location.details.schedule.add': 'Add Schedule',
      'location.details.schedule.empty': 'No schedules found',
      'common.loading': 'Loading...'
    });
  }
}

describe('ClubDetailsComponent', () => {
  let component: ClubDetailsComponent;
  let fixture: ComponentFixture<ClubDetailsComponent>;
  let locationService: jasmine.SpyObj<LocationService>;
  let trainingService: jasmine.SpyObj<TrainingService>;
  let trainerService: jasmine.SpyObj<TrainerService>;
  let router: jasmine.SpyObj<Router>;
  let route: jasmine.SpyObj<ActivatedRoute>;

  const mockLocation = {
    id: '1',
    name: 'Test Location',
    address: '123 Test St',
    city: 'Test City',
    state: 'TS',
    postalCode: '12345',
    country: 'Test Country',
    timezone: 'UTC'
  };

  const mockWorkouts = {
    content: [
      { id: '1', name: 'Workout 1' },
      { id: '2', name: 'Workout 2' }
    ],
    totalElements: 2,
    totalPages: 1,
    size: 10,
    number: 0
  };

  const mockTrainers = {
    content: [
      { id: '1', name: 'Trainer 1' },
      { id: '2', name: 'Trainer 2' }
    ],
    totalElements: 2,
    totalPages: 1,
    size: 10,
    number: 0
  };

  beforeEach(async () => {
    const locationServiceSpy = jasmine.createSpyObj('LocationService', ['getLocation']);
    const trainingServiceSpy = jasmine.createSpyObj('TrainingService', ['getTrainings']);
    const trainerServiceSpy = jasmine.createSpyObj('TrainerService', ['getTrainers']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    const routeSpy = jasmine.createSpyObj('ActivatedRoute', [], {
      snapshot: {
        params: { tenantId: 'tenant-1', locationId: '1' },
        queryParams: { tab: 'workouts' }
      }
    });

    locationServiceSpy.getLocation.and.returnValue(of(mockLocation));
    trainingServiceSpy.getTrainings.and.returnValue(of(mockWorkouts));
    trainerServiceSpy.getTrainers.and.returnValue(of(mockTrainers));

    await TestBed.configureTestingModule({
      imports: [
        ClubDetailsComponent,
        CommonModule,
        TranslateModule.forRoot({
          loader: { provide: TranslateLoader, useClass: FakeLoader }
        })
      ],
      providers: [
        { provide: LocationService, useValue: locationServiceSpy },
        { provide: TrainingService, useValue: trainingServiceSpy },
        { provide: TrainerService, useValue: trainerServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: ActivatedRoute, useValue: routeSpy },
        provideAnimations(),
        provideHttpClient(withFetch())
      ]
    }).compileComponents();

    locationService = TestBed.inject(LocationService) as jasmine.SpyObj<LocationService>;
    trainingService = TestBed.inject(TrainingService) as jasmine.SpyObj<TrainingService>;
    trainerService = TestBed.inject(TrainerService) as jasmine.SpyObj<TrainerService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    route = TestBed.inject(ActivatedRoute) as jasmine.SpyObj<ActivatedRoute>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClubDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
}); 