package com.fitnycrm.schedule.service;

import com.fitnycrm.location.service.LocationService;
import com.fitnycrm.schedule.repository.ScheduleRepository;
import com.fitnycrm.schedule.repository.entity.Schedule;
import com.fitnycrm.schedule.rest.model.CreateScheduleRequest;
import com.fitnycrm.schedule.rest.model.UpdateScheduleRequest;
import com.fitnycrm.schedule.service.exception.ScheduleNotFoundException;
import com.fitnycrm.schedule.service.exception.ScheduleTenantMismatchException;
import com.fitnycrm.schedule.service.exception.ScheduleTrainingMismatchException;
import com.fitnycrm.schedule.service.mapper.ScheduleRequestMapper;
import com.fitnycrm.training.service.TrainingService;
import com.fitnycrm.user.repository.entity.User;
import com.fitnycrm.user.service.trainer.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TrainingService trainingService;
    private final LocationService locationService;
    private final TrainerService trainerService;
    private final ScheduleRequestMapper requestMapper;

    @Transactional
    public Schedule create(UUID tenantId, UUID trainingId, CreateScheduleRequest request) {
        var training = trainingService.findById(tenantId, trainingId);
        var location = locationService.findById(tenantId, request.locationId());
        var instructor = trainerService.findById(tenantId, request.defaultTrainerId());

        var schedule = requestMapper.toSchedule(request);
        schedule.setTraining(training);
        schedule.setLocation(location);
        schedule.setDefaultTrainer(instructor);

        return scheduleRepository.save(schedule);
    }

    @Transactional(readOnly = true)
    public Schedule findById(UUID tenantId, UUID trainingId, UUID scheduleId) {
        var schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException("Schedule not found"));

        if (!schedule.getTraining().getId().equals(trainingId)) {
            throw new ScheduleTrainingMismatchException("Schedule does not belong to the specified training");
        }

        if (!schedule.getTraining().getTenant().getId().equals(tenantId)) {
            throw new ScheduleTenantMismatchException("Schedule does not belong to the specified tenant");
        }

        return schedule;
    }

    @Transactional
    public Schedule update(UUID tenantId, UUID trainingId, UUID scheduleId, UpdateScheduleRequest request) {
        var schedule = findById(tenantId, trainingId, scheduleId);
        requestMapper.update(schedule, request);
        if (!schedule.getTraining().getId().equals(request.defaultTrainerId())) {
            User trainer = trainerService.findById(tenantId, request.defaultTrainerId());
            schedule.setDefaultTrainer(trainer);
        }
        return scheduleRepository.save(schedule);
    }

    @Transactional
    public void delete(UUID tenantId, UUID trainingId, UUID scheduleId) {
        var schedule = findById(tenantId, trainingId, scheduleId);
        scheduleRepository.delete(schedule);
    }
} 