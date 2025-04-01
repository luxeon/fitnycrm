package com.fitnycrm.tenant.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnycrm.common.annotation.IntegrationTest;
import com.fitnycrm.tenant.repository.entity.Tenant;
import com.fitnycrm.tenant.rest.model.TenantDetailsResponse;
import com.fitnycrm.user.repository.UserRepository;
import com.fitnycrm.user.repository.entity.User;
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
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.fitnycrm.common.util.TestUtils.readFile;
import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@Sql(executionPhase =
        Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"/db/tenant/insert.sql", "/db/user/insert.sql"})
class TenantRestControllerTest {

    private static final String BASE_URL = "/api/tenants";
    private static final UUID TENANT_ID = UUID.fromString("7a7632b1-e932-48fd-9296-001036b4ec19");
    private static final UUID USER_WITHOUT_TENANT_ID = UUID.fromString("a35ac7f5-3e4f-462a-a76d-524bd3a5fd02");
    private static final UUID DIFFERENT_TENANT_ID = UUID.fromString("b35ac7f5-3e4f-462a-a76d-524bd3a5fd03");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenCreator jwtTokenCreator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    void getOne_whenTenantExists_thenReturnTenant() throws Exception {
        var expectedResponse = readFile("fixture/tenant/getOne/response/tenant-1.json");

        mockMvc.perform(get(BASE_URL + "/{id}", TENANT_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void getOne_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", TENANT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getOne_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", DIFFERENT_TENANT_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "TRAINER"})
    void getOne_whenUserHasInsufficientRole_thenReturn403(UserRole.Name role) throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", TENANT_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void create_whenValidRequest_thenCreateTenant() throws Exception {
        var request = readFile("fixture/tenant/create/request/valid-tenant.json");
        var expectedResponse = readFile("fixture/tenant/create/response/tenant-created.json");

        String response = mockMvc.perform(post(BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminWithoutTenantTestJwtToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(json().isEqualTo(expectedResponse))
                .andReturn().getResponse().getContentAsString();

        TenantDetailsResponse tenantDetails = objectMapper.readValue(response, TenantDetailsResponse.class);

        transactionTemplate.execute(status -> {
            User user = userRepository.findById(USER_WITHOUT_TENANT_ID).orElseThrow();
            Set<UUID> tenantIds = user.getTenants().stream().map(Tenant::getId).collect(Collectors.toSet());
            assertThat(tenantIds).contains(tenantDetails.id());
            return user;
        });
    }

    @Test
    void create_whenInvalidRequest_thenReturn400() throws Exception {
        var request = readFile("fixture/tenant/create/request/invalid-tenant.json");
        var expectedResponse = readFile("fixture/tenant/create/response/validation-error.json");

        mockMvc.perform(post(BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void create_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        var request = readFile("fixture/tenant/create/request/valid-tenant.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void create_whenNameIsNull_thenReturn400() throws Exception {
        var request = readFile("fixture/tenant/create/request/null-name-tenant.json");
        var expectedResponse = readFile("fixture/tenant/create/response/null-name-error.json");

        mockMvc.perform(post(BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void create_whenUserHasAssignedTenant_thenReturn422() throws Exception {
        var request = readFile("fixture/tenant/create/request/valid-tenant.json");
        var expectedResponse = readFile("fixture/tenant/create/response/tenant-already-created-error.json");

        mockMvc.perform(post(BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void update_whenValidRequest_thenUpdateTenant() throws Exception {
        var request = readFile("fixture/tenant/update/request/valid-tenant.json");
        var expectedResponse = readFile("fixture/tenant/update/response/tenant-updated.json");

        mockMvc.perform(put(BASE_URL + "/{id}", TENANT_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void update_whenInvalidRequest_thenReturn400() throws Exception {
        var request = readFile("fixture/tenant/update/request/invalid-tenant.json");
        var expectedResponse = readFile("fixture/tenant/update/response/validation-error.json");

        mockMvc.perform(put(BASE_URL + "/{id}", TENANT_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void update_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        var request = readFile("fixture/tenant/update/request/valid-tenant.json");

        mockMvc.perform(put(BASE_URL + "/{id}", TENANT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void update_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var request = readFile("fixture/tenant/update/request/valid-tenant.json");

        mockMvc.perform(put(BASE_URL + "/{id}", DIFFERENT_TENANT_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "TRAINER"})
    void create_whenUserHasInsufficientRole_thenReturn403(UserRole.Name role) throws Exception {
        var request = readFile("fixture/tenant/create/request/valid-tenant.json");

        mockMvc.perform(post(BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "TRAINER"})
    void update_whenUserHasInsufficientRole_thenReturn403(UserRole.Name role) throws Exception {
        var request = readFile("fixture/tenant/update/request/valid-tenant.json");

        mockMvc.perform(put(BASE_URL + "/{id}", TENANT_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllForAuthenticatedUser_whenUserHasTenants_thenReturnTenants() throws Exception {
        var expectedResponse = readFile("fixture/tenant/getAllForAuthenticatedUser/response/tenants.json");

        mockMvc.perform(get(BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void getAllForAuthenticatedUser_whenUserHasNoTenants_thenReturnEmptyList() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminWithoutTenantTestJwtToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo("[]"));
    }

    @Test
    void getAllForAuthenticatedUser_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
} 