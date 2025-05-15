package com.fitnycrm.common.localization;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Data
@Component
@Validated
@ConfigurationProperties(prefix = "localization")
public class LocalizationProperties {

    private Set<String> supportedLocales;
}
