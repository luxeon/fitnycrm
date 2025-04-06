import { Routes } from '@angular/router';
import { RegistrationComponent } from './features/registration/registration.component';
import { LoginComponent } from './features/auth/login/login.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { adminGuard } from './core/guards/admin.guard';
import { tenantCheckGuard } from './core/guards/tenant-check.guard';
import { CreateTenantComponent } from './features/tenant/create-tenant.component';
import { ClubDetailsComponent } from './features/location/club-details.component';
import { CreateWorkoutComponent } from './features/training/create-workout.component';
import { EditWorkoutComponent } from './features/training/edit-workout.component';
import { EmailConfirmationComponent } from './features/auth/email-confirmation/email-confirmation.component';
import { ConfirmEmailComponent } from './features/auth/confirm-email/confirm-email.component';

export const routes: Routes = [
  {
    path: 'register',
    component: RegistrationComponent,
    data: { titleKey: 'registration.pageTitle' }
  },
  {
    path: 'login',
    component: LoginComponent,
    data: { titleKey: 'login.pageTitle' }
  },
  {
    path: 'email-confirmation',
    component: EmailConfirmationComponent,
    data: { titleKey: 'emailConfirmation.pageTitle' }
  },
  {
    path: 'auth/confirm-email',
    component: ConfirmEmailComponent,
    data: { titleKey: 'confirmEmail.pageTitle' }
  },
  {
    path: 'create-tenant',
    component: CreateTenantComponent,
    canActivate: [adminGuard],
    data: { titleKey: 'tenant.create.pageTitle' }
  },
  {
    path: 'tenant/:tenantId/location/:locationId',
    component: ClubDetailsComponent,
    canActivate: [adminGuard],
    data: { titleKey: 'location.details.pageTitle' }
  },
  {
    path: 'tenant/:tenantId/workout/create',
    component: CreateWorkoutComponent,
    canActivate: [adminGuard],
    data: { titleKey: 'training.create.pageTitle' }
  },
  {
    path: 'tenant/:tenantId/workout/:workoutId/edit',
    component: EditWorkoutComponent,
    canActivate: [adminGuard],
    data: { titleKey: 'training.edit.pageTitle' }
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [adminGuard, tenantCheckGuard],
    data: { titleKey: 'dashboard.pageTitle' }
  },
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  }
];
