package com.fitavera.payment.rest;

import com.fitavera.common.annotation.IntegrationTest;
import com.fitavera.common.util.TestUtils;
import com.fitavera.user.repository.entity.UserRole;
import com.fitavera.user.util.JwtTokenCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.fitavera.common.util.TestUtils.readFile;
import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@Sql({"/db/tenant/insert.sql", "/db/user/insert.sql"})
class PaymentTariffRestControllerTest {

    private static final String BASE_URL = "/api/tenants/7a7632b1-e932-48fd-9296-001036b4ec19/tariffs";
    private static final UUID DIFFERENT_TENANT_ID = UUID.fromString("99999999-9999-9999-9999-999999999999");
    private static final UUID EXISTING_TARIFF_ID = UUID.fromString("c35ac7f5-3e4f-462a-a76d-524bd3a5fd01");
    private static final UUID NON_EXISTING_TARIFF_ID = UUID.fromString("ad475c18-777c-4baf-a0d4-14865a0c476a");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenCreator jwtTokenCreator;

    @Test
    @Sql("/db/payment/insert-tariffs.sql")
    void findAll_shouldReturnTariffs() throws Exception {
        var expectedResponse = TestUtils.readFile("fixture/payment/tariff/findAll/response.json");

        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void findAll_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findAll_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var expectedResponse = TestUtils.readFile("fixture/payment/tariff/findAll/access-denied.json");

        mockMvc.perform(get("/api/tenants/{tenantId}/tariffs", DIFFERENT_TENANT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "TRAINER"})
    void findAll_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createTariff_whenValidRequest_thenCreateTariff() throws Exception {
        var request = readFile("fixture/payment/tariff/create/request/valid-request.json");
        var expectedResponse = readFile("fixture/payment/tariff/create/response/success.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void createTariff_whenInvalidRequest_thenReturnBadRequest() throws Exception {
        var request = readFile("fixture/payment/tariff/create/request/invalid-request.json");
        var expectedResponse = readFile("fixture/payment/tariff/create/response/invalid.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void createTariff_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        var request = readFile("fixture/payment/tariff/create/request/valid-request.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createTariff_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var request = readFile("fixture/payment/tariff/create/request/valid-request.json");
        var expectedResponse = readFile("fixture/payment/tariff/create/response/access-denied.json");

        mockMvc.perform(post("/api/tenants/{tenantId}/tariffs", DIFFERENT_TENANT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "TRAINER"})
    void createTariff_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        var request = readFile("fixture/payment/tariff/create/request/valid-request.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql("/db/payment/insert-tariffs.sql")
    void findById_whenTariffExists_thenReturnTariff() throws Exception {
        var expectedResponse = readFile("fixture/payment/tariff/findById/response/success.json");

        mockMvc.perform(get(BASE_URL + "/{tariffId}", EXISTING_TARIFF_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void findById_whenTariffNotFound_thenReturn404() throws Exception {
        var expectedResponse = readFile("fixture/payment/tariff/findById/response/not-found.json");

        mockMvc.perform(get(BASE_URL + "/{tariffId}", NON_EXISTING_TARIFF_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNotFound())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void findById_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{tariffId}", EXISTING_TARIFF_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findById_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var expectedResponse = readFile("fixture/payment/tariff/findById/response/access-denied.json");

        mockMvc.perform(get("/api/tenants/{tenantId}/tariffs/{tariffId}", DIFFERENT_TENANT_ID, EXISTING_TARIFF_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "TRAINER"})
    void findById_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        mockMvc.perform(get(BASE_URL + "/{tariffId}", EXISTING_TARIFF_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql("/db/payment/insert-tariffs.sql")
    void updateTariff_whenValidRequest_thenUpdateTariff() throws Exception {
        var request = readFile("fixture/payment/tariff/update/request/valid-request.json");
        var expectedResponse = readFile("fixture/payment/tariff/update/response/success.json");

        mockMvc.perform(put(BASE_URL + "/{tariffId}", EXISTING_TARIFF_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void updateTariff_whenInvalidRequest_thenReturnBadRequest() throws Exception {
        var request = readFile("fixture/payment/tariff/update/request/invalid-request.json");
        var expectedResponse = readFile("fixture/payment/tariff/update/response/invalid-request.json");

        mockMvc.perform(put(BASE_URL + "/{tariffId}", EXISTING_TARIFF_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void updateTariff_whenTariffNotFound_thenReturn404() throws Exception {
        var request = readFile("fixture/payment/tariff/update/request/valid-request.json");
        var expectedResponse = readFile("fixture/payment/tariff/update/response/not-found.json");

        mockMvc.perform(put(BASE_URL + "/{tariffId}", NON_EXISTING_TARIFF_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNotFound())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void updateTariff_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        var request = readFile("fixture/payment/tariff/update/request/valid-request.json");

        mockMvc.perform(put(BASE_URL + "/{tariffId}", EXISTING_TARIFF_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateTariff_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var request = readFile("fixture/payment/tariff/update/request/valid-request.json");
        var expectedResponse = readFile("fixture/payment/tariff/update/response/access-denied.json");

        mockMvc.perform(put("/api/tenants/{tenantId}/tariffs/{tariffId}", DIFFERENT_TENANT_ID, EXISTING_TARIFF_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "TRAINER"})
    void updateTariff_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        var request = readFile("fixture/payment/tariff/update/request/valid-request.json");

        mockMvc.perform(put(BASE_URL + "/{tariffId}", EXISTING_TARIFF_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql("/db/payment/insert-tariffs.sql")
    void deleteTariff_whenTariffExists_thenDeleteTariff() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{tariffId}", EXISTING_TARIFF_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTariff_whenTariffNotFound_thenReturn404() throws Exception {
        var expectedResponse = readFile("fixture/payment/tariff/delete/response/not-found.json");

        mockMvc.perform(delete(BASE_URL + "/{tariffId}", NON_EXISTING_TARIFF_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNotFound())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void deleteTariff_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{tariffId}", EXISTING_TARIFF_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteTariff_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var expectedResponse = readFile("fixture/payment/tariff/delete/response/access-denied.json");

        mockMvc.perform(delete("/api/tenants/{tenantId}/tariffs/{tariffId}", DIFFERENT_TENANT_ID, EXISTING_TARIFF_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "TRAINER"})
    void deleteTariff_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{tariffId}", EXISTING_TARIFF_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }
} 