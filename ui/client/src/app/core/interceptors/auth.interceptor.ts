import { inject } from '@angular/core';
import { HttpRequest, HttpHandlerFn, HttpInterceptorFn } from '@angular/common/http';
import { AuthService } from '../services/auth.service';

const publicEndpoints = [
  '/api/auth/login',
  '/api/auth/signup',
];

const isPublicEndpoint = (url: string): boolean => {
  return publicEndpoints.some(endpoint => url.includes(endpoint)) ||
         /\/api\/tenants\/[^/]+\/clients\/signup\/[^/]+/.test(url);
};

export const authInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn) => {
  // Skip adding token for public endpoints
  if (isPublicEndpoint(req.url)) {
    return next(req);
  }

  const authService = inject(AuthService);
  const token = authService.getToken();

  if (token) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(authReq);
  }

  return next(req);
};
