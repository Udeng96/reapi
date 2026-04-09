package com.eseict.apiServer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import javax.annotation.PostConstruct

@EnableScheduling
@SpringBootApplication
@ConfigurationPropertiesScan
class ApiServerApplication() {
}

fun main(args: Array<String>) {
    runApplication<ApiServerApplication>(*args)
}
