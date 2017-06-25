package web.application.development.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import web.application.development.exception.Error;

public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
	
	@Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("WWW-Authenticate", "Realm=" + getRealmName() + "");
        response.addHeader("Content-Type", "application/problem+json");
        response.addHeader("Content-Language", "en");
       
        Error error = new Error("http://localhost:8080/error/authentication", "Authentication error", "Full authentication is required to access this resource.");
        PrintWriter writer = response.getWriter();
        //writer.println("HTTP Status 401 : " + authException.getMessage());
        writer.print(error.toJSON());
    }
     
    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("Academic Management System");
        super.afterPropertiesSet();
    }

}
