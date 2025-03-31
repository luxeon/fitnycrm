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

export const routes: Routes = [
  { path: 'register', component: RegistrationComponent },
  { path: 'login', component: LoginComponent },
  { path: 'create-tenant', component: CreateTenantComponent, canActivate: [adminGuard] },
  { path: 'create-location', component: CreateLocationComponent, canActivate: [adminGuard] },
  { path: 'edit-location', component: EditLocationComponent, canActivate: [adminGuard] },
  { path: 'club-details', component: ClubDetailsComponent, canActivate: [adminGuard] },
  { path: 'dashboard', component: DashboardComponent, canActivate: [adminGuard, tenantCheckGuard] },
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' }
];
