package web.application.development.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
 
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, proxyTargetClass = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static String REALM="Academic Management System";
	
	@Autowired
	public void configureGlobalSecutiry(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("teacher").password("teacher").roles("TEACHER");
		auth.inMemoryAuthentication().withUser("student").password("student").roles("STUDENT");
		auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeRequests()
		.antMatchers("/**").hasAnyRole("TEACHER", "STUDENT", "ADMIN")
		.and().httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Bean
    public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint(){
        return new CustomBasicAuthenticationEntryPoint();
    }
	
	 @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
	 
	 //how to check authorization?
	 /* 1. authentication - we get user
	  * 2. create a context that contains the user, and the roles for the user, one way is by having a bean, acts as a container for the information, every component that needs to check access level depends on the bean
	  * 
	  * error instance ID in response, no need for description, just an ID that allows someone with access to the logs to find out what happened
	  */
}
