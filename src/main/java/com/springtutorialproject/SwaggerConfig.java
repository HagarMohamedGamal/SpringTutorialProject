package com.springtutorialproject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket apiDocket() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2).select()
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
