package gr.uoa.di.scan.thesis.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component("restAuthenticationEntryPoint")
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint{

	@SuppressWarnings("deprecation")
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authEx) throws IOException, ServletException {
		response.setContentType("application/json");
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	}

}
