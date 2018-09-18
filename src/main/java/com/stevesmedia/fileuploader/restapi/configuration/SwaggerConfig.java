package com.stevesmedia.fileuploader.restapi.configuration;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {                                    
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())                          
          .build()
          .apiInfo(apiInfo());
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfo(
        					"File store project serivice RESTful api", 
        					"Apis for file operations", 
        					"Version 2", 
        					"Terms of service", 
        					new Contact("Sajeev Babu", 
        							"www.steves-media.com", 
        							"stevesmedia@steves-media.com"), 
        					"License of API", 
        					"API license URL",
        					Collections.emptyList());
   }
}
