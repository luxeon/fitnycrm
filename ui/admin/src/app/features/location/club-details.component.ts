import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { LocationService, LocationPageItemResponse } from '../../core/services/location.service';
import { TranslateModule } from '@ngx-translate/core';
import { firstValueFrom } from 'rxjs';
import { animate, style, transition, trigger } from '@angular/animations';
import { WorkoutListComponent } from '../training/components/workout-list.component';
import { TrainerListComponent } from '../trainer/components/trainer-list.component';
import { TrainingService } from '../../core/services/training.service';
import { TrainerService } from '../../core/services/trainer.service';

@Component({
  selector: 'app-club-details',
  standalone: true,
  imports: [CommonModule, TranslateModule, WorkoutListComponent, TrainerListComponent],
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
                [locationId]="locationId"
                (pageChange)="onWorkoutPageChange($event)"
                (workoutDeleted)="loadWorkouts()">
              </app-workout-list>
            </div>

            <div *ngSwitchCase="'trainers'" class="tab-pane">
              <div class="action-header">
                <h3>{{ 'location.details.trainers.title' | translate }}</h3>
                <button class="action-button" (click)="onAddTrainer()">
                  {{ 'location.details.trainers.add' | translate }}
                </button>
              </div>
              <div class="loading" *ngIf="isLoadingTrainers">
                {{ 'common.loading' | translate }}
              </div>
              <div class="empty-state" *ngIf="!isLoadingTrainers && !trainers?.length">
                {{ 'location.details.trainers.empty' | translate }}
              </div>
              <app-trainer-list
                *ngIf="!isLoadingTrainers && trainers?.length"
                [trainers]="trainers"
                [currentPage]="currentTrainerPage"
                [totalPages]="totalTrainerPages"
                [tenantId]="tenantId"
                [locationId]="locationId"
                (pageChange)="onTrainerPageChange($event)"
                (trainerDeleted)="loadTrainers()">
              </app-trainer-list>
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
      margin-bottom: 24px;
      border-bottom: 1px solid #e9ecef;
      padding-bottom: 8px;
    }

    .tab-button {
      padding: 8px 16px;
      background: none;
      border: none;
      border-radius: 4px 4px 0 0;
      color: #6c757d;
      cursor: pointer;
      font-size: 14px;
      transition: all 0.2s;
      position: relative;

      &:hover {
        color: #3498db;
      }

      &.active {
        color: #3498db;
        font-weight: 500;

        &::after {
          content: '';
          position: absolute;
          bottom: -9px;
          left: 0;
          right: 0;
          height: 2px;
          background-color: #3498db;
        }
      }
    }

    .tab-content {
      min-height: 300px;
    }

    .tab-pane {
      animation: fadeIn 0.3s ease-out;
    }

    @keyframes fadeIn {
      from {
        opacity: 0;
        transform: translateY(10px);
      }
      to {
        opacity: 1;
        transform: translateY(0);
      }
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
  private readonly trainerService = inject(TrainerService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  location: LocationPageItemResponse | null = null;
  isLoading = false;
  hasSchedules = false;
  tenantId = '';
  locationId = '';

  // Workouts state
  workouts: any[] = [];
  isLoadingWorkouts = false;
  currentWorkoutPage = 0;
  totalWorkoutPages = 0;

  // Trainers state
  trainers: any[] = [];
  isLoadingTrainers = false;
  currentTrainerPage = 0;
  totalTrainerPages = 0;

  activeTab = 'workouts';

  tabs = [
    { id: 'workouts', label: 'location.details.tabs.workouts' },
    { id: 'trainers', label: 'location.details.tabs.trainers' },
    { id: 'schedule', label: 'location.details.tabs.schedule' }
  ];

  ngOnInit(): void {
    const params = this.route.snapshot.params;
    this.tenantId = params['tenantId'];
    this.locationId = params['locationId'];

    if (this.tenantId && this.locationId) {
      this.loadLocation(this.tenantId, this.locationId);
      const tab = this.route.snapshot.queryParams['tab'];
      if (tab && this.tabs.some(t => t.id === tab)) {
        this.setActiveTab(tab);
      }
    } else {
      this.router.navigate(['/dashboard']);
    }
  }

  private async loadLocation(tenantId: string, locationId: string): Promise<void> {
    this.isLoading = true;
    try {
      this.location = await firstValueFrom(this.locationService.getLocation(tenantId, locationId));
      this.loadWorkouts();
      this.loadTrainers();
    } catch (error) {
      // Handle error appropriately
      this.router.navigate(['/dashboard']);
    } finally {
      this.isLoading = false;
    }
  }

  setActiveTab(tabId: string): void {
    this.activeTab = tabId;
    // Update URL with the new tab
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { tab: tabId },
      queryParamsHandling: 'merge'
    });
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

  async loadWorkouts(page: number = 0): Promise<void> {
    this.isLoadingWorkouts = true;
    try {
      const response = await firstValueFrom(this.trainingService.getTrainings(this.tenantId, page));
      this.workouts = response.content;
      this.currentWorkoutPage = response.number;
      this.totalWorkoutPages = response.totalPages;
    } catch (error) {
      console.error('Failed to load workouts:', error);
    } finally {
      this.isLoadingWorkouts = false;
    }
  }

  async loadTrainers(page: number = 0): Promise<void> {
    this.isLoadingTrainers = true;
    try {
      const response = await firstValueFrom(this.trainerService.getTrainers(this.tenantId, page));
      this.trainers = response.content;
      this.currentTrainerPage = response.number;
      this.totalTrainerPages = response.totalPages;
    } catch (error) {
      console.error('Failed to load trainers:', error);
    } finally {
      this.isLoadingTrainers = false;
    }
  }

  onWorkoutPageChange(page: number): void {
    this.loadWorkouts(page);
  }

  onTrainerPageChange(page: number): void {
    this.loadTrainers(page);
  }

  onAddWorkout(): void {
    this.router.navigate([`/tenant/${this.tenantId}/workout/create`], {
      state: { returnUrl: this.router.url },
      queryParams: { locationId: this.locationId }
    });
  }

  onAddTrainer(): void {
    this.router.navigate([`/tenant/${this.tenantId}/trainer/create`], {
      state: { returnUrl: this.router.url },
      queryParams: { locationId: this.locationId }
    });
  }

  onAddSchedule(): void {
    this.router.navigate([`/tenant/${this.tenantId}/location/${this.locationId}/schedule/create`], {
      state: { returnUrl: this.router.url }
    });
  }
}
