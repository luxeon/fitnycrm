import { Injectable } from '@angular/core';

interface InvitationData {
  tenantId: string;
  inviteId: string;
}

@Injectable({
  providedIn: 'root'
})
export class InvitationStorageService {
  private readonly STORAGE_KEY = 'pending_invitation';

  /**
   * Store the invitation parameters for later use
   */
  storeInvitation(tenantId: string, inviteId: string): void {
    const invitationData: InvitationData = { tenantId, inviteId };
    localStorage.setItem(this.STORAGE_KEY, JSON.stringify(invitationData));
  }

  /**
   * Get the stored invitation parameters if they exist
   */
  getStoredInvitation(): InvitationData | null {
    const storedData = localStorage.getItem(this.STORAGE_KEY);
    return storedData ? JSON.parse(storedData) : null;
  }

  /**
   * Clear the stored invitation
   */
  clearStoredInvitation(): void {
    localStorage.removeItem(this.STORAGE_KEY);
  }

  /**
   * Check if there's a pending invitation
   */
  hasPendingInvitation(): boolean {
    return !!localStorage.getItem(this.STORAGE_KEY);
  }
} 