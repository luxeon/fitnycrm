import { HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (
  request: HttpRequest<unknown>,
  next: HttpHandlerFn
) => {
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