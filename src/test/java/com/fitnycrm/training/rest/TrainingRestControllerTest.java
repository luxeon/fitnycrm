package com.fitnycrm.training.rest;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@Sql({"/db/tenant/insert.sql", "/db/user/insert.sql"})
class TrainingRestControllerTest {

    private static final String BASE_URL = "/api/tenants/7a7632b1-e932-48fd-9296-001036b4ec19/trainings";
    private static final UUID DIFFERENT_TENANT_ID = UUID.fromString("b35ac7f5-3e4f-462a-a76d-524bd3a5fd03");
    private static final UUID EXISTING_TRAINING_ID = UUID.fromString("ae4d661a-ed70-4e36-9caf-048ee8060290");
    private static final UUID NON_EXISTING_TRAINING_ID = UUID.fromString("6218253d-d307-4ba1-ac77-b324aeeeb7ad");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenCreator jwtTokenCreator;

    @Test
    void createTraining_whenValidRequest_thenCreateTraining() throws Exception {
        var request = readFile("fixture/training/create/request/valid-request.json");
        var expectedResponse = readFile("fixture/training/create/response/success.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isCreated())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void createTraining_whenInvalidRequest_thenReturnBadRequest() throws Exception {
        var request = readFile("fixture/training/create/request/invalid-request.json");
        var expectedResponse = readFile("fixture/training/create/response/invalid.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void createTraining_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        var request = readFile("fixture/training/create/request/valid-request.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createTraining_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var request = readFile("fixture/training/create/request/valid-request.json");
        var expectedResponse = readFile("fixture/training/create/response/access-denied.json");

        mockMvc.perform(post("/api/tenants/{tenantId}/trainings", DIFFERENT_TENANT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "TRAINER"})
    void createTraining_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        var request = readFile("fixture/training/create/request/valid-request.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql("/db/training/insert.sql")
    void updateTraining_whenValidRequest_thenUpdateTraining() throws Exception {
        var request = readFile("fixture/training/update/request/valid-request.json");
        var expectedResponse = readFile("fixture/training/update/response/success.json");

        mockMvc.perform(put(BASE_URL + "/{trainingId}", EXISTING_TRAINING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void updateTraining_whenInvalidRequest_thenReturnBadRequest() throws Exception {
        var request = readFile("fixture/training/update/request/invalid-request.json");
        var expectedResponse = readFile("fixture/training/update/response/invalid.json");

        mockMvc.perform(put(BASE_URL + "/{trainingId}", EXISTING_TRAINING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void updateTraining_whenTrainingNotFound_thenReturn404() throws Exception {
        var request = readFile("fixture/training/update/request/valid-request.json");
        var expectedResponse = readFile("fixture/training/update/response/not-found.json");

        mockMvc.perform(put(BASE_URL + "/{trainingId}", NON_EXISTING_TRAINING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNotFound())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void updateTraining_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        var request = readFile("fixture/training/update/request/valid-request.json");

        mockMvc.perform(put(BASE_URL + "/{trainingId}", EXISTING_TRAINING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateTraining_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var request = readFile("fixture/training/update/request/valid-request.json");
        var expectedResponse = readFile("fixture/training/update/response/access-denied.json");

        mockMvc.perform(put("/api/tenants/{tenantId}/trainings/{trainingId}", DIFFERENT_TENANT_ID, EXISTING_TRAINING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "TRAINER"})
    void updateTraining_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        var request = readFile("fixture/training/update/request/valid-request.json");

        mockMvc.perform(put(BASE_URL + "/{trainingId}", EXISTING_TRAINING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql("/db/training/insert.sql")
    void deleteTraining_whenTrainingExists_thenDeleteTraining() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{trainingId}", EXISTING_TRAINING_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTraining_whenTrainingNotFound_thenReturn404() throws Exception {
        var expectedResponse = readFile("fixture/training/delete/response/not-found.json");

        mockMvc.perform(delete(BASE_URL + "/{trainingId}", NON_EXISTING_TRAINING_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNotFound())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void deleteTraining_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{trainingId}", EXISTING_TRAINING_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteTraining_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var expectedResponse = readFile("fixture/training/delete/response/access-denied.json");

        mockMvc.perform(delete("/api/tenants/{tenantId}/trainings/{trainingId}", DIFFERENT_TENANT_ID, EXISTING_TRAINING_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "TRAINER"})
    void deleteTraining_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{trainingId}", EXISTING_TRAINING_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql("/db/training/insert.sql")
    void findById_whenTrainingExists_thenReturnTraining() throws Exception {
        var expectedResponse = readFile("fixture/training/findById/response/success.json");

        mockMvc.perform(get(BASE_URL + "/{trainingId}", EXISTING_TRAINING_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void findById_whenTrainingNotFound_thenReturn404() throws Exception {
        var expectedResponse = readFile("fixture/training/findById/response/not-found.json");

        mockMvc.perform(get(BASE_URL + "/{trainingId}", NON_EXISTING_TRAINING_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNotFound())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void findById_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{trainingId}", EXISTING_TRAINING_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findById_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var expectedResponse = readFile("fixture/training/findById/response/access-denied.json");

        mockMvc.perform(get("/api/tenants/{tenantId}/trainings/{trainingId}", DIFFERENT_TENANT_ID, EXISTING_TRAINING_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    @Sql("/db/training/insert.sql")
    void findByTenantId_whenTrainingsExist_thenReturnPaginatedTrainings() throws Exception {
        var expectedResponse = readFile("fixture/training/findByTenantId/response/success.json");

        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "name,desc")
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
        var expectedResponse = readFile("fixture/training/findByTenantId/response/access-denied.json");

        mockMvc.perform(get("/api/tenants/{tenantId}/trainings", DIFFERENT_TENANT_ID)
                        .param("page", "0")
                        .param("size", "10")
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    @Sql({"/db/training/insert.sql", "/db/payment/insert-tariffs.sql", "/db/payment/insert-training-tariffs.sql"})
    void updatePaymentTariffs_whenValidRequest_thenUpdateTariffs() throws Exception {
        var request = readFile("fixture/training/updatePaymentTariffs/request/valid-request.json");

        mockMvc.perform(put(BASE_URL + "/{trainingId}/tariffs", EXISTING_TRAINING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isOk());
    }

    @Test
    void updatePaymentTariffs_whenInvalidRequest_thenReturnBadRequest() throws Exception {
        var request = readFile("fixture/training/updatePaymentTariffs/request/invalid-request.json");
        var expectedResponse = readFile("fixture/training/updatePaymentTariffs/response/invalid.json");

        mockMvc.perform(put(BASE_URL + "/{trainingId}/tariffs", EXISTING_TRAINING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void updatePaymentTariffs_whenTrainingNotFound_thenReturn404() throws Exception {
        var request = readFile("fixture/training/updatePaymentTariffs/request/valid-request.json");
        var expectedResponse = readFile("fixture/training/updatePaymentTariffs/response/not-found.json");

        mockMvc.perform(put(BASE_URL + "/{trainingId}/tariffs", NON_EXISTING_TRAINING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNotFound())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void updatePaymentTariffs_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        var request = readFile("fixture/training/updatePaymentTariffs/request/valid-request.json");

        mockMvc.perform(put(BASE_URL + "/{trainingId}/tariffs", EXISTING_TRAINING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updatePaymentTariffs_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var request = readFile("fixture/training/updatePaymentTariffs/request/valid-request.json");
        var expectedResponse = readFile("fixture/training/updatePaymentTariffs/response/access-denied.json");

        mockMvc.perform(put("/api/tenants/{tenantId}/trainings/{trainingId}/tariffs", DIFFERENT_TENANT_ID, EXISTING_TRAINING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "TRAINER"})
    void updatePaymentTariffs_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        var request = readFile("fixture/training/updatePaymentTariffs/request/valid-request.json");

        mockMvc.perform(put(BASE_URL + "/{trainingId}/tariffs", EXISTING_TRAINING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql({"/db/training/insert.sql", "/db/payment/insert-tariffs.sql", "/db/payment/insert-training-tariffs.sql"})
    void getPaymentTariffs_whenTrainingExists_thenReturnTariffs() throws Exception {
        var expectedResponse = readFile("fixture/training/getPaymentTariffs/response/success.json");

        mockMvc.perform(get(BASE_URL + "/{trainingId}/tariffs", EXISTING_TRAINING_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void getPaymentTariffs_whenTrainingNotFound_thenReturn404() throws Exception {
        var expectedResponse = readFile("fixture/training/getPaymentTariffs/response/not-found.json");

        mockMvc.perform(get(BASE_URL + "/{trainingId}/tariffs", NON_EXISTING_TRAINING_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNotFound())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void getPaymentTariffs_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{trainingId}/tariffs", EXISTING_TRAINING_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getPaymentTariffs_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var expectedResponse = readFile("fixture/training/getPaymentTariffs/response/access-denied.json");

        mockMvc.perform(get("/api/tenants/{tenantId}/trainings/{trainingId}/tariffs", DIFFERENT_TENANT_ID, EXISTING_TRAINING_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }
} 