package gr.uoa.di.scan.thesis.test;


import java.util.ArrayList;
import java.util.List;

import gr.uoa.di.scan.thesis.dto.PostDTO;
import gr.uoa.di.scan.thesis.dto.TagDTO;
import gr.uoa.di.scan.thesis.dto.UserDTO;
import gr.uoa.di.scan.thesis.exception.EntityNotFoundException;
import gr.uoa.di.scan.thesis.service.PostService;
import gr.uoa.di.scan.thesis.service.TagService;
import gr.uoa.di.scan.thesis.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@ContextConfiguration(locations={"classpath:app-context.xml"})
@TransactionConfiguration(defaultRollback = false)
public class PostServiceTester extends AbstractTestNGSpringContextTests {

	@Autowired
	private PostService postService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private TagService tagService;

	private PostDTO testPost;
	private UserDTO testUser;
	private TagDTO tagOne;
	private TagDTO tagTwo;

	@BeforeClass
	public void beforeClass() {
		testPost = new PostDTO();
		testPost.setTitle("Test title");
		testPost.setBody("Test body");

		testUser = new UserDTO();
		testUser.setEmail("test@test.gr");
		testUser.setPassword("test");
		testUser.setUsername("tester1");
		
		tagOne = new TagDTO();
		tagOne.setTitle("Tech");
		tagTwo = new TagDTO();
		tagTwo.setTitle("Edu");
	}

	@Test(priority=1, expectedExceptions = EntityNotFoundException.class)
	public void testCreatePostWithNonExistentUser() throws EntityNotFoundException {
		testUser.setId((long) 9999);
		testPost.setCreatedBy(testUser);
		postService.create(testPost);
	}

	@Test(priority=2)
	public void testCreatePost() throws EntityNotFoundException {
		testUser = userService.create(testUser);
		Assert.assertNotNull(testUser.getId());

		testPost.setCreatedBy(testUser);
		testPost = postService.create(testPost);

		Assert.assertNotNull(testPost.getId());
	}

	@Test(priority=3)
	public void testFindPost() {
		PostDTO foundPost = postService.findByID(testPost.getId());
		Assert.assertEquals(foundPost.getId(), testPost.getId());
	}

	@Test(priority=4)
	public void testUpdatePost() throws EntityNotFoundException {

		testPost = postService.findByID(testPost.getId());
		testPost.setBody("Test updated body");
		testPost = postService.update(testPost);

		Assert.assertEquals(testPost.getBody(), "Test updated body");
	}
	
	@Test(priority=4)
	public void testAddTagsToPost() throws EntityNotFoundException {
		
		tagOne = tagService.create(tagOne);
		tagTwo = tagService.create(tagTwo);
		List<Long> tagIds = new ArrayList<Long>();
		tagIds.add(tagOne.getId());
		tagIds.add(tagTwo.getId());
		testPost = postService.addTags(testPost, tagIds);
		Assert.assertTrue(testPost.getTags().size() == 2);
		Assert.assertTrue(testPost.getTags().contains(tagOne));
		Assert.assertTrue(testPost.getTags().contains(tagTwo));
		
		Page<PostDTO> page = postService.getPostsByTag(tagOne.getTitle(), 1);
		Assert.assertTrue(page.getNumberOfElements() == 1);
		System.out.println("postsof tag one:" + page.getContent().toString());
		
	}
	
	@Test(priority=4)
	public void testGetPostByUser() throws EntityNotFoundException {
		
		Page<PostDTO> page = postService.getPostsByUser(testUser.getId(), 1);
		Assert.assertTrue(page.getNumberOfElements() == 1);
	}

	@Test(priority=5, expectedExceptions = EntityNotFoundException.class)
	public void testUpdateNonExistentPost() throws EntityNotFoundException {

		PostDTO nonExistentPost = new PostDTO();
		nonExistentPost.setId((long) 9999);
		nonExistentPost.setTitle("Mpla");
		nonExistentPost.setBody("Mpla mpla");

		nonExistentPost = postService.update(nonExistentPost);
	}

	@Test(priority=6)
	public void testDeleteTag() throws EntityNotFoundException {
		
		Assert.assertNotNull(tagService.findByID(tagOne.getId()));
		tagService.delete(tagOne.getId());
		Assert.assertNull(tagService.findByID(tagOne.getId()));
	}
	
	@Test(priority=7)
	public void testAfterDeleteTag() throws EntityNotFoundException {
		testPost = postService.findByID(testPost.getId());
		Assert.assertTrue(testPost.getTags().size() == 1);
		Assert.assertTrue(testPost.getTags().contains(tagTwo));
	}

	@Test(priority=8)
	public void testDeletePost() throws EntityNotFoundException {
		
		Assert.assertNotNull(postService.findByID(testPost.getId()));
		postService.delete(testPost.getId());
		Assert.assertNull(postService.findByID(testPost.getId()));
	}

	@Test(priority=9, expectedExceptions = EntityNotFoundException.class)
	public void testDeleteNonExistentPost() throws EntityNotFoundException {
		postService.delete(testPost.getId());
	}

}
