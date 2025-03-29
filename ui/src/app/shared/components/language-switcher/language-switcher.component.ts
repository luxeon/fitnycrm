import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-language-switcher',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="language-switcher">
      <button 
        (click)="switchLanguage('en')" 
        [class.active]="currentLang === 'en'">
        🇺🇸 EN
      </button>
      <button 
        (click)="switchLanguage('es')" 
        [class.active]="currentLang === 'es'">
        🇪🇸 ES
      </button>
    </div>
  `,
  styles: [`
    .language-switcher {
      display: flex;
      gap: 8px;
    }
    button {
      padding: 8px 16px;
      border: 1px solid #ddd;
      border-radius: 4px;
      background: white;
      cursor: pointer;
      transition: all 0.2s;
    }
    button:hover {
      background: #f5f5f5;
    }
    button.active {
      background: #e0e0e0;
      border-color: #bbb;
    }
  `]
})
export class LanguageSwitcherComponent {
  private translate = inject(TranslateService);
  currentLang = this.translate.currentLang;

  switchLanguage(lang: string) {
    this.translate.use(lang);
    this.currentLang = lang;
  }
} 