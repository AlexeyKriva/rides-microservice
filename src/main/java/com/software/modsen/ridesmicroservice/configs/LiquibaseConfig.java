package com.software.modsen.ridesmicroservice.configs;

import liquibase.integration.spring.SpringLiquibase;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@AllArgsConstructor
public class LiquibaseConfig {
    private DataSource dataSource;

    @Bean
    public SpringLiquibase springLiquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:db/changelog/main-changelog.xml");
        liquibase.setLiquibaseSchema("public");
        liquibase.setShouldRun(true);

        return liquibase;
    }
}