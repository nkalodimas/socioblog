package gr.uoa.di.scan.thesis.security;

import gr.uoa.di.scan.thesis.dto.UserDTO;
import gr.uoa.di.scan.thesis.service.UserService;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.DefaultToken;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

    @Autowired UserService userService;
	@Autowired KeyBasedPersistenceTokenService tokenService;
    AuthenticationManager authManager;

    private static final String HEADER_SECURITY_TOKEN = "Spring-Token";

    public AuthenticationTokenProcessingFilter(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        @SuppressWarnings("unchecked")

        HttpServletRequest req = (HttpServletRequest) request;

        String tokenKey = req.getHeader(HEADER_SECURITY_TOKEN);

        if(tokenKey != null && !tokenKey.isEmpty()) {
            // validate the token
            DefaultToken token = (DefaultToken) tokenService.verifyToken(tokenKey);
            if (token != null) {
                // determine the user based on the (already validated) token
                UserDTO user = userService.findByID( Long.parseLong(token.getExtendedInformation()) ); //tokenUtils.getUserFromToken(tok);
                // build an Authentication object with the user's info
                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(user.getEmail(), user.getId().toString());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));
                // set the authentication into the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authManager.authenticate(authentication));         
            }
        }
        // continue through the filter chain
        chain.doFilter(request, response);
    }
}