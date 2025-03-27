package com.fitnycrm.user.rest.trainer;

import com.fitnycrm.common.annotation.IntegrationTest;
import com.fitnycrm.user.util.JwtTokenCreator;
import com.fitnycrm.user.repository.entity.UserRole;
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
class TrainerRestControllerTest {

    private static final String BASE_URL = "/api/tenants/7a7632b1-e932-48fd-9296-001036b4ec19/trainers";
    private static final UUID DIFFERENT_TENANT_ID = UUID.fromString("b35ac7f5-3e4f-462a-a76d-524bd3a5fd03");
    private static final UUID EXISTING_TRAINER_ID = UUID.fromString("c35ac7f5-3e4f-462a-a76d-524bd3a5fd03");
    private static final UUID NON_EXISTING_TRAINER_ID = UUID.fromString("c35ac7f5-3e4f-462a-a76d-524bd3a5fd99");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenCreator jwtTokenCreator;

    @Test
    void createTrainer_whenValidRequest_thenCreateTrainer() throws Exception {
        var request = readFile("fixture/trainer/create/request/valid-request.json");
        var expectedResponse = readFile("fixture/trainer/create/response/success.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isCreated())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void createTrainer_whenInvalidEmail_thenReturnBadRequest() throws Exception {
        var request = readFile("fixture/trainer/create/request/invalid-email.json");
        var expectedResponse = readFile("fixture/trainer/create/response/invalid-email.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    @Sql("/db/trainer/insert.sql")
    void createTrainer_whenEmailAlreadyExists_thenReturnConflict() throws Exception {
        var request = readFile("fixture/trainer/create/request/valid-request.json");
        var expectedResponse = readFile("fixture/trainer/create/response/email-exists.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isConflict())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void createTrainer_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        var request = readFile("fixture/trainer/create/request/valid-request.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createTrainer_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var request = readFile("fixture/trainer/create/request/valid-request.json");
        var expectedResponse = readFile("fixture/trainer/create/response/access-denied.json");

        mockMvc.perform(post("/api/tenants/{tenantId}/trainers", DIFFERENT_TENANT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "TRAINER"})
    void createTrainer_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        var request = readFile("fixture/trainer/create/request/valid-request.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql("/db/trainer/insert.sql")
    void updateTrainer_whenValidRequest_thenUpdateTrainer() throws Exception {
        var request = readFile("fixture/trainer/update/request/valid-request.json");
        var expectedResponse = readFile("fixture/trainer/update/response/success.json");

        mockMvc.perform(put(BASE_URL + "/{trainerId}", EXISTING_TRAINER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void updateTrainer_whenInvalidEmail_thenReturnBadRequest() throws Exception {
        var request = readFile("fixture/trainer/update/request/invalid-email.json");
        var expectedResponse = readFile("fixture/trainer/update/response/invalid-email.json");

        mockMvc.perform(put(BASE_URL + "/{trainerId}", EXISTING_TRAINER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void updateTrainer_whenTrainerNotFound_thenReturn404() throws Exception {
        var request = readFile("fixture/trainer/update/request/valid-request.json");
        var expectedResponse = readFile("fixture/trainer/update/response/not-found.json");

        mockMvc.perform(put(BASE_URL + "/{trainerId}", NON_EXISTING_TRAINER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNotFound())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    @Sql("/db/trainer/insert.sql")
    void updateTrainer_whenEmailAlreadyExists_thenReturnConflict() throws Exception {
        var request = readFile("fixture/trainer/update/request/valid-request.json");
        var expectedResponse = readFile("fixture/trainer/update/response/email-exists.json");

        mockMvc.perform(put(BASE_URL + "/{trainerId}", EXISTING_TRAINER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request.replace("james.bond.updated@example.com", "jane.smith@gmail.com"))
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isConflict())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void updateTrainer_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        var request = readFile("fixture/trainer/update/request/valid-request.json");

        mockMvc.perform(put(BASE_URL + "/{trainerId}", EXISTING_TRAINER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateTrainer_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var request = readFile("fixture/trainer/update/request/valid-request.json");
        var expectedResponse = readFile("fixture/trainer/create/response/access-denied.json");

        mockMvc.perform(put("/api/tenants/{tenantId}/trainers/{trainerId}", DIFFERENT_TENANT_ID, EXISTING_TRAINER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "TRAINER"})
    void updateTrainer_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        var request = readFile("fixture/trainer/update/request/valid-request.json");

        mockMvc.perform(put(BASE_URL + "/{trainerId}", EXISTING_TRAINER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql("/db/trainer/insert.sql")
    void deleteTrainer_whenTrainerExists_thenDeleteTrainer() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{trainerId}", EXISTING_TRAINER_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTrainer_whenTrainerNotFound_thenReturn404() throws Exception {
        var expectedResponse = readFile("fixture/trainer/update/response/not-found.json");

        mockMvc.perform(delete(BASE_URL + "/{trainerId}", NON_EXISTING_TRAINER_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNotFound())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void deleteTrainer_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{trainerId}", EXISTING_TRAINER_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteTrainer_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var expectedResponse = readFile("fixture/trainer/create/response/access-denied.json");

        mockMvc.perform(delete("/api/tenants/{tenantId}/trainers/{trainerId}", DIFFERENT_TENANT_ID, EXISTING_TRAINER_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "TRAINER"})
    void deleteTrainer_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{trainerId}", EXISTING_TRAINER_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql("/db/trainer/insert.sql")
    void findById_whenTrainerExists_thenReturnTrainer() throws Exception {
        var expectedResponse = readFile("fixture/trainer/get/response/success.json");

        mockMvc.perform(get(BASE_URL + "/{trainerId}", EXISTING_TRAINER_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void findById_whenTrainerNotFound_thenReturn404() throws Exception {
        var expectedResponse = readFile("fixture/trainer/get/response/not-found.json");

        mockMvc.perform(get(BASE_URL + "/{trainerId}", NON_EXISTING_TRAINER_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNotFound())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void findById_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{trainerId}", EXISTING_TRAINER_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findById_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var expectedResponse = readFile("fixture/trainer/get/response/access-denied.json");

        mockMvc.perform(get("/api/tenants/{tenantId}/trainers/{trainerId}", DIFFERENT_TENANT_ID, EXISTING_TRAINER_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    @Sql("/db/trainer/insert.sql")
    void findByTenantId_whenTrainersExist_thenReturnPaginatedTrainers() throws Exception {
        var expectedResponse = readFile("fixture/trainer/findByTenantId/response/success.json");

        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "email,desc")
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void findByTenantId_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findByTenantId_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var expectedResponse = readFile("fixture/trainer/findByTenantId/response/access-denied.json");

        mockMvc.perform(get("/api/tenants/{tenantId}/trainers", DIFFERENT_TENANT_ID)
                        .param("page", "0")
                        .param("size", "10")
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "TRAINER"})
    void findByTenantId_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "10")
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }
} 