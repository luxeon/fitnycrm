import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { TenantService } from '../services/tenant.service';
import { firstValueFrom } from 'rxjs';

export const tenantCheckGuard: CanActivateFn = async () => {
  const authService = inject(AuthService);
  const tenantService = inject(TenantService);
  const router = inject(Router);

  if (!authService.isAuthenticated()) {
    router.navigate(['/login']);
    return false;
  }

  try {
    const tenants = await firstValueFrom(tenantService.getAllForAuthenticatedUser());
    if (!tenants.length) {
      router.navigate(['/create-tenant']);
      return false;
    }
    return true;
  } catch (error) {
    router.navigate(['/create-tenant']);
    return false;
  }
};
