import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, TranslateModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  private translate = inject(TranslateService);

  constructor() {
    // Set default language
    this.translate.setDefaultLang('en');
    this.translate.use('en');

    // Add languages
    this.translate.addLangs(['en']);
  }
}
