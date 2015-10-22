package gr.uoa.di.scan.thesis.security;

import gr.uoa.di.scan.thesis.dto.UserDTO;
import gr.uoa.di.scan.thesis.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

	@Autowired
	UserService userService;

	public UserDTO getUserFromContext(){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null && authentication instanceof UsernamePasswordAuthenticationToken ){
			UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken)authentication;
			if (auth != null) {
				String id = auth.getCredentials().toString();
				if(!id.isEmpty())
					return userService.findByID(Long.parseLong(id));
			}
		}
		return null;
	}
}
