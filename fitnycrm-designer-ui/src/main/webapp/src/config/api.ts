export const API_CONFIG = {
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080',
} as const;

// Helper function to build API URLs
export const buildApiUrl = (path: string): string => {
  return `${API_CONFIG.baseURL}${path}`;
}; 