package com.fittrackcrm.core.email.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "mail")
public class EmailProperties {

    @NotBlank
    private String host;
    
    @NotNull
    private Integer port;
    
    @NotBlank
    private String username;
    
    @NotBlank
    private String password;
    
    @NotBlank
    private String from;
    
    @NotBlank
    private String confirmationUrl;
} 