import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { animate, style, transition, trigger } from '@angular/animations';

@Component({
  selector: 'app-tariffs',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  animations: [
    trigger('fadeInOut', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('300ms ease-out', style({ opacity: 1 }))
      ])
    ])
  ],
  template: `
    <div class="tariffs-section" @fadeInOut>
      <div class="action-header">
        <h3>{{ 'dashboard.tariffs.title' | translate }}</h3>
        <button class="action-button" (click)="onAddTariff()">
          {{ 'dashboard.tariffs.add' | translate }}
        </button>
      </div>

      <div class="loading" *ngIf="isLoading">
        {{ 'common.loading' | translate }}
      </div>

      <div class="empty-state" *ngIf="!isLoading && !tariffs?.length">
        {{ 'dashboard.tariffs.empty' | translate }}
      </div>

      <div class="tariffs-grid" *ngIf="!isLoading && tariffs?.length">
        <div class="tariff-card" *ngFor="let tariff of tariffs">
          <div class="card-actions">
            <button class="edit-btn" (click)="onEditTariff(tariff)">
              <span class="edit-icon">✎</span>
            </button>
            <button class="delete-btn" (click)="onDeleteTariff(tariff)">
              <span class="delete-icon">×</span>
            </button>
          </div>
          <div class="tariff-info">
            <h4 class="name">{{ tariff.name }}</h4>
            <div class="price">{{ tariff.price | currency }}</div>
            <div class="duration">{{ tariff.duration }} {{ 'common.days' | translate }}</div>
            <div class="description">{{ tariff.description }}</div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .tariffs-section {
      padding: 0;
    }

    .action-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 24px;

      h3 {
        margin: 0;
        color: #2c3e50;
        font-size: 20px;
      }
    }

    .action-button {
      padding: 8px 16px;
      background: #3498db;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 14px;
      transition: background-color 0.2s;

      &:hover {
        background: #2980b9;
      }
    }

    .loading {
      text-align: center;
      padding: 48px;
      color: #6c757d;
      font-size: 16px;
    }

    .empty-state {
      text-align: center;
      padding: 48px;
      background: #f8f9fa;
      border-radius: 8px;
      color: #6c757d;
      font-size: 16px;
    }

    .tariffs-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 1.5rem;
      margin-bottom: 1.5rem;
    }

    .tariff-card {
      position: relative;
      padding: 1.5rem;
      background: #f8f9fa;
      border-radius: 8px;
      border: 1px solid #e9ecef;
      transition: transform 0.2s, box-shadow 0.2s;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        background: #f1f3f5;
      }

      .card-actions {
        position: absolute;
        top: 12px;
        right: 12px;
        display: flex;
        gap: 8px;
      }

      .edit-btn, .delete-btn {
        width: 24px;
        height: 24px;
        border-radius: 50%;
        border: none;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        transition: background-color 0.2s;
      }

      .edit-btn {
        background: #3498db;
        color: white;

        .edit-icon {
          font-size: 14px;
          line-height: 1;
        }

        &:hover {
          background: #2980b9;
        }
      }

      .delete-btn {
        background: #e74c3c;
        color: white;

        .delete-icon {
          font-size: 18px;
          line-height: 1;
        }

        &:hover {
          background: #c0392b;
        }
      }

      .tariff-info {
        .name {
          font-size: 1.25rem;
          font-weight: 600;
          color: #2c3e50;
          margin: 0 0 0.5rem;
        }

        .price {
          font-size: 1.5rem;
          font-weight: 700;
          color: #3498db;
          margin-bottom: 0.5rem;
        }

        .duration {
          color: #495057;
          font-size: 0.9rem;
          margin-bottom: 0.5rem;
        }

        .description {
          color: #6c757d;
          font-size: 0.9rem;
          line-height: 1.4;
        }
      }
    }
  `]
})
export class TariffsComponent implements OnInit {
  @Input() tenantId!: string;

  isLoading = false;
  tariffs: any[] = []; // TODO: Replace with proper interface

  ngOnInit(): void {
    this.loadTariffs();
  }

  loadTariffs(): void {
    this.isLoading = true;
    // TODO: Implement tariff service and load tariffs
    this.isLoading = false;
    this.tariffs = [];
  }

  onAddTariff(): void {
    // TODO: Implement add tariff functionality
  }

  onEditTariff(tariff: any): void {
    // TODO: Implement edit tariff functionality
  }

  onDeleteTariff(tariff: any): void {
    // TODO: Implement delete tariff functionality
  }
}
