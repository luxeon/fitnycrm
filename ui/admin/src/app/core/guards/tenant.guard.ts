import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const tenantGuard: CanActivateFn = (route) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isAuthenticated()) {
    router.navigate(['/login']);
    return false;
  }

  const tenantId = route.params['tenantId'];
  if (!tenantId || !authService.hasTenant(tenantId)) {
    router.navigate(['/dashboard']);
    return false;
  }

  return true;
}; 