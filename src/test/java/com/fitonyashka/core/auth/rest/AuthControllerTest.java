package com.fitonyashka.core.auth.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fitonyashka.core.common.annotation.IntegrationTest;
import static com.fitonyashka.core.common.util.TestUtils.readFile;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;

@IntegrationTest
class AuthControllerTest {

    private static final String AUTH_URL = "/api/auth";
    private static final String ADMIN_URL = "/api/admins";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void login_whenValidCredentials_thenReturnToken() throws Exception {
        // Create admin account first
        var signupRequest = readFile("fixture/admin/signup/request/valid-request.json");
        mockMvc.perform(post(ADMIN_URL + "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupRequest))
                .andExpect(status().isCreated());

        // Try to login
        var loginRequest = readFile("fixture/auth/login/request/valid-request.json");
        mockMvc.perform(post(AUTH_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void login_whenInvalidCredentials_thenReturn401() throws Exception {
        // Create admin account first
        var signupRequest = readFile("fixture/admin/signup/request/valid-request.json");
        mockMvc.perform(post(ADMIN_URL + "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupRequest))
                .andExpect(status().isCreated());

        // Try to login with wrong password
        var loginRequest = readFile("fixture/auth/login/request/invalid-credentials.json");
        var expectedResponse = readFile("fixture/auth/login/response/invalid-credentials.json");

        mockMvc.perform(post(AUTH_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void login_whenNonExistentEmail_thenReturn401() throws Exception {
        var loginRequest = readFile("fixture/auth/login/request/valid-request.json");
        var expectedResponse = readFile("fixture/auth/login/response/invalid-credentials.json");

        mockMvc.perform(post(AUTH_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(json().isEqualTo(expectedResponse));
    }
} 