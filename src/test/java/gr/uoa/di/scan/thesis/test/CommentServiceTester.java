package gr.uoa.di.scan.thesis.test;


import gr.uoa.di.scan.thesis.dto.CommentDTO;
import gr.uoa.di.scan.thesis.dto.PostDTO;
import gr.uoa.di.scan.thesis.dto.UserDTO;
import gr.uoa.di.scan.thesis.exception.EntityNotFoundException;
import gr.uoa.di.scan.thesis.service.CommentService;
import gr.uoa.di.scan.thesis.service.PostService;
import gr.uoa.di.scan.thesis.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@ContextConfiguration(locations={"classpath:app-context.xml"})
@TransactionConfiguration(defaultRollback = false)
public class CommentServiceTester extends AbstractTestNGSpringContextTests {

	@Autowired
	private CommentService commentService;

	@Autowired
	private UserService userService;

	@Autowired
	private PostService postService;

	private CommentDTO testComment;
	private UserDTO testUser;
	private PostDTO testPost;

	@BeforeClass
	public void beforeClass() {
		testComment = new CommentDTO();
		testComment.setBody("Test comment");

		testUser = new UserDTO();
		testUser.setId((long)1111);
		testUser.setEmail("test@test.gr");
		testUser.setPassword("test");
		testUser.setUsername("tester1");

		testPost = new PostDTO();
		testPost.setTitle("Test title");
		testPost.setBody("Test body");
	}

	@Test(priority=1, expectedExceptions = EntityNotFoundException.class)
	public void testCreateCommentWithNonExistentUser() throws EntityNotFoundException {
		testUser.setId((long)9999);
		testComment.setPostedBy(testUser);
		commentService.create(testComment);
	}

	@Test(priority=2, expectedExceptions = EntityNotFoundException.class)
	public void testCreateCommentWithNonExistentPost() throws EntityNotFoundException {

		testUser = userService.create(testUser);
		Assert.assertNotNull(testUser.getId());

		testComment.setPostedInPost(testPost);
		commentService.create(testComment);
	}

	@Test(priority=3)
	public void testCreateComment() throws EntityNotFoundException {

		testPost.setCreatedBy(testUser);
		testPost = postService.create(testPost);
		Assert.assertNotNull(testPost.getId());

		testComment.setPostedBy(testUser);
		testComment.setPostedInPost(testPost);
		testComment = commentService.create(testComment);

		Assert.assertNotNull(testComment.getId());
	}

	@Test(priority=4)
	public void testFindComment() {
		CommentDTO foundComment = commentService.findByID(testComment.getId());
		Assert.assertEquals(foundComment.getId(), testComment.getId());
	}

	@Test(priority=5)
	public void testUpdateComment() throws EntityNotFoundException {

		testComment = commentService.findByID(testComment.getId());
		testComment.setBody("Test updated comment");
		testComment = commentService.update(testComment);

		Assert.assertEquals(testComment.getBody(), "Test updated comment");
	}

	@Test(priority=6, expectedExceptions = EntityNotFoundException.class)
	public void testUpdateNonExistentComment() throws EntityNotFoundException {

		CommentDTO nonExistentComment = new CommentDTO();
		nonExistentComment.setId((long) 9999);
		nonExistentComment.setBody("Mpla mpla");

		nonExistentComment = commentService.update(nonExistentComment);
	}

	@Test(priority=7)
	public void testDeleteComment() throws EntityNotFoundException {
		Long deletedCommentId = testComment.getId();
		commentService.delete(deletedCommentId);

		Assert.assertNull(commentService.findByID(deletedCommentId));
	}

	@Test(priority=8, expectedExceptions = EntityNotFoundException.class)
	public void testDeleteNonExistentComment() throws EntityNotFoundException {
		commentService.delete(testComment.getId());
	}

}
