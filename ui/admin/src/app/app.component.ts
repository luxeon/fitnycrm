import { Component, inject } from '@angular/core';
import { RouterOutlet, Router, NavigationEnd } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { TranslateService } from '@ngx-translate/core';
import { filter, map, mergeMap } from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  private readonly router = inject(Router);
  private readonly titleService = inject(Title);
  private readonly translateService = inject(TranslateService);

  constructor() {
    this.setupLanguage();
    this.setupTitleUpdates();
  }

  private setupLanguage(): void {
    // Set default language
    this.translateService.setDefaultLang('en');

    const savedLanguage = localStorage.getItem('selectedLanguage');

    if (savedLanguage && savedLanguage.match(/en|uk/)) {
      this.translateService.use(savedLanguage);
    } else {
      const browserLang = this.translateService.getBrowserLang();
      this.translateService.use(browserLang?.match(/en|uk/) ? browserLang : 'en');
    }
  }

  private setupTitleUpdates(): void {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd),
      map(() => {
        let route = this.router.routerState.snapshot.root;
        while (route.firstChild) {
          route = route.firstChild;
        }
        return route;
      }),
      filter(route => route.data['titleKey'] !== undefined),
      mergeMap(route =>
        this.translateService.get(route.data['titleKey'])
      )
    ).subscribe(translatedTitle => {
      this.titleService.setTitle(`${translatedTitle} - Fitavera CRM`);
    });
  }
}
