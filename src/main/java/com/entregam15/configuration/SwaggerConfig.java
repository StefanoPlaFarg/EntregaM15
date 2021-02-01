/**
 * 
 */
package com.entregam15.configuration;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


//Configuration of Swagger
@Configuration
@EnableSwagger2
public class SwaggerConfig{
	
	@Bean
	public Docket apiDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.globalOperationParameters(Arrays.asList(
						new ParameterBuilder().
							name("Authorization") //Let introduce parameters to use the API correctly on Swagger. In this case the authentication
							.description("Authentication token").modelRef(new ModelRef("string"))
							.parameterType("header")
							.required(false)
							.build()
						))
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.entregam15.controller"))//analyze this package to to fetch the APIs
				.paths(PathSelectors.any())
				.build()
				.apiInfo(getApiInfo())
				;
	}
	
	private ApiInfo getApiInfo() {
		return new ApiInfo(
				"Order Service API",
				"Order Service API Description",
				"1.0",
				"http://mypage.com/terms",
				new Contact("MyPAGE", "https://mypage.com", "apis@mypage.com"),
				"LICENSE",
				"LICENSE URL",
				Collections.emptyList()
				);
	}
	
	
}