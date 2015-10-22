package gr.uoa.di.scan.thesis.controller;

import gr.uoa.di.scan.thesis.dto.UserDTO;
import gr.uoa.di.scan.thesis.entity.User;
import gr.uoa.di.scan.thesis.exception.FailedLoginException;
import gr.uoa.di.scan.thesis.exception.FailedSignUpException;
import gr.uoa.di.scan.thesis.exception.IsNotOwner;
import gr.uoa.di.scan.thesis.security.SecurityUtils;
import gr.uoa.di.scan.thesis.service.GenericService;
import gr.uoa.di.scan.thesis.service.UserService;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.gson.Gson;

@Controller
@RequestMapping("/users")
public class UserController extends GenericController<User,UserDTO, Long>{
	
	@Autowired
	UserService userService;
	
	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	KeyBasedPersistenceTokenService tokenService;
	
	@Override
	GenericService<User,UserDTO, Long> getService() {
		
		return userService;
	}
	
	
	@Override
	@PreAuthorize("hasRole('ROLE_USER')")
	public UserDTO update(@PathVariable Long id, @RequestBody UserDTO dto) {
		UserDTO applicant = securityUtils.getUserFromContext();
		if(id.longValue() != applicant.getId().longValue())
			throw new IsNotOwner("Applicant is not the owner of " + User.class.getCanonicalName());
		return super.update(id, dto);
	}
	
	@RequestMapping(value = "/full/{id}",method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody UserDTO get(@PathVariable int id) {
		System.out.println("Responding on endpoint request /api/users/full/id GET request");
		return userService.getFullUser((long) id);
	}

	@RequestMapping(value="/login", method = RequestMethod.POST, produces="application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody String login(@RequestBody String body){
		ObjectMapper mapper = new ObjectMapper();
		String accessToken = "";
		String socialId = "";
		try {
			accessToken = mapper.readTree(body).get("access-Token").getTextValue();
			System.out.println("access token:" + accessToken);
			socialId = mapper.readTree(body).get("socialId").getTextValue();
			System.out.println("socialid:" + socialId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuilder key = new StringBuilder();
		UserDTO user = userService.logIn(accessToken, socialId, key);

		return "[\"" + key.toString() + "\"," + new Gson().toJson(user) + "]";
	}

	@RequestMapping(value="/status", method = RequestMethod.POST, produces="application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody String getStatus(){
		System.out.println("Responding on users/status POST request");
		UserDTO user = securityUtils.getUserFromContext();
		if(user != null)
			return "loggedIn";
		else
			return "unknown";
	}
	
	@RequestMapping(value="/signup", method = RequestMethod.POST, produces="application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody String signUp(@RequestBody String body){
		ObjectMapper mapper = new ObjectMapper();
		String accessToken = "";
		String socialId = "";
		try {
			accessToken = mapper.readTree(body).get("access-Token").getTextValue();
			System.out.println("access token:" + accessToken);
			socialId = mapper.readTree(body).get("socialId").getTextValue();
			System.out.println("socialid:" + socialId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuilder key = new StringBuilder();
		UserDTO user = userService.signUp(accessToken, socialId, key);

		return "[\"" + key.toString() + "\"," + new Gson().toJson(user) + "]";
	}
	
	@ExceptionHandler(FailedLoginException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public @ResponseBody String handleFailedLoginException(FailedLoginException e) {
		System.out.println(e.getMessage());
		return e.getMessage();
	}
	
	@ExceptionHandler(FailedSignUpException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public @ResponseBody String handleFailedSignUpException(FailedSignUpException e) {
		System.out.println(e.getMessage());
		return e.getMessage();
	}
	
	@ExceptionHandler(IsNotOwner.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public @ResponseBody String handleIsNotOwner(IsNotOwner e) {
		System.out.println(e.getMessage());
		return e.getMessage();
	}
}
