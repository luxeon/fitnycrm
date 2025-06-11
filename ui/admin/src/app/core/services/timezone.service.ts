import { Injectable, inject } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
  providedIn: 'root'
})
export class TimezoneService {
  private readonly translate = inject(TranslateService);

  private readonly timezoneValues = [
    'America/New_York',
    'America/Chicago',
    'America/Denver',
    'America/Los_Angeles',
    'America/Anchorage',
    'Pacific/Honolulu',
    'America/Phoenix',
    'Europe/Madrid',
    'Europe/London',
    'Europe/Paris',
    'Europe/Berlin',
    'Europe/Warsaw',
    'Europe/Kyiv',
    'Europe/Moscow',
    'Asia/Dubai',
    'Asia/Shanghai',
    'Asia/Tokyo',
    'Australia/Sydney',
    'Pacific/Auckland'
  ];

  getTimezones() {
    return this.timezoneValues.map(value => ({
      value,
      label: this.translate.instant(`location.timezones.${value}`)
    }));
  }

  getTimezoneLabel(value: string): string {
    return this.translate.instant(`location.timezones.${value}`, { defaultValue: value });
  }

  getUserTimezone(): string {
    try {
      const userTimezone = Intl.DateTimeFormat().resolvedOptions().timeZone;

      // First try to find exact match
      const exactMatch = this.timezoneValues.find(tz => tz === userTimezone);
      if (exactMatch) {
        return exactMatch;
      }

      // If no exact match, try to find timezone in same region
      const userRegion = userTimezone.split('/')[0];
      const sameRegion = this.timezoneValues.find(tz => tz.startsWith(userRegion + '/'));
      if (sameRegion) {
        return sameRegion;
      }

      // Default to Madrid if no match found
      return 'Europe/Madrid';
    } catch {
      // Fallback to Madrid if browser API fails
      return 'Europe/Madrid';
    }
  }
}
