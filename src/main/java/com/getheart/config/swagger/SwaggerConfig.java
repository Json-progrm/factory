package com.getheart.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @author Json
 * @date 2020-11-16:15
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //配置要扫描接口的方式
                .apis(RequestHandlerSelectors.basePackage("com.getheart.controller"))
                .build();
    }


    private ApiInfo apiInfo(){

        //作者信息
        Contact contact = new Contact("俊杰", "", "2510343480@qq.com");

        return  new ApiInfo(
                "俊杰的SwaggerAPI文档",
                "陪你昂首直到世界尽头！",
                "1.0",
                "urn:tos",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList()
        );

    }


}
