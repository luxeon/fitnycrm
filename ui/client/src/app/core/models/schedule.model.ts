export interface ScheduleResponse {
  id: string;
  trainingId: string;
  daysOfWeek: string[];
  startTime: string;
  endTime: string;
  defaultTrainerId: string;
  clientCapacity: number;
}

export interface SchedulePageItemResponse {
  id: string;
  trainingId: string;
  trainingName: string;
  defaultTrainerId: string;
  defaultTrainerFullName: string;
  daysOfWeek: string[];
  startTime: string;
  endTime: string;
  clientCapacity: number;
}

export interface VisitRequest {
  scheduleId: string;
  date: string;
}

export interface VisitResponse {
  id: string;
  scheduleId: string;
  date: string;
  status: 'CONFIRMED' | 'CANCELLED';
  createdAt: string;
  updatedAt: string;
}

export interface UserVisit extends VisitResponse {
  scheduleName: string;
  startTime: string;
  endTime: string;
  locationName: string;
  trainerName?: string;
} 