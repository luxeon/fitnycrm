package com.fitnycrm.user.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnycrm.common.annotation.IntegrationTest;
import com.fitnycrm.user.rest.model.AdminDetailsResponse;
import com.fitnycrm.user.repository.UserRepository;
import com.fitnycrm.user.repository.entity.User;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.fitnycrm.common.util.TestUtils.readFile;
import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@Sql({"/db/tenant/insert.sql", "/db/user/insert.sql", "/db/user/insert-unconfirmed-user.sql"})
class AuthRestControllerTest {

    private static final String AUTH_URL = "/api/auth";

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("test@fittrackcrm.com", "test"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_whenValidCredentials_thenReturnToken() throws Exception {
        var loginRequest = readFile("fixture/auth/login/request/valid-request.json");
        mockMvc.perform(post(AUTH_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());
    }

    @Test
    void login_whenInvalidPassword_thenReturn401() throws Exception {
        var loginRequest = readFile("fixture/auth/login/request/invalid-password.json");
        var expectedResponse = readFile("fixture/auth/login/response/invalid-credentials.json");

        mockMvc.perform(post(AUTH_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void login_whenNonExistentEmail_thenReturn401() throws Exception {
        var loginRequest = readFile("fixture/auth/login/request/invalid-email.json");
        var expectedResponse = readFile("fixture/auth/login/response/invalid-credentials.json");

        mockMvc.perform(post(AUTH_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void createAdmin_whenValidRequest_thenCreateAdminAndSendEmail() throws Exception {
        var request = readFile("fixture/auth/signup/request/valid-request.json");
        var expectedResponse = readFile("fixture/auth/signup/response/success.json");

        String response = mockMvc.perform(post(AUTH_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(json().isEqualTo(expectedResponse))
                .andReturn()
                .getResponse().getContentAsString();

        AdminDetailsResponse adminDetails = objectMapper.readValue(response, AdminDetailsResponse.class);
        User user = userRepository.findById(adminDetails.id()).orElseThrow();
        assertThat(user.getConfirmationToken()).isNotNull();
        assertThat(user.getConfirmationTokenExpiresAt()).isNotNull();
        assertThat(user.isEmailConfirmed()).isFalse();

        // Verify email was sent
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertThat(receivedMessages).hasSize(1);
        MimeMessage receivedMessage = receivedMessages[0];
        assertThat(receivedMessage.getAllRecipients()).hasSize(1);
        assertThat(receivedMessage.getAllRecipients()[0].toString()).isEqualTo("john.doe@example.com");
        assertThat(receivedMessage.getSubject()).isEqualTo("Confirm your email");
    }

    @Test
    void createAdmin_whenInvalidEmail_thenReturnBadRequest() throws Exception {
        var request = readFile("fixture/auth/signup/request/invalid-email.json");
        var expectedResponse = readFile("fixture/auth/signup/response/invalid-email.json");

        mockMvc.perform(post(AUTH_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void createAdmin_whenEmailAlreadyExists_thenReturnConflict() throws Exception {
        var request = readFile("fixture/auth/signup/request/valid-request.json");
        var expectedResponse = readFile("fixture/auth/signup/response/email-exists.json");

        // Create first admin
        mockMvc.perform(post(AUTH_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated());

        // Try to create second admin with same email
        mockMvc.perform(post(AUTH_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void confirmEmail_whenValidToken_thenConfirmEmail() throws Exception {
        var token = "valid-confirmation-token";
        
        mockMvc.perform(get(AUTH_URL + "/confirm-email")
                        .param("token", token))
                .andExpect(status().isOk());

        // Verify that email is confirmed in database
        var user = userRepository.findById(UUID.fromString("935ac7f5-3e4f-462a-a76d-524bd3a5fd01"))
                        .orElseThrow();
        assertThat(user.isEmailConfirmed()).isTrue();
        assertThat(user.getConfirmationToken()).isNull();
    }

    @Test
    void confirmEmail_whenInvalidToken_thenReturnBadRequest() throws Exception {
        var token = "invalid-token";
        var expectedResponse = readFile("fixture/auth/confirm-email/response/invalid-token.json");

        mockMvc.perform(get(AUTH_URL + "/confirm-email")
                        .param("token", token))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void confirmEmail_whenExpiredToken_thenReturnBadRequest() throws Exception {
        var token = "expired-token";
        var expectedResponse = readFile("fixture/auth/confirm-email/response/expired-token.json");

        mockMvc.perform(get(AUTH_URL + "/confirm-email")
                        .param("token", token))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(expectedResponse));
    }
} 