package com.fitnycrm.location.rest;

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
@Sql({"/db/tenant/insert.sql", "/db/user/insert.sql"})
class LocationRestControllerIntegrationTest {

    private static final String BASE_URL = "/api/tenants/7a7632b1-e932-48fd-9296-001036b4ec19/locations";
    private static final UUID DIFFERENT_TENANT_ID = UUID.fromString("99999999-9999-9999-9999-999999999999");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenCreator jwtTokenCreator;

    @Test
    void createLocation_whenValidRequest_thenCreateLocation() throws Exception {
        var request = readFile("fixture/location/create/request/valid-request.json");
        var expectedResponse = readFile("fixture/location/create/response/success.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isCreated())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void createLocation_whenInvalidRequest_thenReturnBadRequest() throws Exception {
        var request = readFile("fixture/location/create/request/invalid_request.json");
        var expectedResponse = readFile("fixture/location/create/response/invalid.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void createLocation_whenTenantNotFound_thenReturn404() throws Exception {
        var request = readFile("fixture/location/create/request/valid-request.json");

        mockMvc.perform(post("/api/tenants/{tenantId}/locations", DIFFERENT_TENANT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    void createLocation_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        var request = readFile("fixture/location/create/request/valid-request.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createLocation_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var request = readFile("fixture/location/create/request/valid-request.json");
        var expectedResponse = readFile("fixture/location/create/response/access-denied.json");

        mockMvc.perform(post("/api/tenants/{tenantId}/locations", DIFFERENT_TENANT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "COACH"})
    void createLocation_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        var request = readFile("fixture/location/create/request/valid-request.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }
} 