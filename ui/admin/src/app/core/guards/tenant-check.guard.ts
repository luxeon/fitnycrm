import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const tenantCheckGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isAuthenticated()) {
    router.navigate(['/login']);
    return false;
  }

  const payload = authService.getCurrentUser();
  if (!payload?.tenantIds?.length) {
    router.navigate(['/create-tenant']);
    return false;
  }

  return true;
}; 