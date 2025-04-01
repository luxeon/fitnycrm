import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { LocationService, LocationPageItemResponse } from '../../core/services/location.service';
import { TranslateModule } from '@ngx-translate/core';
import { firstValueFrom } from 'rxjs';
import { animate, style, transition, trigger } from '@angular/animations';
import { TrainingService, TrainingPageItemResponse } from '../../core/services/training.service';
import { WorkoutListComponent } from '../training/components/workout-list.component';

@Component({
  selector: 'app-club-details',
  standalone: true,
  imports: [CommonModule, TranslateModule, WorkoutListComponent],
  animations: [
    trigger('fadeInOut', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('300ms ease-out', style({ opacity: 1 }))
      ])
    ])
  ],
  template: `
    <div class="club-details-container">
      <div class="club-details-content" @fadeInOut>
        <div class="header-section">
          <button class="back-button" (click)="onBack()">
            <span class="back-icon">←</span>
            {{ 'common.back' | translate }}
          </button>
          <h2>{{ 'location.details.title' | translate }}</h2>
        </div>

        <div class="address-section" *ngIf="location">
          <h3>{{ location.address }}</h3>
          <p>{{ location.city }}, {{ location.state }} {{ location.postalCode }}</p>
          <p>{{ location.country }}</p>
        </div>

        <div class="loading" *ngIf="isLoading">
          {{ 'common.loading' | translate }}
        </div>

        <div class="tabs-container" *ngIf="!isLoading && location">
          <div class="tabs">
            <button 
              *ngFor="let tab of tabs" 
              [class.active]="activeTab === tab.id"
              (click)="setActiveTab(tab.id)"
              class="tab-button">
              {{ tab.label | translate }}
            </button>
          </div>

          <div class="tab-content" [ngSwitch]="activeTab">
            <div *ngSwitchCase="'workouts'" class="tab-pane">
              <div class="action-header">
                <h3>{{ 'location.details.workouts.title' | translate }}</h3>
                <button class="action-button" (click)="onAddWorkout()">
                  {{ 'location.details.workouts.add' | translate }}
                </button>
              </div>
              <div class="loading" *ngIf="isLoadingWorkouts">
                {{ 'common.loading' | translate }}
              </div>
              <div class="empty-state" *ngIf="!isLoadingWorkouts && !workouts?.length">
                {{ 'location.details.workouts.empty' | translate }}
              </div>
              <app-workout-list 
                *ngIf="!isLoadingWorkouts && workouts?.length" 
                [workouts]="workouts"
                [currentPage]="currentWorkoutPage"
                [totalPages]="totalWorkoutPages"
                [tenantId]="tenantId"
                [locationId]="location?.id || ''"
                (pageChange)="onWorkoutPageChange($event)"
                (workoutDeleted)="loadWorkouts(tenantId)">
              </app-workout-list>
            </div>

            <div *ngSwitchCase="'trainers'" class="tab-pane">
              <div class="action-header">
                <h3>{{ 'location.details.trainers.title' | translate }}</h3>
                <button class="action-button" (click)="onAddTrainer()">
                  {{ 'location.details.trainers.add' | translate }}
                </button>
              </div>
              <div class="empty-state" *ngIf="!hasTrainers">
                {{ 'location.details.trainers.empty' | translate }}
              </div>
              <!-- Trainer list will be added here -->
            </div>

            <div *ngSwitchCase="'schedule'" class="tab-pane">
              <div class="action-header">
                <h3>{{ 'location.details.schedule.title' | translate }}</h3>
                <button class="action-button" (click)="onAddSchedule()">
                  {{ 'location.details.schedule.add' | translate }}
                </button>
              </div>
              <div class="empty-state" *ngIf="!hasSchedules">
                {{ 'location.details.schedule.empty' | translate }}
              </div>
              <!-- Schedule content will be added here -->
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .club-details-container {
      min-height: 100vh;
      padding: 40px;
      background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    }

    .club-details-content {
      background: white;
      padding: 40px;
      border-radius: 12px;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      max-width: 1200px;
      margin: 0 auto;
    }

    .header-section {
      display: flex;
      align-items: center;
      margin-bottom: 32px;
      gap: 24px;
      border-bottom: 1px solid #eee;
      padding-bottom: 24px;

      h2 {
        color: #2c3e50;
        margin: 0;
        font-size: 24px;
        font-weight: 600;
      }
    }

    .back-button {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 8px 0;
      background: none;
      border: none;
      color: #3498db;
      cursor: pointer;
      font-size: 14px;
      transition: color 0.2s;

      .back-icon {
        font-size: 18px;
        line-height: 1;
      }

      &:hover {
        color: #2980b9;
      }
    }

    .address-section {
      margin-bottom: 32px;

      h3 {
        color: #2c3e50;
        margin: 0 0 8px;
        font-size: 20px;
      }

      p {
        color: #34495e;
        margin: 0 0 4px;
        font-size: 16px;
      }
    }

    .loading {
      text-align: center;
      padding: 1rem;
      color: #6c757d;
    }

    .tabs-container {
      margin-top: 24px;
    }

    .tabs {
      display: flex;
      gap: 8px;
      border-bottom: 2px solid #eee;
      margin-bottom: 24px;
    }

    .tab-button {
      padding: 12px 24px;
      background: none;
      border: none;
      border-bottom: 2px solid transparent;
      margin-bottom: -2px;
      color: #7f8c8d;
      cursor: pointer;
      font-size: 16px;
      transition: all 0.2s;

      &:hover {
        color: #2c3e50;
      }

      &.active {
        color: #3498db;
        border-bottom-color: #3498db;
      }
    }

    .tab-content {
      min-height: 400px;
    }

    .tab-pane {
      padding: 24px 0;
    }

    .action-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 24px;

      h3 {
        margin: 0;
        color: #2c3e50;
        font-size: 20px;
      }
    }

    .action-button {
      padding: 8px 16px;
      background: #3498db;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 14px;
      transition: background-color 0.2s;

      &:hover {
        background: #2980b9;
      }
    }

    .empty-state {
      text-align: center;
      padding: 48px;
      background: #f8f9fa;
      border-radius: 8px;
      color: #6c757d;
      font-size: 16px;
    }
  `]
})
export class ClubDetailsComponent implements OnInit {
  private readonly locationService = inject(LocationService);
  private readonly trainingService = inject(TrainingService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  location: LocationPageItemResponse | null = null;
  workouts: TrainingPageItemResponse[] = [];
  isLoading = false;
  isLoadingWorkouts = false;
  activeTab = 'workouts';
  currentWorkoutPage = 0;
  totalWorkoutPages = 0;
  tenantId = '';

  // Temporary flags for empty states
  hasTrainers = false;
  hasSchedules = false;

  tabs = [
    { id: 'workouts', label: 'location.details.tabs.workouts' },
    { id: 'trainers', label: 'location.details.tabs.trainers' },
    { id: 'schedule', label: 'location.details.tabs.schedule' }
  ];

  ngOnInit(): void {
    const tenantId = this.route.snapshot.queryParams['tenantId'];
    const locationId = this.route.snapshot.queryParams['locationId'];

    if (tenantId && locationId) {
      this.tenantId = tenantId;
      this.loadLocation(tenantId, locationId);
      this.loadWorkouts(tenantId);
    } else {
      this.router.navigate(['/dashboard']);
    }
  }

  private async loadLocation(tenantId: string, locationId: string): Promise<void> {
    this.isLoading = true;
    try {
      this.location = await firstValueFrom(this.locationService.getLocation(tenantId, locationId));
    } catch (error) {
      // Handle error appropriately
      this.router.navigate(['/dashboard']);
    } finally {
      this.isLoading = false;
    }
  }

  async loadWorkouts(tenantId: string, page: number = 0): Promise<void> {
    this.isLoadingWorkouts = true;
    try {
      const response = await firstValueFrom(this.trainingService.getTrainings(tenantId, page));
      this.workouts = response.content;
      this.currentWorkoutPage = response.number;
      this.totalWorkoutPages = response.totalPages;
    } catch (error) {
      // Handle error appropriately
      console.error('Failed to load workouts:', error);
    } finally {
      this.isLoadingWorkouts = false;
    }
  }

  onWorkoutPageChange(page: number): void {
    const tenantId = this.route.snapshot.queryParams['tenantId'];
    if (tenantId) {
      this.loadWorkouts(tenantId, page);
    }
  }

  setActiveTab(tabId: string): void {
    this.activeTab = tabId;
  }

  onAddWorkout(): void {
    const tenantId = this.route.snapshot.queryParams['tenantId'];
    const locationId = this.route.snapshot.queryParams['locationId'];
    
    this.router.navigate(['/create-workout'], {
      queryParams: { tenantId, locationId }
    });
  }

  onAddTrainer(): void {
    // To be implemented
  }

  onAddSchedule(): void {
    // To be implemented
  }

  onBack(): void {
    this.router.navigate(['/dashboard']).then(() => {
      // Restore scroll position after navigation
      const savedScrollPos = sessionStorage.getItem('dashboardScrollPos');
      if (savedScrollPos) {
        window.scrollTo(0, parseInt(savedScrollPos, 10));
        sessionStorage.removeItem('dashboardScrollPos'); // Clean up
      }
    });
  }
}