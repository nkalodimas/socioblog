package gr.uoa.di.scan.thesis.test;


import gr.uoa.di.scan.thesis.dto.UserDTO;
import gr.uoa.di.scan.thesis.exception.EntityNotFoundException;
import gr.uoa.di.scan.thesis.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@ContextConfiguration(locations={"classpath:app-context.xml"})
@TransactionConfiguration(defaultRollback = false)
public class UserServiceTester extends AbstractTestNGSpringContextTests {
	
	@Autowired
	private UserService userService;
	
	private UserDTO testUser;
	
	@BeforeClass
	public void beforeClass() {
		testUser = new UserDTO();
		testUser.setEmail("test@test.gr");
		testUser.setPassword("test");
		testUser.setUsername("tester1");
	}
	
	@Test(priority=1)
	public void testCreateUser() {		
		testUser = userService.create(testUser);
		
		Assert.assertNotNull(testUser.getId());
	}
	
	@Test(priority=2)
	public void testFindUser() {
		UserDTO foundUser = userService.findByID(testUser.getId());
		
		Assert.assertEquals(foundUser.getId(), testUser.getId());
	}
	
	@Test(priority=3)
	public void testUpdateUser() throws EntityNotFoundException {
		
		testUser = userService.findByID(testUser.getId());
		testUser.setUsername("tester2");
		testUser = userService.update(testUser);
		
		Assert.assertEquals(testUser.getUsername(), "tester2");
	}

	@Test(priority=4, expectedExceptions = EntityNotFoundException.class)
	public void testUpdateNonExistentUser() throws EntityNotFoundException {
		
		UserDTO nonExistentUser = new UserDTO();
		nonExistentUser.setId((long) 9999);
		nonExistentUser.setEmail("no@test.com");
		nonExistentUser.setPassword("pass");
		nonExistentUser.setUsername("no");
		
		nonExistentUser = userService.update(nonExistentUser);
	}
	
	@Test(priority=5)
	public void testFindByEmail() {
		UserDTO user = userService.findByEmail(testUser.getEmail());
		Assert.assertEquals(user.getEmail(), testUser.getEmail());
		
		user = userService.findByEmail("nonexistent@test.com");
		Assert.assertNull(user);
	}
	
	@Test(priority=6)
	public void testDeleteUser() throws EntityNotFoundException {
		userService.delete(testUser.getId());
		
		Assert.assertNull(userService.findByID(testUser.getId()));
	}

	@Test(priority=7, expectedExceptions = EntityNotFoundException.class)
	public void testDeleteNonExistentUser() throws EntityNotFoundException {
		userService.delete(testUser.getId());
	}

}
