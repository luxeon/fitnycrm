import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { AuthService } from '../core/services/auth.service';
import { ClientInvitationService } from '../core/services/client-invitation.service';
import { InvitationStorageService } from '../core/services/invitation-storage.service';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-client-invitation',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatProgressSpinnerModule,
    MatCardModule,
    MatButtonModule
  ],
  template: `
    <div class="invitation-container">
      <mat-card *ngIf="isLoading">
        <mat-card-content class="centered-content">
          <mat-spinner diameter="40"></mat-spinner>
          <p>Processing your invitation...</p>
        </mat-card-content>
      </mat-card>
      
      <mat-card *ngIf="!isLoading && successMessage">
        <mat-card-content class="centered-content">
          <h2>Success!</h2>
          <p>{{ successMessage }}</p>
          <button mat-raised-button color="primary" routerLink="/dashboard">Go to Dashboard</button>
        </mat-card-content>
      </mat-card>
      
      <mat-card *ngIf="!isLoading && errorMessage">
        <mat-card-content class="centered-content">
          <h2>Error</h2>
          <p>{{ errorMessage }}</p>
          <button mat-raised-button color="primary" routerLink="/dashboard">Go to Dashboard</button>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .invitation-container {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 80vh;
    }
    
    mat-card {
      min-width: 400px;
      padding: 20px;
    }
    
    .centered-content {
      display: flex;
      flex-direction: column;
      align-items: center;
      text-align: center;
      gap: 16px;
    }
  `]
})
export class ClientInvitationComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private authService = inject(AuthService);
  private clientInvitationService = inject(ClientInvitationService);
  private invitationStorageService = inject(InvitationStorageService);
  
  isLoading = true;
  successMessage = '';
  errorMessage = '';
  
  ngOnInit(): void {
    // Get the tenant ID and invite ID from the route
    this.route.paramMap.subscribe(params => {
      const tenantId = params.get('tenantId');
      const inviteId = params.get('inviteId');
      
      if (!tenantId || !inviteId) {
        this.showError('Invalid invitation link');
        return;
      }

      // Check if the user is already logged in
      if (this.authService.isLoggedIn()) {
        // If logged in, try to join the tenant
        this.joinTenant(tenantId, inviteId);
      } else {
        // If not logged in, store the invitation data and redirect to login
        this.invitationStorageService.storeInvitation(tenantId, inviteId);
        this.router.navigate(['/login']);
      }
    });
  }
  
  private joinTenant(tenantId: string, inviteId: string): void {
    this.isLoading = true;
    
    this.clientInvitationService.joinByInvitation(tenantId, inviteId).subscribe({
      next: () => {
        this.isLoading = false;
        this.successMessage = 'You have successfully joined the sport club!';
        this.invitationStorageService.clearStoredInvitation();
      },
      error: (error) => {
        this.isLoading = false;
        
        if (error.status === 404) {
          this.errorMessage = 'The invitation was not found or has expired.';
        } else if (error.status === 400) {
          this.errorMessage = 'You are already a member of this sport club.';
        } else {
          this.errorMessage = 'An error occurred while processing your invitation. Please try again later.';
        }
      }
    });
  }
  
  private showError(message: string): void {
    this.isLoading = false;
    this.errorMessage = message;
  }
} 