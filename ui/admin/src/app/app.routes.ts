import { Routes } from '@angular/router';
import { RegistrationComponent } from './features/registration/registration.component';
import { LoginComponent } from './features/auth/login/login.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { adminGuard } from './core/guards/admin.guard';
import { tenantCheckGuard } from './core/guards/tenant-check.guard';
import { CreateTenantComponent } from './features/tenant/create-tenant.component';
import { ClubDetailsComponent } from './features/location/club-details.component';
import { EmailConfirmationComponent } from './features/auth/email-confirmation/email-confirmation.component';
import { ConfirmEmailComponent } from './features/auth/confirm-email/confirm-email.component';
import { ClientDetailsComponent } from './features/dashboard/components/clients/client-details/client-details.component';
import { AppLayoutComponent } from './shared/components/app-layout/app-layout.component';

export const routes: Routes = [
  // Public routes without layout
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
  
  // Authenticated routes with layout
  {
    path: '',
    component: AppLayoutComponent,
    canActivate: [adminGuard],
    children: [
      {
        path: 'create-tenant',
        component: CreateTenantComponent,
        data: { titleKey: 'tenant.create.pageTitle' }
      },
      {
        path: 'tenant/:tenantId/location/:locationId',
        component: ClubDetailsComponent,
        data: { titleKey: 'location.details.pageTitle' }
      },
      {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [tenantCheckGuard],
        data: { titleKey: 'dashboard.pageTitle' }
      },
      {
        path: 'tenants/:tenantId/clients/:clientId',
        component: ClientDetailsComponent,
        data: { titleKey: 'dashboard.clients.details.pageTitle' }
      },
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      }
    ]
  }
];
