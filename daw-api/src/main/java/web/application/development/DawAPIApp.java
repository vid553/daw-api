package web.application.development;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.*;
//import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.ComponentScan;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class DawAPIApp {
	
	public static void main(String[] args) {
		SpringApplication.run(DawAPIApp.class, args);
	}
	
}
