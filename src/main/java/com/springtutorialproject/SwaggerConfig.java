package com.springtutorialproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	Contact contact = new Contact("Hagar M. Gamal", 
			"http://localhost:8080/", 
			"hagar.mohamed.gamal@gmail.com");
	
	List<VendorExtension> vendorExtensions = new ArrayList<>();
	
	ApiInfo apiInfo = new ApiInfo("Photo App RESTful Web service documentation",
			"This pages documents photo app RESTful Web service endpoints",
			"1.0",
			"http://localhost:8080/",
			contact,
			"Apache 2.0",
			"https://www.apache.org/licenses/LICENSE-2.0",
			vendorExtensions
			);

	@Bean
	public Docket apiDocket() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				.protocols(new HashSet<>(Arrays.asList("HTTP", "HTTPs")))
				.apiInfo(apiInfo)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.springtutorialproject"))
				.paths(PathSelectors.any())
				.build();
		return docket;
	}
	
	/*
	 * @Bean public LinkDiscoverers discovers() { List<LinkDiscoverer> plugins = new
	 * ArrayList<>(); plugins.add(new CollectionJsonLinkDiscoverer()); return new
	 * LinkDiscoverers(SimplePluginRegistry.of(plugins)); }
	 */

}
