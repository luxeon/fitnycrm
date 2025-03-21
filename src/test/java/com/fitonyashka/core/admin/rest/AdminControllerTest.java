package com.fitonyashka.core.admin.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;

import com.fitonyashka.core.common.annotation.IntegrationTest;
import static com.fitonyashka.core.common.util.TestUtils.readFile;

@IntegrationTest
class AdminControllerTest {

    private static final String BASE_URL = "/api/admins";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void signup_whenValidRequest_thenCreateAdmin() throws Exception {
        var request = readFile("fixture/admin/signup/request/valid-request.json");
        var expectedResponse = readFile("fixture/admin/signup/response/success.json");

        mockMvc.perform(post(BASE_URL + "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isCreated())
            .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void signup_whenInvalidEmail_thenReturnBadRequest() throws Exception {
        var request = readFile("fixture/admin/signup/request/invalid-email.json");
        var expectedResponse = readFile("fixture/admin/signup/response/invalid-email.json");

        mockMvc.perform(post(BASE_URL + "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isBadRequest())
            .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void signup_whenEmailAlreadyExists_thenReturnConflict() throws Exception {
        var request = readFile("fixture/admin/signup/request/valid-request.json");
        var expectedResponse = readFile("fixture/admin/signup/response/email-exists.json");

        // Create first admin
        mockMvc.perform(post(BASE_URL + "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isCreated());

        // Try to create second admin with same email
        mockMvc.perform(post(BASE_URL + "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isConflict())
            .andExpect(json().isEqualTo(expectedResponse));
    }
} 