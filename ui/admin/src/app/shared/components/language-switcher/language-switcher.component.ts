import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateService } from '@ngx-translate/core';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

interface Language {
  code: string;
  name: string;
  flag: string;
}

@Component({
  selector: 'app-language-switcher',
  standalone: true,
  imports: [CommonModule, MatMenuModule, MatButtonModule, MatIconModule],
  template: `
    <div class="language-switcher">
      <button mat-button [matMenuTriggerFor]="languageMenu" class="language-button">
        <span class="flag">{{ getCurrentLanguage().flag }}</span>
        <span class="lang-code">{{ getCurrentLanguage().code.toUpperCase() }}</span>
        <mat-icon>arrow_drop_down</mat-icon>
      </button>
      <mat-menu #languageMenu="matMenu" xPosition="before">
        @for (language of languages; track language.code) {
          <button mat-menu-item
            (click)="switchLanguage(language.code)"
            [class.active]="currentLang() === language.code">
            <span class="flag">{{ language.flag }}</span>
            <span class="lang-name">{{ language.name }}</span>
          </button>
        }
      </mat-menu>
    </div>
  `,
  styles: [`
    .language-switcher {
      display: inline-block;
    }

    .language-button {
      display: flex;
      align-items: center;
      padding: 4px 8px;
      border-radius: 4px;
      background: transparent;
      border: 1px solid #ddd;
      transition: all 0.2s;
    }

    .flag {
      font-size: 1.2rem;
      margin-right: 4px;
    }

    .lang-code {
      font-size: 0.9rem;
      font-weight: 500;
    }

    .lang-name {
      margin-left: 8px;
    }

    button.active {
      background: #f0f0f0;
    }
  `]
})
export class LanguageSwitcherComponent {
  private translate = inject(TranslateService);
  currentLang = signal(this.translate.currentLang || 'en');

  languages: Language[] = [
    { code: 'en', name: 'English', flag: '🇺🇸' },
    // { code: 'es', name: 'Español', flag: '🇪🇸' },
    { code: 'uk', name: 'Українська', flag: '🇺🇦' }
  ];

  getCurrentLanguage(): Language {
    return this.languages.find(lang => lang.code === this.currentLang()) || this.languages[0];
  }

  switchLanguage(lang: string): void {
    this.translate.use(lang);
    this.currentLang.set(lang);
  }
}
