package com.fitavera.schedule.service;

import com.fitavera.location.repository.entity.Location;
import com.fitavera.location.service.LocationService;
import com.fitavera.schedule.repository.ScheduleRepository;
import com.fitavera.schedule.repository.entity.Schedule;
import com.fitavera.schedule.rest.model.CreateScheduleRequest;
import com.fitavera.schedule.rest.model.UpdateScheduleRequest;
import com.fitavera.schedule.service.exception.ScheduleNotFoundException;
import com.fitavera.schedule.service.exception.ScheduleTenantMismatchException;
import com.fitavera.schedule.service.exception.ScheduleTrainingMismatchException;
import com.fitavera.schedule.service.mapper.ScheduleRequestMapper;
import com.fitavera.training.service.TrainingService;
import com.fitavera.user.repository.entity.User;
import com.fitavera.user.service.trainer.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public Schedule create(UUID tenantId, UUID locationId, CreateScheduleRequest request) {
        var location = locationService.findById(tenantId, locationId);
        var training = trainingService.findById(tenantId, request.trainingId());
        var instructor = trainerService.findById(tenantId, request.defaultTrainerId());

        var schedule = requestMapper.toSchedule(request);
        schedule.setTraining(training);
        schedule.setLocation(location);
        schedule.setDefaultTrainer(instructor);

        return scheduleRepository.save(schedule);
    }

    @Transactional(readOnly = true)
    public Schedule findById(UUID tenantId, UUID locationId, UUID scheduleId) {
        var schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException("Schedule not found"));

        if (!schedule.getLocation().getId().equals(locationId)) {
            throw new ScheduleTrainingMismatchException("Schedule does not belong to the specified training");
        }

        if (!schedule.getTraining().getTenant().getId().equals(tenantId)) {
            throw new ScheduleTenantMismatchException("Schedule does not belong to the specified tenant");
        }

        return schedule;
    }

    @Transactional
    public Schedule update(UUID tenantId, UUID locationId, UUID scheduleId, UpdateScheduleRequest request) {
        var schedule = findById(tenantId, locationId, scheduleId);
        requestMapper.update(schedule, request);
        if (!schedule.getDefaultTrainer().getId().equals(request.defaultTrainerId())) {
            User trainer = trainerService.findById(tenantId, request.defaultTrainerId());
            schedule.setDefaultTrainer(trainer);
        }
        return scheduleRepository.save(schedule);
    }

    @Transactional
    public void delete(UUID tenantId, UUID locationId, UUID scheduleId) {
        var schedule = findById(tenantId, locationId, scheduleId);
        scheduleRepository.delete(schedule);
    }

    @Transactional(readOnly = true)
    public List<Schedule> findByLocation(UUID tenantId, UUID locationId) {
        Location location = locationService.findById(tenantId, locationId);
        return scheduleRepository.findAllByLocation(location);
    }
} 