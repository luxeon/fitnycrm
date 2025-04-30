import { Routes } from '@angular/router';
import { ClientSignupComponent } from './client-signup/client-signup.component';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { authGuard } from './core/guards/auth.guard';
import { ClientInvitationComponent } from './client-invitation/client-invitation.component';
import { LocationListComponent } from './features/locations/location-list.component';
import { ScheduleListComponent } from './features/schedules/schedule-list.component';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'signup',
    component: ClientSignupComponent
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [authGuard]
  },
  {
    path: 'tenants/:tenantId/clients/signup/:inviteId',
    component: ClientInvitationComponent
  },
  {
    path: 'tenants/:tenantId/clients/join/:inviteId',
    component: ClientInvitationComponent,
    canActivate: [authGuard]
  },
  {
    path: 'tenant/:tenantId/locations',
    component: LocationListComponent,
    canActivate: [authGuard]
  },
  {
    path: 'tenant/:tenantId/locations/:locationId/schedules',
    component: ScheduleListComponent,
    canActivate: [authGuard]
  },
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: '/login'
  }
];
