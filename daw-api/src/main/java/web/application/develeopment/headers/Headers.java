package web.application.develeopment.headers;

import org.springframework.http.HttpHeaders;

public class Headers {
	
	public static HttpHeaders SirenHeader() {
		final HttpHeaders header = new HttpHeaders();
	    header.add("Content-Type", "application/vnd.siren+json");
	    header.add("Content-Language", "en");
	    
	    return header;
	}
	
	public static HttpHeaders ProblemHeader() {
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "application/problem+json");
        header.add("Content-Language", "en");
        
        return header;
	}

}
