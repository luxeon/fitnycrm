import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

export const httpProviders = [
  provideHttpClient(
    withInterceptorsFromDi()
  )
]; 