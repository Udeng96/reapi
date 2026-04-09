package com.eseict.reapi.hams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(
    scanBasePackages = {
        "com.eseict.reapi.hams",
        "com.eseict.reapi.common"
    },
    exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
    }
)
public class HamsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HamsApplication.class, args);
    }
}
