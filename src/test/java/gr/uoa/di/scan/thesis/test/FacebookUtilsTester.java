package gr.uoa.di.scan.thesis.test;


import gr.uoa.di.scan.thesis.dto.UserDTO;
import gr.uoa.di.scan.thesis.entity.SocialProvider;
import gr.uoa.di.scan.thesis.service.SocialUserService;
import gr.uoa.di.scan.thesis.service.UserService;
import gr.uoa.di.scan.thesis.social.FacebookUtils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gson.Gson;

@ContextConfiguration(locations={"classpath:app-context.xml"})
@TransactionConfiguration(defaultRollback = false)
public class FacebookUtilsTester extends AbstractTestNGSpringContextTests {

	@Autowired
	private FacebookUtils fbUtils;
	
	@Autowired
	UserService userService;
	
	@Autowired
	SocialUserService socialUserService;
	
	@Autowired
	Gson gson;

	private String testAccessToken;
	private UserDTO testUser;

	@BeforeClass
	public void beforeClass() {
		// Get access token from developer->tools->graph api
		testAccessToken = "";
	}

	@Test(priority=1)
	public void testGetFbUser() {
		User fbUser = fbUtils.getFbUser(testAccessToken);
		System.out.println("Fb user email: " + fbUser.getEmail() + "," + fbUser.getName());
		Assert.assertNotNull(fbUser);
	}

	@Test(priority=2)
	public void testSignUp() {
		StringBuilder key = new StringBuilder();
		testUser = userService.signUp(testAccessToken, SocialProvider.facebook.toString(), key);
		Assert.assertNotNull(testUser);
	}
	@Test(priority=3)
	public void testFbUsers() {
		List<String> tokens = socialUserService.findBySocialProvider(SocialProvider.facebook);
		System.out.println("Tokens:" + tokens);
		Assert.assertTrue(tokens.size() == 1 && tokens.get(0).equals(testAccessToken));
	}

}
