package com.fitnycrm.visit.service;

import com.fitnycrm.payment.service.ClientPaymentService;
import com.fitnycrm.schedule.repository.entity.Schedule;
import com.fitnycrm.schedule.service.ScheduleService;
import com.fitnycrm.user.repository.entity.User;
import com.fitnycrm.user.service.client.ClientService;
import com.fitnycrm.visit.repository.VisitRepository;
import com.fitnycrm.visit.repository.entity.Visit;
import com.fitnycrm.visit.service.exception.VisitRegistrationException;
import com.fitnycrm.visit.service.mapper.VisitRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final ClientService clientService;
    private final ScheduleService scheduleService;
    private final VisitRepository visitRepository;
    private final ClientPaymentService clientPaymentService;
    private final VisitRequestMapper mapper;

    @Transactional
    public Visit register(UUID tenantId, UUID locationId, UUID scheduleId, LocalDate date, UUID clientId) {
        User client = clientService.findById(tenantId, clientId);
        Schedule schedule = scheduleService.findById(tenantId, locationId, scheduleId);

        long count = visitRepository.countByScheduleAndDate(schedule, date);
        if (count >= schedule.getClientCapacity()) {
            throw new VisitRegistrationException("Client capacity exceeded");
        }

        clientPaymentService.countVisit(tenantId, clientId, schedule.getTraining().getId());

        Visit visit = mapper.toEntity(date);
        visit.setSchedule(schedule);
        visit.setClient(client);

        visit = visitRepository.save(visit);

        return visit;
    }
}
