package com.eseict.reapi.hams.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.eseict.reapi.hams.hams.repository",
    entityManagerFactoryRef = "pgEntityManagerFactory",
    transactionManagerRef = "pgTransactionManager"
)
public class PgDataSourceConfig {

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder(JpaVendorAdapter jpaVendorAdapter) {
        return new EntityManagerFactoryBuilder(jpaVendorAdapter, Map.of(), null);
    }

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties pgDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean
    public DataSource pgDataSource() {
        return pgDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean pgEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
            .dataSource(pgDataSource())
            .packages("com.eseict.reapi.hams.hams.domain")
            .persistenceUnit("pg")
            .properties(Map.of("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"))
            .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager pgTransactionManager(LocalContainerEntityManagerFactoryBean pgEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(pgEntityManagerFactory.getObject()));
    }
}
