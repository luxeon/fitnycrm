import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService, UserDetailsResponse } from '../../core/services/auth.service';
import { TenantService, TenantResponse } from '../../core/services/tenant.service';
import { TranslateModule } from '@ngx-translate/core';
import { LocationsComponent } from './components/locations/locations.component';
import { WorkoutListComponent } from '../training/components/workout-list.component';
import { TrainerListComponent } from '../trainer/components/trainer-list.component';
import { TrainingService } from '../../core/services/training.service';
import { TrainerService } from '../../core/services/trainer.service';
import { firstValueFrom } from 'rxjs';
import { ClientsComponent } from './components/clients/clients.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    LocationsComponent,
    WorkoutListComponent,
    TrainerListComponent,
    ClientsComponent
  ],
  template: `
    <div class="dashboard-container">
      <div class="dashboard-content">
        <h1>{{ 'dashboard.welcome' | translate:{ name: userDetails?.firstName || ('dashboard.user' | translate) } }}</h1>
        <div *ngIf="tenantDetails" class="fitness-club">
          <span class="label">{{ 'dashboard.fitnessClub.label' | translate }}</span>
          <h2>{{ tenantDetails.name }}</h2>
        </div>

        <div class="tabs-container" *ngIf="tenantDetails">
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
            <div *ngSwitchCase="'locations'" class="tab-pane">
              <app-locations *ngIf="tenantDetails" [tenantId]="tenantDetails.id"></app-locations>
            </div>

            <div *ngSwitchCase="'workouts'" class="tab-pane">
              <div class="action-header">
                <h3>{{ 'dashboard.workouts.title' | translate }}</h3>
                <button class="action-button" (click)="onAddWorkout()">
                  {{ 'dashboard.workouts.add' | translate }}
                </button>
              </div>
              <div class="loading" *ngIf="isLoadingWorkouts">
                {{ 'common.loading' | translate }}
              </div>
              <div class="empty-state" *ngIf="!isLoadingWorkouts && !workouts?.length">
                {{ 'dashboard.workouts.empty' | translate }}
              </div>
              <app-workout-list
                *ngIf="!isLoadingWorkouts && workouts?.length"
                [workouts]="workouts"
                [currentPage]="currentWorkoutPage"
                [totalPages]="totalWorkoutPages"
                [tenantId]="tenantDetails.id"
                (pageChange)="onWorkoutPageChange($event)"
                (workoutDeleted)="loadWorkouts(tenantDetails.id)">
              </app-workout-list>
            </div>

            <div *ngSwitchCase="'trainers'" class="tab-pane">
              <div class="action-header">
                <h3>{{ 'dashboard.trainers.title' | translate }}</h3>
                <button class="action-button" (click)="onAddTrainer()">
                  {{ 'dashboard.trainers.add' | translate }}
                </button>
              </div>
              <div class="loading" *ngIf="isLoadingTrainers">
                {{ 'common.loading' | translate }}
              </div>
              <div class="empty-state" *ngIf="!isLoadingTrainers && !trainers?.length">
                {{ 'dashboard.trainers.empty' | translate }}
              </div>
              <app-trainer-list
                *ngIf="!isLoadingTrainers && trainers?.length"
                [trainers]="trainers"
                [currentPage]="currentTrainerPage"
                [totalPages]="totalTrainerPages"
                [tenantId]="tenantDetails.id"
                (pageChange)="onTrainerPageChange($event)"
                (trainerDeleted)="loadTrainers(tenantDetails.id)">
              </app-trainer-list>
            </div>

            <div *ngSwitchCase="'clients'" class="tab-pane">
              <app-clients *ngIf="tenantDetails" [tenantId]="tenantDetails.id"></app-clients>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly tenantService = inject(TenantService);
  private readonly trainingService = inject(TrainingService);
  private readonly trainerService = inject(TrainerService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  userDetails: UserDetailsResponse | null = null;
  tenantDetails: TenantResponse | null = null;
  workouts: any[] = [];
  trainers: any[] = [];
  isLoadingWorkouts = false;
  isLoadingTrainers = false;
  currentWorkoutPage = 0;
  totalWorkoutPages = 0;
  currentTrainerPage = 0;
  totalTrainerPages = 0;

  activeTab = 'locations';

  tabs = [
    { id: 'locations', label: 'dashboard.tabs.locations' },
    { id: 'workouts', label: 'dashboard.tabs.workouts' },
    { id: 'trainers', label: 'dashboard.tabs.trainers' },
    { id: 'clients', label: 'dashboard.tabs.clients' }
  ];

  ngOnInit(): void {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return;
    }

    this.userDetails = this.authService.getCurrentUser();

    this.tenantService.getAllForAuthenticatedUser()
      .subscribe(tenants => {
        if (tenants.length > 0) {
          this.tenantService.getById(tenants[0].id)
            .subscribe(tenant => {
              this.tenantDetails = tenant;
              const tab = this.route.snapshot.queryParams['tab'];
              if (tab && this.tabs.some(t => t.id === tab)) {
                this.setActiveTab(tab, true);
              } else {
                this.setActiveTab('locations', false); // Don't update URL during initialization
              }
            });
        }
      });
  }

  setActiveTab(tabId: string, updateUrl: boolean = true): void {
    this.activeTab = tabId;
    if (this.tenantDetails) {
      if (tabId === 'workouts' && !this.workouts.length) {
        this.loadWorkouts(this.tenantDetails.id);
      } else if (tabId === 'trainers' && !this.trainers.length) {
        this.loadTrainers(this.tenantDetails.id);
      }
    }
    // Update URL with the new tab only when requested
    if (updateUrl) {
      this.router.navigate([], {
        relativeTo: this.route,
        queryParams: { tab: tabId },
        queryParamsHandling: 'merge'
      });
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
      console.error('Failed to load workouts:', error);
    } finally {
      this.isLoadingWorkouts = false;
    }
  }

  async loadTrainers(tenantId: string, page: number = 0): Promise<void> {
    this.isLoadingTrainers = true;
    try {
      const response = await firstValueFrom(this.trainerService.getTrainers(tenantId, page));
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
    if (this.tenantDetails) {
      this.loadWorkouts(this.tenantDetails.id, page);
    }
  }

  onTrainerPageChange(page: number): void {
    if (this.tenantDetails) {
      this.loadTrainers(this.tenantDetails.id, page);
    }
  }

  onAddWorkout(): void {
    if (this.tenantDetails) {
      this.router.navigate([`/tenant/${this.tenantDetails.id}/workout/create`]);
    }
  }

  onAddTrainer(): void {
    if (this.tenantDetails) {
      this.router.navigate([`/tenant/${this.tenantDetails.id}/trainer/create`]);
    }
  }
}
