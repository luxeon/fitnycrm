import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-client-signup',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './client-signup.component.html',
  styleUrls: ['./client-signup.component.scss']
})
export class ClientSignupComponent {
  private route = inject(ActivatedRoute);
  
  tenantId: string | null = null;
  inviteId: string | null = null;

  constructor() {
    this.route.paramMap.subscribe(params => {
      this.tenantId = params.get('tenantId');
      this.inviteId = params.get('inviteId');
    });
  }
} 