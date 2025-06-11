import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { AppHeaderComponent } from './shared/components/app-header/app-header.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, TranslateModule, AppHeaderComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  private translate = inject(TranslateService);

  constructor() {
    // Set default language
    this.translate.setDefaultLang('en');
    
    // Use browser language if available, otherwise use default
    const browserLang = this.translate.getBrowserLang();
    this.translate.use(browserLang?.match(/en|uk/) ? browserLang : 'en');

    // Add languages
    this.translate.addLangs(['en', 'uk']);
  }
}
