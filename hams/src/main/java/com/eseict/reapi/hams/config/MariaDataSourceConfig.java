package com.eseict.reapi.hams.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.eseict.reapi.hams.equip.repository",
    entityManagerFactoryRef = "mariaEntityManagerFactory",
    transactionManagerRef = "mariaTransactionManager"
)
public class MariaDataSourceConfig {

    @Bean
    @ConfigurationProperties("hams-equip.datasource")
    public DataSourceProperties mariaDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource mariaDataSource() {
        return mariaDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean mariaEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
            .dataSource(mariaDataSource())
            .packages("com.eseict.reapi.hams.equip.domain")
            .persistenceUnit("maria")
            .properties(Map.of("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect"))
            .build();
    }

    @Bean
    public PlatformTransactionManager mariaTransactionManager(LocalContainerEntityManagerFactoryBean mariaEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(mariaEntityManagerFactory.getObject()));
    }
}
