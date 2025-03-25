package com.fitnycrm.location.rest;

import com.fitnycrm.common.annotation.IntegrationTest;
import com.fitnycrm.common.util.TestUtils;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@Sql({"/db/tenant/insert.sql", "/db/user/insert.sql"})
class LocationRestControllerTest {

    private static final String BASE_URL = "/api/tenants/7a7632b1-e932-48fd-9296-001036b4ec19/locations";
    private static final UUID DIFFERENT_TENANT_ID = UUID.fromString("99999999-9999-9999-9999-999999999999");
    private static final UUID EXISTING_LOCATION_ID = UUID.fromString("c35ac7f5-3e4f-462a-a76d-524bd3a5fd01");
    private static final UUID NON_EXISTING_LOCATION_ID = UUID.fromString("ad475c18-777c-4baf-a0d4-14865a0c476a");

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

    @Test
    @Sql("/db/location/insert.sql")
    void updateLocation_whenValidRequest_thenUpdateLocation() throws Exception {
        var request = readFile("fixture/location/update/request/valid-request.json");
        var expectedResponse = readFile("fixture/location/update/response/success.json");

        mockMvc.perform(put(BASE_URL + "/{locationId}", EXISTING_LOCATION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void updateLocation_whenInvalidRequest_thenReturnBadRequest() throws Exception {
        var request = readFile("fixture/location/update/request/invalid-request.json");
        var expectedResponse = readFile("fixture/location/update/response/invalid-request.json");

        mockMvc.perform(put(BASE_URL + "/{locationId}", EXISTING_LOCATION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void updateLocation_whenLocationNotFound_thenReturn404() throws Exception {
        var request = readFile("fixture/location/update/request/valid-request.json");
        var expectedResponse = readFile("fixture/location/update/response/not-found.json");

        mockMvc.perform(put(BASE_URL + "/{locationId}", NON_EXISTING_LOCATION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNotFound())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void updateLocation_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        var request = readFile("fixture/location/update/request/valid-request.json");

        mockMvc.perform(put(BASE_URL + "/{locationId}", EXISTING_LOCATION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateLocation_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var request = readFile("fixture/location/update/request/valid-request.json");
        var expectedResponse = readFile("fixture/location/update/response/access-denied.json");

        mockMvc.perform(put("/api/tenants/{tenantId}/locations/{locationId}", DIFFERENT_TENANT_ID, EXISTING_LOCATION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "COACH"})
    void updateLocation_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        var request = readFile("fixture/location/update/request/valid-request.json");

        mockMvc.perform(put(BASE_URL + "/{locationId}", EXISTING_LOCATION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql("/db/location/insert.sql")
    void deleteLocation_whenLocationExists_thenDeleteLocation() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{locationId}", EXISTING_LOCATION_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteLocation_whenLocationNotFound_thenReturn404() throws Exception {
        var expectedResponse = readFile("fixture/location/delete/response/not-found.json");

        mockMvc.perform(delete(BASE_URL + "/{locationId}", NON_EXISTING_LOCATION_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNotFound())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void deleteLocation_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{locationId}", EXISTING_LOCATION_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteLocation_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var expectedResponse = readFile("fixture/location/delete/response/access-denied.json");

        mockMvc.perform(delete("/api/tenants/{tenantId}/locations/{locationId}", DIFFERENT_TENANT_ID, EXISTING_LOCATION_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "COACH"})
    void deleteLocation_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{locationId}", EXISTING_LOCATION_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql("/db/location/insert.sql")
    void getAll_shouldReturnLocations() throws Exception {
        var expectedResponse = TestUtils.readFile("fixture/location/getAll/response.json");

        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void getAll_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAll_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var expectedResponse = TestUtils.readFile("fixture/location/getAll/error-403.json");

        mockMvc.perform(get("/api/tenants/{tenantId}/locations", DIFFERENT_TENANT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "COACH"})
    void getAll_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql("/db/location/insert.sql")
    void findById_whenLocationExists_thenReturnLocation() throws Exception {
        var expectedResponse = readFile("fixture/location/findById/response/success.json");

        mockMvc.perform(get(BASE_URL + "/{locationId}", EXISTING_LOCATION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void findById_whenLocationNotFound_thenReturn404() throws Exception {
        var expectedResponse = readFile("fixture/location/findById/response/not-found.json");

        mockMvc.perform(get(BASE_URL + "/{locationId}", NON_EXISTING_LOCATION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNotFound())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void findById_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{locationId}", EXISTING_LOCATION_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findById_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var expectedResponse = readFile("fixture/location/findById/response/access-denied.json");

        mockMvc.perform(get("/api/tenants/{tenantId}/locations/{locationId}", DIFFERENT_TENANT_ID, EXISTING_LOCATION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "COACH"})
    void findById_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        mockMvc.perform(get(BASE_URL + "/{locationId}", EXISTING_LOCATION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }
} 