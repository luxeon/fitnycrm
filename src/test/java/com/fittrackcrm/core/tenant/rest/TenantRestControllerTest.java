package com.fittrackcrm.core.tenant.rest;

import com.fittrackcrm.core.user.repository.entity.User;
import com.fittrackcrm.core.security.config.JwtProperties;
import com.fittrackcrm.core.common.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fittrackcrm.core.common.annotation.IntegrationTest;

import java.util.UUID;

import static com.fittrackcrm.core.common.util.TestUtils.readFile;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;

@IntegrationTest
@Sql(executionPhase =
        Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        value = "classpath:db/user/insert-admin.sql")
class TenantRestControllerTest {

    private static final String BASE_URL = "/api/tenants";
    private static final UUID TENANT_ID = UUID.fromString("7a7632b1-e932-48fd-9296-001036b4ec19");
    private static final UUID ADMIN_USER_ID = UUID.fromString("835ac7f5-3e4f-462a-a76d-524bd3a5fd00");

    private static String JWT_TOKEN;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtProperties jwtProperties;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(ADMIN_USER_ID);
        user.setFirstName("Max");
        user.setLastName("Power");
        user.setEmail("max.power@gmail.com");
        user.setPhoneNumber("+123456789");

        JWT_TOKEN = TestUtils.generateTestJwtToken(user, jwtProperties);
    }

    @Test
    @Sql("/db/tenant/insert.sql")
    void getOne_whenTenantExists_thenReturnTenant() throws Exception {
        var expectedResponse = readFile("fixture/tenant/getOne/response/tenant-1.json");

        mockMvc.perform(get(BASE_URL + "/{id}", TENANT_ID)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void getOne_whenTenantDoesNotExist_thenReturn404() throws Exception {
        var nonExistentTenantId = UUID.fromString("ac0e73c4-5a55-44a7-b05f-c4f13a8971ab");
        var expectedResponse = readFile("fixture/tenant/getOne/response/tenant-not-found.json");

        mockMvc.perform(get(BASE_URL + "/{id}", nonExistentTenantId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void getOne_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", TENANT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
} 