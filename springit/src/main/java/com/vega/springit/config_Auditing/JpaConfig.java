package com.vega.springit.config_Auditing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//this package to make Auditing know who is carnally logged in
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware") //Annotation to enable auditing in JPA via annotation configuration.
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }
}
