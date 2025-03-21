package com.fittrackcrm.core.user.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;

import com.fittrackcrm.core.common.annotation.IntegrationTest;
import static com.fittrackcrm.core.common.util.TestUtils.readFile;

@IntegrationTest
class UserRestControllerTest {

    private static final String BASE_URL = "/api/users";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void signup_whenValidRequest_thenCreateUser() throws Exception {
        var request = readFile("fixture/user/signup/request/valid-request.json");
        var expectedResponse = readFile("fixture/user/signup/response/success.json");

        mockMvc.perform(post(BASE_URL + "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isCreated())
            .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void signup_whenInvalidEmail_thenReturnBadRequest() throws Exception {
        var request = readFile("fixture/user/signup/request/invalid-email.json");
        var expectedResponse = readFile("fixture/user/signup/response/invalid-email.json");

        mockMvc.perform(post(BASE_URL + "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isBadRequest())
            .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void signup_whenEmailAlreadyExists_thenReturnConflict() throws Exception {
        var request = readFile("fixture/user/signup/request/valid-request.json");
        var expectedResponse = readFile("fixture/user/signup/response/email-exists.json");

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