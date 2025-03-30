package com.fitnycrm.user.rest;

import com.fitnycrm.common.annotation.IntegrationTest;
import com.fitnycrm.user.repository.entity.UserRole;
import com.fitnycrm.user.util.JwtTokenCreator;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@Sql({"/db/tenant/insert.sql", "/db/user/insert.sql"})
class ClientRestControllerTest {

    private static final String BASE_URL = "/api/tenants/7a7632b1-e932-48fd-9296-001036b4ec19/clients";
    private static final UUID DIFFERENT_TENANT_ID = UUID.fromString("b35ac7f5-3e4f-462a-a76d-524bd3a5fd03");
    private static final UUID EXISTING_CLIENT_ID = UUID.fromString("c35ac7f5-3e4f-462a-a76d-524bd3a5fd01");
    private static final UUID NON_EXISTING_CLIENT_ID = UUID.fromString("c35ac7f5-3e4f-462a-a76d-524bd3a5fd99");

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("test@fittrackcrm.com", "test"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenCreator jwtTokenCreator;

    @Test
    void createClient_whenValidRequest_thenCreateClient() throws Exception {
        var request = readFile("fixture/client/create/request/valid-request.json");
        var expectedResponse = readFile("fixture/client/create/response/success.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isCreated())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void createClient_whenInvalidEmail_thenReturnBadRequest() throws Exception {
        var request = readFile("fixture/client/create/request/invalid-email.json");
        var expectedResponse = readFile("fixture/client/create/response/invalid-email.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    @Sql("/db/client/insert.sql")
    void createClient_whenEmailAlreadyExists_thenReturnConflict() throws Exception {
        var request = readFile("fixture/client/create/request/valid-request.json");
        var expectedResponse = readFile("fixture/client/create/response/email-exists.json");

        // Try to create second client with same email
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isConflict())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void createClient_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        var request = readFile("fixture/client/create/request/valid-request.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createClient_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var request = readFile("fixture/client/create/request/valid-request.json");
        var expectedResponse = readFile("fixture/client/create/response/access-denied.json");

        mockMvc.perform(post("/api/tenants/{tenantId}/clients", DIFFERENT_TENANT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "TRAINER"})
    void createClient_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        var request = readFile("fixture/client/create/request/valid-request.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql("/db/client/insert.sql")
    void updateClient_whenValidRequest_thenUpdateClient() throws Exception {
        var request = readFile("fixture/client/update/request/valid-request.json");
        var expectedResponse = readFile("fixture/client/update/response/success.json");

        mockMvc.perform(put(BASE_URL + "/{clientId}", EXISTING_CLIENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void updateClient_whenInvalidEmail_thenReturnBadRequest() throws Exception {
        var request = readFile("fixture/client/update/request/invalid-email.json");
        var expectedResponse = readFile("fixture/client/update/response/invalid-email.json");

        mockMvc.perform(put(BASE_URL + "/{clientId}", EXISTING_CLIENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void updateClient_whenClientNotFound_thenReturn404() throws Exception {
        var request = readFile("fixture/client/update/request/valid-request.json");
        var expectedResponse = readFile("fixture/client/update/response/not-found.json");

        mockMvc.perform(put(BASE_URL + "/{clientId}", NON_EXISTING_CLIENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNotFound())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    @Sql("/db/client/insert.sql")
    void updateClient_whenEmailAlreadyExists_thenReturnConflict() throws Exception {
        var request = readFile("fixture/client/update/request/valid-request.json");
        var expectedResponse = readFile("fixture/client/update/response/email-exists.json");

        // Update client with email that already exists
        mockMvc.perform(put(BASE_URL + "/{clientId}", EXISTING_CLIENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request.replace("james.bond.updated@example.com", "jane.smith@gmail.com"))
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isConflict())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void updateClient_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        var request = readFile("fixture/client/update/request/valid-request.json");

        mockMvc.perform(put(BASE_URL + "/{clientId}", EXISTING_CLIENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateClient_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var request = readFile("fixture/client/update/request/valid-request.json");
        var expectedResponse = readFile("fixture/client/create/response/access-denied.json");

        mockMvc.perform(put("/api/tenants/{tenantId}/clients/{clientId}", DIFFERENT_TENANT_ID, EXISTING_CLIENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "TRAINER"})
    void updateClient_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        var request = readFile("fixture/client/update/request/valid-request.json");

        mockMvc.perform(put(BASE_URL + "/{clientId}", EXISTING_CLIENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql("/db/client/insert.sql")
    void deleteClient_whenClientExists_thenDeleteClient() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{clientId}", EXISTING_CLIENT_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteClient_whenClientNotFound_thenReturn404() throws Exception {
        var expectedResponse = readFile("fixture/client/update/response/not-found.json");

        mockMvc.perform(delete(BASE_URL + "/{clientId}", NON_EXISTING_CLIENT_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNotFound())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void deleteClient_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{clientId}", EXISTING_CLIENT_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteClient_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var expectedResponse = readFile("fixture/client/create/response/access-denied.json");

        mockMvc.perform(delete("/api/tenants/{tenantId}/clients/{clientId}", DIFFERENT_TENANT_ID, EXISTING_CLIENT_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "TRAINER"})
    void deleteClient_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{clientId}", EXISTING_CLIENT_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql("/db/client/insert.sql")
    void findById_whenClientExists_thenReturnClient() throws Exception {
        var expectedResponse = readFile("fixture/client/findById/response/success.json");

        mockMvc.perform(get(BASE_URL + "/{clientId}", EXISTING_CLIENT_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void findById_whenClientNotFound_thenReturn404() throws Exception {
        var expectedResponse = readFile("fixture/client/findById/response/not-found.json");

        mockMvc.perform(get(BASE_URL + "/{clientId}", NON_EXISTING_CLIENT_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNotFound())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void findById_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{clientId}", EXISTING_CLIENT_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findById_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var expectedResponse = readFile("fixture/client/findById/response/access-denied.json");

        mockMvc.perform(get("/api/tenants/{tenantId}/clients/{clientId}", DIFFERENT_TENANT_ID, EXISTING_CLIENT_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT", "TRAINER"})
    void findById_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        mockMvc.perform(get(BASE_URL + "/{clientId}", EXISTING_CLIENT_ID)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql("/db/client/insert.sql")
    void findByTenantId_whenClientsExist_thenReturnPaginatedClients() throws Exception {
        var expectedResponse = readFile("fixture/client/findByTenantId/response/success.json");

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
        var expectedResponse = readFile("fixture/client/findByTenantId/response/access-denied.json");

        mockMvc.perform(get("/api/tenants/{tenantId}/clients", DIFFERENT_TENANT_ID)
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

    @Test
    void invite_whenValidRequest_thenSendInvitationEmail() throws Exception {
        var request = readFile("fixture/client/invite/request/valid-request.json");

        mockMvc.perform(post(BASE_URL + "/invite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isNoContent());

        // Verify email was sent
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertThat(receivedMessages).hasSize(1);
        MimeMessage receivedMessage = receivedMessages[0];
        assertThat(receivedMessage.getAllRecipients()).hasSize(1);
        assertThat(receivedMessage.getAllRecipients()[0].toString()).isEqualTo("client@example.com");
        assertThat(receivedMessage.getSubject()).isEqualTo("You've been invited to FitTrack CRM by Max Power");
    }

    @Test
    void invite_whenInvalidEmail_thenReturnBadRequest() throws Exception {
        var request = readFile("fixture/client/invite/request/invalid-email.json");
        var expectedResponse = readFile("fixture/client/invite/response/invalid-email.json");

        mockMvc.perform(post(BASE_URL + "/invite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void invite_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        var request = readFile("fixture/client/invite/request/valid-request.json");

        mockMvc.perform(post(BASE_URL + "/invite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void invite_whenUserHasDifferentTenant_thenReturn403() throws Exception {
        var request = readFile("fixture/client/invite/request/valid-request.json");
        var expectedResponse = readFile("fixture/client/create/response/access-denied.json");

        mockMvc.perform(post("/api/tenants/{tenantId}/clients/invite", DIFFERENT_TENANT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.Name.class, names = {"CLIENT"})
    void invite_whenUserHasUnauthorizedRole_thenReturn403(UserRole.Name role) throws Exception {
        var request = readFile("fixture/client/invite/request/valid-request.json");

        mockMvc.perform(post(BASE_URL + "/invite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateTestJwtToken(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql("/db/client/insert.sql")
    void invite_whenEmailExists_thenReturnConflict() throws Exception {
        var request = readFile("fixture/client/invite/request/existing-email.json");
        var expectedResponse = readFile("fixture/client/invite/response/email-exists.json");

        mockMvc.perform(post(BASE_URL + "/invite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isConflict())
                .andExpect(json().isEqualTo(expectedResponse));
    }
} 