package com.fittrackcrm.core.user.rest;

import com.fittrackcrm.core.security.util.JwtTokenCreator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;

import com.fittrackcrm.core.common.annotation.IntegrationTest;

import static com.fittrackcrm.core.common.util.TestUtils.readFile;

@IntegrationTest
@Sql({"/db/tenant/insert.sql", "/db/user/insert-admin.sql"})
class UserRestControllerTest {

    private static final String BASE_URL = "/api/tenants/7a7632b1-e932-48fd-9296-001036b4ec19/clients";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenCreator jwtTokenCreator;

    @Test
    void createClient_whenValidRequest_thenCreateClient() throws Exception {
        var request = readFile("fixture/user/createClient/request/valid-request.json");
        var expectedResponse = readFile("fixture/user/createClient/response/success.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isCreated())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void createClient_whenInvalidEmail_thenReturnBadRequest() throws Exception {
        var request = readFile("fixture/user/createClient/request/invalid-email.json");
        var expectedResponse = readFile("fixture/user/createClient/response/invalid-email.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void createClient_whenEmailAlreadyExists_thenReturnConflict() throws Exception {
        var request = readFile("fixture/user/createClient/request/valid-request.json");
        var expectedResponse = readFile("fixture/user/createClient/response/email-exists.json");

        // Create first admin
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isCreated());

        // Try to create second admin with same email
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(HttpHeaders.AUTHORIZATION, jwtTokenCreator.generateAdminTestJwtToken()))
                .andExpect(status().isConflict())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void createClient_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        var request = readFile("fixture/user/createClient/request/valid-request.json");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized());
    }
} 