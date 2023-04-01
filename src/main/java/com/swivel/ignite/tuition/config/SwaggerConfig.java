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

    private static final String AUTH_HEADER = "Authorization";
    private static final String STRING_PARAMETER_MODEL = "string";
    private static final String HEADER_PARAMETER = "header";

    @Bean
    public Docket api() {
        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        List<Parameter> aParameters = new ArrayList<>();
        aParameters.clear();
        aParameterBuilder.name(AUTH_HEADER).modelRef(new ModelRef(STRING_PARAMETER_MODEL))
                .parameterType(HEADER_PARAMETER).required(false).build();
        aParameters.add(aParameterBuilder.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(generateAPIInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.swivel.ignite.tuition"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(aParameters);
    }

    private ApiInfo generateAPIInfo() {
        return new ApiInfo("Ignite Tuition Service", "Implementing Swagger with SpringBoot Application",
                "1.0.0", "", getContacts(), "", "", new ArrayList<>());
    }

    private Contact getContacts() {
        return new Contact("Mohamed Nawaz", "", "nawas@swivelgroup.com.au");
    }
}
