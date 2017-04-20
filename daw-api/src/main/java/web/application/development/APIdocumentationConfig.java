package web.application.development;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;

import static springfox.documentation.builders.PathSelectors.*;

@Configuration
public class APIdocumentationConfig {
    @Bean
    public Docket documentation() {
        return new Docket(DocumentationType.SWAGGER_2)
          .select()
            .apis(RequestHandlerSelectors.any())
            .paths(regex("/.*"))
            .build()
          .pathMapping("/")
          .apiInfo(podatki());
    }
    
    @Bean
    public UiConfiguration uiConfig() {
      return UiConfiguration.DEFAULT;
    }
    private ApiInfo podatki() {
      return new ApiInfoBuilder()
        .title("API documentation - Miha test 22 - err codes handling")
        .description("Documentation of API for academic management system")
        .version("1.0")
        .build();
    }
}