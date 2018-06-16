/*
 * Copyright (c) 2017 Wolkabout
 */

package com.demo.addressbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Date;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private final Logger logger = LoggerFactory.getLogger(SwaggerConfiguration.class);

    @Bean
    public Docket swaggerSpringfoxDocket() {
        logger.debug("Initializing swagger...");

        return new Docket(DocumentationType.SWAGGER_2)
                .forCodeGeneration(true)
                .genericModelSubstitutes(ResponseEntity.class)
                .ignoredParameterTypes(Pageable.class)
                .ignoredParameterTypes(java.sql.Date.class)
                .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
                .directModelSubstitute(java.time.LocalTime.class, Date.class)
                .select().paths(PathSelectors.regex("/api/.*")).build();
    }

}
