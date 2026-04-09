package com.eseict.reapi.dstr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
    "com.eseict.reapi.dstr",
    "com.eseict.reapi.common"
})
@EntityScan(basePackages = {
    "com.eseict.reapi.dstr",
    "com.eseict.reapi.common"
})
@EnableJpaRepositories(basePackages = {
    "com.eseict.reapi.dstr"
})
public class DstrApplication {

    public static void main(String[] args) {
        SpringApplication.run(DstrApplication.class, args);
    }
}
