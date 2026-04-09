package com.eseict.apiServer.conf.hams

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


@Configuration
@EnableJpaRepositories(
    basePackages = ["com.eseict.apiServer.repository.hams"],
    entityManagerFactoryRef = "hamsEntityManager",
    transactionManagerRef = "hamsTransactionManager",
)
@ConfigurationProperties(prefix = "hams.datasource.hikari")
class HamsDataConfig : HikariConfig() {

    @Value("\${hams.jpa.properties.hibernate.dialect}")
    lateinit var dialect:String;

    @Bean(name = ["hams"])
    fun dataSource(): DataSource {
        return HikariDataSource(this)
    }

    @Autowired
    @Bean(name = ["hamsTransactionManager"])
    fun jpaTransactionManager(@Qualifier("hamsEntityManager") entityManagerFactoryBean: LocalContainerEntityManagerFactoryBean): JpaTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = entityManagerFactoryBean.getObject()
        return transactionManager
    }

    @Bean(name = ["hamsEntityManager"])
    fun entityManagerFactoryBean(
        builder: EntityManagerFactoryBuilder,
        @Qualifier("hams") ds: DataSource?
    ): LocalContainerEntityManagerFactoryBean? {
        val factoryBean = builder.dataSource(ds)
            .packages("com.eseict.apiServer.domain.hams")
            .build()
        factoryBean.setJpaProperties(jpaHibernateProperties())
        return factoryBean
    }

    private fun jpaHibernateProperties(): Properties {
        val props = Properties()
        props.setProperty("hibernate.show_sql", "true")
        props.setProperty("hibernate.format_sql", "true")
        props.setProperty("hibernate.jdbc.time_zone", ApiConstants.TIME_ZONE)
        props.setProperty("hibernate.dialect", dialect)
        return props
    }

}