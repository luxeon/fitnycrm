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