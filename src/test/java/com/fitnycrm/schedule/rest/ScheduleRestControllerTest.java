package com.fitnycrm.schedule.rest;

import com.fitnycrm.common.annotation.IntegrationTest;
import com.fitnycrm.user.repository.entity.UserRole;
import com.fitnycrm.user.util.JwtTokenCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.fitnycrm.common.util.TestUtils.readFile;
import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@Sql({"/db/tenant/insert.sql", "/db/user/insert.sql", "/db/training/insert.sql", "/db/location/insert.sql", "/db/trainer/insert.sql"})
class ScheduleRestControllerTest {

    private static final String BASE_URL = "/api/tenants/7a7632b1-e932-48fd-9296-001036b4ec19/trainings/ae4d661a-ed70-4e36-9caf-048ee8060290/schedules";
    private static final UUID DIFFERENT_TENANT_ID = UUID.fromString("b35ac7f5-3e4f-462a-a76d-524bd3a5fd03");
    private static final UUID TRAINING_ID = UUID.fromString("8a7632b1-e932-48fd-9296-001036b4ec19");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenCreator jwtTokenCreator;

    @Test
    void createSchedule_whenValidRequest_thenCreateSchedule() throws Exception {
        var request = readFile("fixture/schedule/create/request/valid-request.json");
        var expectedResponse = readFile("fixture/schedule/create/response/success.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isCreated())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void createSchedule_whenInvalidDaysOfWeek_thenReturnBadRequest() throws Exception {
        var request = readFile("fixture/schedule/create/request/invalid-days-of-week.json");
        var expectedResponse = readFile("fixture/schedule/create/response/invalid-days-of-week.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void createSchedule_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        var request = readFile("fixture/schedule/create/request/valid-request.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createSchedule_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var request = readFile("fixture/schedule/create/request/valid-request.json");
        var expectedResponse = readFile("fixture/schedule/create/response/access-denied.json");

        mockMvc.perform(post("/api/tenants/{tenantId}/trainings/{trainingId}/schedules", DIFFERENT_TENANT_ID, TRAINING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "TRAINER"})
    void createSchedule_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        var request = readFile("fixture/schedule/create/request/valid-request.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }
} 