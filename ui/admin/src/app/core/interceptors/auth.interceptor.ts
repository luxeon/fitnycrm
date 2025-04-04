import { HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';

const publicEndpoints = [
  `/auth/login`,
  `/auth/signup`,
  `/auth/confirm-email`
];

export const authInterceptor: HttpInterceptorFn = (
  request: HttpRequest<unknown>,
  next: HttpHandlerFn
) => {
  // Skip adding token for public endpoints
  if (publicEndpoints.some(endpoint => request.url.includes(endpoint))) {
    return next(request);
  }

  const token = localStorage.getItem('accessToken');

  if (token) {
    request = request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(request);
};
