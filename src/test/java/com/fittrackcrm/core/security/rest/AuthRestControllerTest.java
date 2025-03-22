package com.fittrackcrm.core.security.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fittrackcrm.core.common.annotation.IntegrationTest;
import static com.fittrackcrm.core.common.util.TestUtils.readFile;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;

@IntegrationTest
@Sql({"/db/tenant/insert.sql", "/db/user/insert-admin.sql"})
class AuthRestControllerTest {

    private static final String AUTH_URL = "/api/auth";

    @Autowired
    private MockMvc mockMvc;

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
    void createAdmin_whenValidRequest_thenCreateAdmin() throws Exception {
        var request = readFile("fixture/auth/signup/request/valid-request.json");
        var expectedResponse = readFile("fixture/auth/signup/response/success.json");

        mockMvc.perform(post(AUTH_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(json().isEqualTo(expectedResponse));
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
} 