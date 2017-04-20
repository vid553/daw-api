package web.application.development;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
//import org.springframework.stereotype.*;
//import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class DawAPIApp {
	
	public static void main(String[] args) {
		SpringApplication.run(DawAPIApp.class, args);
	}
	
}
