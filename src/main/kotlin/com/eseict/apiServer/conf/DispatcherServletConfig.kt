package com.eseict.apiServer.conf

import com.eseict.apiServer.interceptor.DataSourceInterceptor
import com.eseict.apiServer.interceptor.TokenInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.resource.PathResourceResolver


@Configuration
class DispatcherServletConfig(val dataSourceInterceptor: DataSourceInterceptor) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
//        registry.addInterceptor(tokenInterceptor())
//        registry.addInterceptor(dataSourceInterceptor)
        super.addInterceptors(registry)
    }


//    @Bean
//    fun tokenInterceptor(): TokenInterceptor {
//        return TokenInterceptor()
//    }

    private val CLASSPATH_RESOURCE_LOCATIONS: ArrayList<String> = arrayListOf(
        "classpath:/META-INF/resources/", "classpath:/resources/",
        "classpath:/static/", "classpath:/templates/"
    )

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/").resourceChain(true)
            .addResolver(PathResourceResolver())
//        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/static/").resourceChain(true)
//            .addResolver(PathResourceResolver())
        registry.addResourceHandler("/**").addResourceLocations("classpath:/resources/")
        registry.addResourceHandler("/**").addResourceLocations("classpath:/templates/")
        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/")
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/")
    }

//    @Bean
//    fun dataSourceInterceptor(): DataSourceInterceptor {
//        return DataSourceInterceptor()
//    }
}