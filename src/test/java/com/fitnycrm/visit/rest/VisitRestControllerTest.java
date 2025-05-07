package com.fitnycrm.visit.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnycrm.common.annotation.IntegrationTest;
import com.fitnycrm.user.repository.entity.UserRole;
import com.fitnycrm.user.util.JwtTokenCreator;
import com.fitnycrm.visit.rest.model.VisitDetailsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.fitnycrm.common.util.TestUtils.readFile;
import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@Sql({"/db/tenant/insert.sql", "/db/user/insert.sql", "/db/training/insert.sql", "/db/location/insert.sql", "/db/trainer/insert.sql", "/db/schedule/insert.sql", "/db/client-training-credits.sql"})
class VisitRestControllerTest {

    private static final String CANCEL_URL = "/api/tenants/7a7632b1-e932-48fd-9296-001036b4ec19/locations/c35ac7f5-3e4f-462a-a76d-524bd3a5fd01/schedules/9a7632b1-e932-48fd-9296-001036b4ec19/visits/{visitId}";
    private static final String REGISTER_URL = "/api/tenants/7a7632b1-e932-48fd-9296-001036b4ec19/locations/c35ac7f5-3e4f-462a-a76d-524bd3a5fd01/schedules/9a7632b1-e932-48fd-9296-001036b4ec19/visits";

    private static final UUID LOCATION_ID = UUID.fromString("c35ac7f5-3e4f-462a-a76d-524bd3a5fd01");
    private static final UUID SCHEDULE_ID = UUID.fromString("9a7632b1-e932-48fd-9296-001036b4ec19");
    private static final UUID DIFFERENT_TENANT_ID = UUID.fromString("b35ac7f5-3e4f-462a-a76d-524bd3a5fd03");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenCreator jwtTokenCreator;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_whenValidRequest_thenCreateVisit() throws Exception {
        var request = readFile("fixture/visit/register/request/valid-request.json");
        var expectedResponse = readFile("fixture/visit/register/response/success.json");

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(UserRole.Name.CLIENT)))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    @Sql("/db/visit/insert.sql")
    void register_whenMaxCapacityExceeded_thenReturnBadRequest() throws Exception {
        var validRequest = readFile("fixture/visit/register/request/valid-request.json");
        var expectedResponse = readFile("fixture/visit/register/response/max-capacity-exceeded.json");

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(UserRole.Name.CLIENT)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void register_whenInvalidDate_thenReturnBadRequest() throws Exception {
        var request = readFile("fixture/visit/register/request/invalid-date.json");
        var expectedResponse = readFile("fixture/visit/register/response/invalid-date.json");

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(UserRole.Name.CLIENT)))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void register_whenDateDoesNotMatchSchedule_thenReturnBadRequest() throws Exception {
        var request = readFile("fixture/visit/register/request/date-not-match-schedule.json");
        var expectedResponse = readFile("fixture/visit/register/response/date-not-match-schedule.json");

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(UserRole.Name.CLIENT)))
                .andExpect(status().is4xxClientError())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void register_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        var request = readFile("fixture/visit/register/request/valid-request.json");

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void register_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var request = readFile("fixture/visit/register/request/valid-request.json");
        var expectedResponse = readFile("fixture/visit/register/response/access-denied.json");

        mockMvc.perform(post("/api/tenants/{tenantId}/locations/{locationId}/schedules/{scheduleId}/visits",
                        DIFFERENT_TENANT_ID, LOCATION_ID, SCHEDULE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(UserRole.Name.CLIENT)))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void register_whenUserHasUnauthorizedRole_thenReturn403() throws Exception {
        var request = readFile("fixture/visit/register/request/valid-request.json");

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    void register_whenClientAlreadyRegistered_thenReturnBadRequest() throws Exception {
        // First registration should succeed
        var validRequest = readFile("fixture/visit/register/request/valid-request.json");
        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(UserRole.Name.CLIENT)))
                .andExpect(status().isOk());

        // Second registration for the same training and date should fail
        var expectedResponse = readFile("fixture/visit/register/response/already-registered.json");
        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(UserRole.Name.CLIENT)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    @Sql("/db/visit/insert.sql")
    void cancel_whenValidRequest_thenCancelVisit() throws Exception {
        var request = readFile("fixture/visit/register/request/valid-request-2.json");

        String token = jwtTokenCreator.generateTestJwtToken("user1@test.com", UserRole.Name.CLIENT);
        String response = mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UUID visitId = objectMapper.readValue(response, VisitDetailsResponse.class).id();

        mockMvc.perform(delete(CANCEL_URL, visitId)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
    }

    @Test
    @Sql("/db/visit/insert.sql")
    void cancel_whenVisitInPast_thenReturnError() throws Exception {
        var expectedResponse = readFile("fixture/visit/cancel/response/past-visit.json");

        mockMvc.perform(delete(CANCEL_URL, "a1a1a1a1-e932-48fd-9296-001036b4ec19")
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken("user1@test.com", UserRole.Name.CLIENT)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    @Sql("/db/visit/insert.sql")
    void cancel_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        mockMvc.perform(delete(CANCEL_URL, "c3c3c3c3-e932-48fd-9296-001036b4ec19"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql("/db/visit/insert.sql")
    void cancel_whenUserHasUnauthorizedRole_thenReturn403() throws Exception {
        mockMvc.perform(delete(CANCEL_URL, "c3c3c3c3-e932-48fd-9296-001036b4ec19")
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden());
    }
}
