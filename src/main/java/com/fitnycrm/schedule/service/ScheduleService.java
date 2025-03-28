package com.fitnycrm.schedule.service;

import com.fitnycrm.location.service.LocationService;
import com.fitnycrm.schedule.repository.ScheduleRepository;
import com.fitnycrm.schedule.repository.entity.Schedule;
import com.fitnycrm.schedule.rest.model.CreateScheduleRequest;
import com.fitnycrm.schedule.service.mapper.ScheduleRequestMapper;
import com.fitnycrm.training.service.TrainingService;
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
        var instructor = trainerService.findById(tenantId, request.defaultInstructorId());

        var schedule = requestMapper.toSchedule(request);
        schedule.setTraining(training);
        schedule.setLocation(location);
        schedule.setDefaultInstructor(instructor);

        return scheduleRepository.save(schedule);
    }
} 