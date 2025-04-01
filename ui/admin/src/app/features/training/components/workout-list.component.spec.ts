import { ComponentFixture, TestBed } from '@angular/core/testing';
import { WorkoutListComponent } from './workout-list.component';
import { Router } from '@angular/router';
import { TrainingService } from '../../../core/services/training.service';
import { TranslateModule } from '@ngx-translate/core';
import { of, throwError } from 'rxjs';

describe('WorkoutListComponent', () => {
  let component: WorkoutListComponent;
  let fixture: ComponentFixture<WorkoutListComponent>;
  let trainingService: jasmine.SpyObj<TrainingService>;
  let router: jasmine.SpyObj<Router>;

  const mockWorkout = {
    id: 'ae4d661a-ed70-4e36-9caf-048ee8060290',
    name: 'Basic Training',
    description: 'Basic training session',
    durationMinutes: 60,
    clientCapacity: 10,
    createdAt: '2024-03-20T10:00:00Z',
    updatedAt: '2024-03-20T10:00:00Z'
  };

  beforeEach(async () => {
    trainingService = jasmine.createSpyObj('TrainingService', ['deleteTraining']);
    router = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [
        WorkoutListComponent,
        TranslateModule.forRoot()
      ],
      providers: [
        { provide: TrainingService, useValue: trainingService },
        { provide: Router, useValue: router }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(WorkoutListComponent);
    component = fixture.componentInstance;
    component.workouts = [mockWorkout];
    component.tenantId = '7a7632b1-e932-48fd-9296-001036b4ec19';
    component.locationId = 'c35ac7f5-3e4f-462a-a76d-524bd3a5fd01';
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to edit workout page when edit button is clicked', () => {
    component.onEditClick(mockWorkout);

    expect(router.navigate).toHaveBeenCalledWith(
      ['/edit-workout'],
      {
        queryParams: {
          tenantId: '7a7632b1-e932-48fd-9296-001036b4ec19',
          locationId: 'c35ac7f5-3e4f-462a-a76d-524bd3a5fd01',
          workoutId: mockWorkout.id
        }
      }
    );
  });

  it('should emit page change event', () => {
    spyOn(component.pageChange, 'emit');
    const newPage = 2;

    component.onPageChange(newPage);

    expect(component.pageChange.emit).toHaveBeenCalledWith(newPage);
  });

  it('should show delete confirmation dialog when delete button is clicked', () => {
    component.onDeleteClick(mockWorkout);

    expect(component.workoutToDelete).toBe(mockWorkout);
  });

  it('should clear workout to delete when cancel is clicked', () => {
    component.workoutToDelete = mockWorkout;
    component.onDeleteCancel();

    expect(component.workoutToDelete).toBeNull();
  });

  it('should delete workout and emit event when confirmed', async () => {
    trainingService.deleteTraining.and.returnValue(of(void 0));
    spyOn(component.workoutDeleted, 'emit');
    component.workoutToDelete = mockWorkout;

    await component.onDeleteConfirm();

    expect(trainingService.deleteTraining).toHaveBeenCalledWith(
      '7a7632b1-e932-48fd-9296-001036b4ec19',
      mockWorkout.id
    );
    expect(component.workoutToDelete).toBeNull();
    expect(component.workoutDeleted.emit).toHaveBeenCalled();
  });

  it('should handle error when deleting workout', async () => {
    trainingService.deleteTraining.and.returnValue(throwError(() => new Error('Failed to delete')));
    spyOn(console, 'error');
    component.workoutToDelete = mockWorkout;

    await component.onDeleteConfirm();

    expect(console.error).toHaveBeenCalled();
  });
}); 