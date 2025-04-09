import { Routes } from '@angular/router';
import { ClientSignupComponent } from './client-signup/client-signup.component';

export const routes: Routes = [
  { 
    path: 'tenants/:tenantId/clients/signup/:inviteId', 
    component: ClientSignupComponent 
  },
  {
    path: '**',
    redirectTo: '/'
  }
];
