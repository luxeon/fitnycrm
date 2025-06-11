package com.fitavera.email.config;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "mail")
public class EmailProperties {

    @NotNull
    @Email
    private String from;

    @NotBlank
    private String senderName;

    @NotNull
    @URL
    private String confirmationUrl;

    @NotNull
    @URL
    private String clientRegistrationUrl;
} 