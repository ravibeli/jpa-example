package com.hr.app.jpaexample.config;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang3.LocaleUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class LocaleConfig {

    @Value("${jpa-example.default-locale}")
    private String defaultLocale;

    @Value("${jpa-example.supported-locales}")
    private List<String> supportedLocales;

    @Bean
    public LocaleResolver localeResolver() {
        final AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setSupportedLocales(
                supportedLocales.stream().map((l) -> LocaleUtils.toLocale(l)).collect(Collectors.toList()));
        resolver.setDefaultLocale(LocaleUtils.toLocale(defaultLocale));
        return resolver;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding(CharEncoding.UTF_8);
        return messageSource;
    }

}
