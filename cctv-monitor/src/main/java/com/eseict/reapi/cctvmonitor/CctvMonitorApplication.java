package com.eseict.reapi.cctvmonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
    "com.eseict.reapi.cctvmonitor",
    "com.eseict.reapi.common"
})
@EntityScan(basePackages = {
    "com.eseict.reapi.cctvmonitor",
    "com.eseict.reapi.common"
})
@EnableJpaRepositories(basePackages = {
    "com.eseict.reapi.cctvmonitor"
})
public class CctvMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CctvMonitorApplication.class, args);
    }
}
