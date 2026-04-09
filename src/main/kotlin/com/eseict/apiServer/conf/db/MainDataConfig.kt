package com.eseict.apiServer.conf.db

import com.eseict.apiServer.conf.ApiConstants
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import java.util.*
import javax.sql.DataSource


@EnableJpaRepositories(
    basePackages = [
        "com.eseict.apiServer.repository.rino",
        "com.eseict.apiServer.repository.eas",
        "com.eseict.apiServer.repository.mobile",
    ],
    entityManagerFactoryRef = "rinoEm", transactionManagerRef = "rinoTm"
)
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.hikari")
class MainDataConfig(
) : HikariConfig() {

    @Value("\${spring.jpa.properties.hibernate.dialect}")
    lateinit var dialect:String;

    @Primary
    @Bean(name = ["rino"])
    fun dataSource(): DataSource {
        return HikariDataSource(this)
    }

    @Primary
    @Autowired
    @Bean(name = ["rinoTm"])
    fun jpaTransactionManager(@Qualifier("rinoEm") entityManagerFactoryBean: LocalContainerEntityManagerFactoryBean): JpaTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = entityManagerFactoryBean.getObject()
        return transactionManager
    }

    @Primary
    @Bean(name = ["rinoEm"])
    fun entityManagerFactoryBean(
        builder: EntityManagerFactoryBuilder,
        @Qualifier("rino") ds: DataSource?
    ): LocalContainerEntityManagerFactoryBean? {
        val factoryBean = builder.dataSource(ds)
            .packages(
                "com.eseict.apiServer.domain.rino",
                "com.eseict.apiServer.domain.xeus",
                "com.eseict.apiServer.domain.eas"
            )
            .build()
        factoryBean.setJpaProperties(jpaHibernateProperties())
        return factoryBean
    }

    private fun jpaHibernateProperties(): Properties {
        val props = Properties()

//        props.setProperty("hibernate.show_sql", if (com.eseict.common.base.SystemProperties.getProperty("IS_DEV").toBoolean()) "true" else "false")
        props.setProperty("hibernate.show_sql", "false")
        props.setProperty("hibernate.format_sql", "false")
        props.setProperty("hibernate.jdbc.time_zone", ApiConstants.TIME_ZONE)
        props.setProperty("hibernate.dialect", dialect)
        return props
    }
}