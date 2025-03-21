package com.fitonyashka.core.tenant.rest;

import com.fitonyashka.core.admin.repository.AdminRepository;
import com.fitonyashka.core.admin.repository.entity.Admin;
import com.fitonyashka.core.common.config.JwtProperties;
import com.fitonyashka.core.common.util.TestUtils;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fitonyashka.core.common.annotation.IntegrationTest;
import static com.fitonyashka.core.common.util.TestUtils.readFile;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;

@IntegrationTest
@Sql(executionPhase =
        Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        value = "classpath:db/admin/insert-default.sql")
class TenantControllerTest {

    private static final String BASE_URL = "/api/tenants";

    private static String JWT_TOKEN;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtProperties jwtProperties;

    @BeforeEach
    void setUp() {
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setFirstName("Max");
        admin.setLastName("Power");
        admin.setEmail("max.power@gmail.com");
        admin.setPhoneNumber("+123456789");

        JWT_TOKEN = TestUtils.generateTestJwtToken(admin, jwtProperties);
    }

    @Test
    @Sql("/db/tenant/insert.sql")
    void getOne_whenTenantExists_thenReturnTenant() throws Exception {
        var tenantId = 1L;
        var expectedResponse = readFile("fixture/tenant/getOne/response/tenant-1.json");

        mockMvc.perform(get(BASE_URL + "/{id}", tenantId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void getOne_whenTenantDoesNotExist_thenReturn404() throws Exception {
        var nonExistentTenantId = 999L;
        var expectedResponse = readFile("fixture/tenant/getOne/response/tenant-not-found.json");

        mockMvc.perform(get(BASE_URL + "/{id}", nonExistentTenantId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(json().isEqualTo(expectedResponse));
    }

    @Test
    void getOne_whenJwtTokenDoesNotExist_thenReturn401() throws Exception {
        var tenantId = 1L;

        mockMvc.perform(get(BASE_URL + "/{id}", tenantId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
} 