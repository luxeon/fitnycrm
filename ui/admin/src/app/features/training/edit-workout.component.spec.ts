import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { EditWorkoutComponent } from './edit-workout.component';
import { ActivatedRoute, Router, ActivatedRouteSnapshot, Data, convertToParamMap } from '@angular/router';
import { TrainingService } from '../../core/services/training.service';
import { of, throwError } from 'rxjs';
import { TranslateModule } from '@ngx-translate/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('EditWorkoutComponent', () => {
  let component: EditWorkoutComponent;
  let fixture: ComponentFixture<EditWorkoutComponent>;
  let trainingService: jasmine.SpyObj<TrainingService>;
  let router: jasmine.SpyObj<Router>;
  let route: Partial<ActivatedRoute>;

  const mockWorkout = {
    id: 'ae4d661a-ed70-4e36-9caf-048ee8060290',
    name: 'Basic Training',
    description: 'Basic training session',
    durationMinutes: 60,
    clientCapacity: 10,
    createdAt: '2024-03-20T10:00:00Z',
    updatedAt: '2024-03-20T10:00:00Z'
  };

  const mockRouteSnapshot: Partial<ActivatedRouteSnapshot> = {
    url: [],
    params: {},
    queryParams: {
      tenantId: '7a7632b1-e932-48fd-9296-001036b4ec19',
      locationId: 'c35ac7f5-3e4f-462a-a76d-524bd3a5fd01',
      workoutId: 'ae4d661a-ed70-4e36-9caf-048ee8060290'
    },
    fragment: null,
    data: {} as Data,
    outlet: 'primary',
    component: null,
    routeConfig: null,
    root: {} as ActivatedRouteSnapshot,
    parent: null,
    firstChild: null,
    children: [],
    pathFromRoot: [],
    paramMap: convertToParamMap({}),
    queryParamMap: convertToParamMap({
      tenantId: '7a7632b1-e932-48fd-9296-001036b4ec19',
      locationId: 'c35ac7f5-3e4f-462a-a76d-524bd3a5fd01',
      workoutId: 'ae4d661a-ed70-4e36-9caf-048ee8060290'
    })
  };

  beforeEach(async () => {
    trainingService = jasmine.createSpyObj('TrainingService', ['getTraining', 'updateTraining']);
    router = jasmine.createSpyObj('Router', ['navigate']);
    route = {
      snapshot: mockRouteSnapshot as ActivatedRouteSnapshot
    };

    await TestBed.configureTestingModule({
      imports: [
        EditWorkoutComponent,
        TranslateModule.forRoot(),
        ReactiveFormsModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: TrainingService, useValue: trainingService },
        { provide: Router, useValue: router },
        { provide: ActivatedRoute, useValue: route }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(EditWorkoutComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load workout data on init', fakeAsync(() => {
    trainingService.getTraining.and.returnValue(of(mockWorkout));

    fixture.detectChanges();
    tick();

    expect(trainingService.getTraining).toHaveBeenCalledWith(
      '7a7632b1-e932-48fd-9296-001036b4ec19',
      'ae4d661a-ed70-4e36-9caf-048ee8060290'
    );
    expect(component.workoutForm.value).toEqual({
      name: mockWorkout.name,
      description: mockWorkout.description,
      durationMinutes: mockWorkout.durationMinutes,
      clientCapacity: mockWorkout.clientCapacity
    });
    expect(component.isLoading).toBeFalse();
  }));

  it('should handle error when loading workout', fakeAsync(() => {
    spyOn(console, 'error');
    trainingService.getTraining.and.returnValue(throwError(() => new Error('Failed to load')));

    fixture.detectChanges();
    tick();

    expect(component.errorMessage).toBeTruthy();
    expect(component.isLoading).toBeFalse();
    expect(console.error).toHaveBeenCalledWith('Failed to load workout:', jasmine.any(Error));
  }));

  it('should update workout when form is valid', fakeAsync(() => {
    trainingService.getTraining.and.returnValue(of(mockWorkout));
    trainingService.updateTraining.and.returnValue(of(mockWorkout));

    fixture.detectChanges();
    tick();

    const updatedWorkout = {
      name: 'Updated Training',
      description: 'Updated description',
      durationMinutes: 90,
      clientCapacity: 15
    };

    component.workoutForm.patchValue(updatedWorkout);
    component.onSubmit();
    tick();

    expect(trainingService.updateTraining).toHaveBeenCalledWith(
      '7a7632b1-e932-48fd-9296-001036b4ec19',
      'ae4d661a-ed70-4e36-9caf-048ee8060290',
      updatedWorkout
    );
    expect(router.navigate).toHaveBeenCalledWith(
      ['/club-details'],
      { queryParams: {
        tenantId: '7a7632b1-e932-48fd-9296-001036b4ec19',
        locationId: 'c35ac7f5-3e4f-462a-a76d-524bd3a5fd01'
      }}
    );
  }));

  it('should handle error when updating workout', fakeAsync(() => {
    trainingService.getTraining.and.returnValue(of(mockWorkout));
    trainingService.updateTraining.and.returnValue(throwError(() => ({ status: 500 })));

    fixture.detectChanges();
    tick();

    component.onSubmit();
    tick();

    expect(component.errorMessage).toBeTruthy();
    expect(component.isSaving).toBeFalse();
  }));

  it('should handle unauthorized error when updating workout', fakeAsync(() => {
    trainingService.getTraining.and.returnValue(of(mockWorkout));
    trainingService.updateTraining.and.returnValue(throwError(() => ({ status: 401 })));

    fixture.detectChanges();
    tick();

    component.onSubmit();
    tick();

    expect(component.errorMessage).toContain('Authentication failed');
    expect(component.isSaving).toBeFalse();
  }));

  it('should navigate back on cancel', () => {
    component.onCancel();

    expect(router.navigate).toHaveBeenCalledWith(
      ['/club-details'],
      { queryParams: {
        tenantId: '7a7632b1-e932-48fd-9296-001036b4ec19',
        locationId: 'c35ac7f5-3e4f-462a-a76d-524bd3a5fd01'
      }}
    );
  });

  it('should validate required fields', fakeAsync(() => {
    trainingService.getTraining.and.returnValue(of(mockWorkout));

    fixture.detectChanges();
    tick();

    component.workoutForm.patchValue({
      name: '',
      description: 'Test',
      durationMinutes: null,
      clientCapacity: null
    });

    expect(component.workoutForm.valid).toBeFalse();
    expect(component.workoutForm.get('name')?.errors?.['required']).toBeTruthy();
    expect(component.workoutForm.get('durationMinutes')?.errors?.['required']).toBeTruthy();
    expect(component.workoutForm.get('clientCapacity')?.errors?.['required']).toBeTruthy();
  }));

  it('should validate minimum values', fakeAsync(() => {
    trainingService.getTraining.and.returnValue(of(mockWorkout));

    fixture.detectChanges();
    tick();

    component.workoutForm.patchValue({
      name: 'Test',
      description: 'Test',
      durationMinutes: 0,
      clientCapacity: 0
    });

    expect(component.workoutForm.valid).toBeFalse();
    expect(component.workoutForm.get('durationMinutes')?.errors?.['min']).toBeTruthy();
    expect(component.workoutForm.get('clientCapacity')?.errors?.['min']).toBeTruthy();
  }));
});
