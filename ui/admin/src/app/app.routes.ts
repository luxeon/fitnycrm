import { Routes } from '@angular/router';
import { RegistrationComponent } from './features/registration/registration.component';
import { LoginComponent } from './features/auth/login/login.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { adminGuard } from './core/guards/admin.guard';
import { tenantCheckGuard } from './core/guards/tenant-check.guard';
import { CreateTenantComponent } from './features/tenant/create-tenant.component';
import { CreateLocationComponent } from './features/location/create-location.component';
import { EditLocationComponent } from './features/location/edit-location.component';
import { ClubDetailsComponent } from './features/location/club-details.component';
import { CreateWorkoutComponent } from './features/training/create-workout.component';
import { EditWorkoutComponent } from './features/training/edit-workout.component';
import { EmailConfirmationComponent } from './features/auth/email-confirmation/email-confirmation.component';

export const routes: Routes = [
  { path: 'register', component: RegistrationComponent },
  { path: 'login', component: LoginComponent },
  { path: 'email-confirmation', component: EmailConfirmationComponent },
  { path: 'create-tenant', component: CreateTenantComponent, canActivate: [adminGuard] },
  { path: 'create-location', component: CreateLocationComponent, canActivate: [adminGuard] },
  { path: 'edit-location', component: EditLocationComponent, canActivate: [adminGuard] },
  { path: 'club-details', component: ClubDetailsComponent, canActivate: [adminGuard] },
  { path: 'create-workout', component: CreateWorkoutComponent, canActivate: [adminGuard] },
  { path: 'edit-workout', component: EditWorkoutComponent, canActivate: [adminGuard] },
  { path: 'dashboard', component: DashboardComponent, canActivate: [adminGuard, tenantCheckGuard] },
  { path: 'create-trainer', loadComponent: () => import('./features/trainer/create-trainer.component').then(m => m.CreateTrainerComponent), canActivate: [adminGuard] },
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' }
];
