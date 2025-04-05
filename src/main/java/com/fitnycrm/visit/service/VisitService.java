package com.fitnycrm.visit.service;

import com.fitnycrm.payment.service.ClientPaymentService;
import com.fitnycrm.schedule.repository.entity.Schedule;
import com.fitnycrm.schedule.service.ScheduleService;
import com.fitnycrm.user.repository.entity.User;
import com.fitnycrm.user.service.client.ClientService;
import com.fitnycrm.visit.repository.VisitRepository;
import com.fitnycrm.visit.repository.entity.Visit;
import com.fitnycrm.visit.rest.model.CreateVisitRequest;
import com.fitnycrm.visit.service.exception.VisitCancellationException;
import com.fitnycrm.visit.service.exception.VisitNotFoundException;
import com.fitnycrm.visit.service.exception.VisitRegistrationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VisitService {

    // TODO make it configurable
    private static final int CANCEL_UNTIL_IN_HOURS = 4;

    private final ClientService clientService;
    private final ScheduleService scheduleService;
    private final VisitRepository visitRepository;
    private final ClientPaymentService clientPaymentService;

    @Transactional
    public Visit register(UUID tenantId, UUID locationId, UUID scheduleId, CreateVisitRequest request, UUID clientId) {
        User client = clientService.findById(tenantId, clientId);
        Schedule schedule = scheduleService.findById(tenantId, locationId, scheduleId);

        if (!schedule.getDaysOfWeek().contains(request.date().getDayOfWeek())) {
            throw new VisitRegistrationException("Date of week doesn't match to schedule");
        }

        long count = visitRepository.countByScheduleAndDate(schedule, request.date());
        if (count >= schedule.getClientCapacity()) {
            throw new VisitRegistrationException("Client capacity exceeded");
        }

        clientPaymentService.countVisit(tenantId, clientId, schedule.getTraining().getId());

        Visit visit = new Visit();
        visit.setDate(request.date());
        visit.setSchedule(schedule);
        visit.setClient(client);

        visit = visitRepository.save(visit);

        return visit;
    }

    @Transactional
    public void cancel(UUID scheduleId, UUID visitId, UUID clientId) {
        Visit visit = findById(scheduleId, visitId, clientId);
        Schedule schedule = visit.getSchedule();
        LocalTime startTime = schedule.getStartTime();
        OffsetDateTime visitDateTime = OffsetDateTime.from(visit.getDate()).withHour(startTime.getHour()).withMinute(startTime.getMinute());
        OffsetDateTime now = OffsetDateTime.now();

        if (visitDateTime.isBefore(now)) {
            throw new VisitCancellationException("You can't cancel a visit in the past");
        }

        if (ChronoUnit.HOURS.between(visitDateTime, now) < CANCEL_UNTIL_IN_HOURS) {
            throw new VisitCancellationException("You can cancel before " + CANCEL_UNTIL_IN_HOURS + " hours till training.");
        }
        visitRepository.delete(visit);
    }

    @Transactional(readOnly = true)
    public Visit findById(UUID scheduleId, UUID visitId, UUID clientId) {
        Visit visit = visitRepository.findById(visitId).orElseThrow(() -> new VisitNotFoundException(visitId));
        if (!visit.getClient().getId().equals(clientId) || !visit.getSchedule().getId().equals(scheduleId)) {
            throw new VisitRegistrationException("Client id mismatch");
        }
        return visit;
    }
}
