import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TimezoneService {
  private readonly timezones = [
    { value: 'America/New_York', label: 'Eastern Time (US & Canada)' },
    { value: 'America/Chicago', label: 'Central Time (US & Canada)' },
    { value: 'America/Denver', label: 'Mountain Time (US & Canada)' },
    { value: 'America/Los_Angeles', label: 'Pacific Time (US & Canada)' },
    { value: 'America/Anchorage', label: 'Alaska' },
    { value: 'Pacific/Honolulu', label: 'Hawaii' },
    { value: 'America/Phoenix', label: 'Arizona' },
    { value: 'Europe/Madrid', label: 'Madrid' },
    { value: 'Europe/London', label: 'London' },
    { value: 'Europe/Paris', label: 'Paris' },
    { value: 'Europe/Berlin', label: 'Berlin' },
    { value: 'Europe/Warsaw', label: 'Warsaw' },
    { value: 'Europe/Kyiv', label: 'Kyiv' },
    { value: 'Europe/Moscow', label: 'Moscow' },
    { value: 'Asia/Dubai', label: 'Dubai' },
    { value: 'Asia/Shanghai', label: 'Shanghai' },
    { value: 'Asia/Tokyo', label: 'Tokyo' },
    { value: 'Australia/Sydney', label: 'Sydney' },
    { value: 'Pacific/Auckland', label: 'Auckland' }
  ];

  getTimezones() {
    return this.timezones;
  }

  getTimezoneLabel(value: string): string {
    const timezone = this.timezones.find(tz => tz.value === value);
    return timezone?.label || value;
  }

  getUserTimezone(): string {
    try {
      const userTimezone = Intl.DateTimeFormat().resolvedOptions().timeZone;

      // First try to find exact match
      const exactMatch = this.timezones.find(tz => tz.value === userTimezone);
      if (exactMatch) {
        return exactMatch.value;
      }

      // If no exact match, try to find timezone in same region
      const userRegion = userTimezone.split('/')[0];
      const sameRegion = this.timezones.find(tz => tz.value.startsWith(userRegion + '/'));
      if (sameRegion) {
        return sameRegion.value;
      }

      // Default to New York if no match found
      return 'Europe/Madrid';
    } catch {
      // Fallback to New York if browser API fails
      return 'Europe/Madrid';
    }
  }
}
