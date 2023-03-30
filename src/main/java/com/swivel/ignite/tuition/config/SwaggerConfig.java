package com.swivel.ignite.tuition.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {


    @Bean
    public Docket api() {
        //Adding Header
        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        List<Parameter> aParameters = new ArrayList<Parameter>();

        aParameters.clear();

        aParameterBuilder.name("Authorization").modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build();
        aParameters.add(aParameterBuilder.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(generateAPIInfo())
                .select()
                //Here adding base package to scan controllers. This will scan only controllers inside
                //specific package and include in the swagger documentation
                .apis(RequestHandlerSelectors.basePackage("com.swivel.ignite.tuition"))
                .paths(PathSelectors.any())
                .build()
//                .ignoredParameterTypes(HeaderVo.class)
                .globalOperationParameters(aParameters);
    }

    //Api information
    private ApiInfo generateAPIInfo() {
        return new ApiInfo("Ignite Tuition Service", "Implementing Swagger with SpringBoot Application", "1.0.0",
                "", getContacts(), "", "", new ArrayList<>());
    }

    // Developer Contacts
    private Contact getContacts() {
        return new Contact("Mohamed Nawaz", "", "nawas@swivelgroup.com.au");
    }
}
