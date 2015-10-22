package gr.uoa.di.scan.thesis.controller;

import gr.uoa.di.scan.thesis.dto.CommentDTO;
import gr.uoa.di.scan.thesis.dto.PostDTO;
import gr.uoa.di.scan.thesis.dto.UserDTO;
import gr.uoa.di.scan.thesis.entity.Post;
import gr.uoa.di.scan.thesis.exception.IsNotOwner;
import gr.uoa.di.scan.thesis.security.SecurityUtils;
import gr.uoa.di.scan.thesis.service.CommentService;
import gr.uoa.di.scan.thesis.service.GenericService;
import gr.uoa.di.scan.thesis.service.PostService;
import gr.uoa.di.scan.thesis.service.TagService;
import gr.uoa.di.scan.thesis.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/posts")
public class PostController extends GenericController<Post,PostDTO, Long>{
	
	@Autowired
	PostService postService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	TagService tagService;

	@Autowired
	CommentService commentService;

	@Autowired
	SecurityUtils securityUtils;

	@Override
	GenericService<Post,PostDTO, Long> getService() {
		
		return postService;
	}

	@Override
	@PreAuthorize("hasRole('ROLE_USER')")
	public PostDTO create(@RequestBody PostDTO dto) {
		UserDTO owner = securityUtils.getUserFromContext();
		dto.setCreatedBy(owner);
		return super.create(dto);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_USER')")
	public PostDTO update(@PathVariable Long id, @RequestBody PostDTO dto) {
		UserDTO applicant = securityUtils.getUserFromContext();
		if(!dto.getCreatedBy().getId().equals(applicant.getId()) )
			throw new IsNotOwner("Applicant is not owner of this " + Post.class.getSimpleName());
		return super.update(id, dto);
	}

	@RequestMapping(value = "/page/{pageNumber}", method = RequestMethod.GET, produces="application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody String getPostsByPage(@PathVariable int pageNumber) {
		System.out.println("Responding on endpoint request /api/posts/page/" + pageNumber + " GET request");
		Page<PostDTO> page = postService.getPostsByPage(pageNumber);
		Map<String,Object> ret = new HashMap<String,Object>();
		ret.put("totalPages", page.getTotalPages());
		ret.put("totalPosts", page.getTotalElements());
		ret.put("posts", page.getContent());
		ObjectMapper mapper = new ObjectMapper();
		String result = "";
		try {
			result = mapper.writeValueAsString(ret);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value = "/{tagTitle}/{pageNumber}", method = RequestMethod.GET, produces="application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody String getPostsByTag(@PathVariable String tagTitle, @PathVariable int pageNumber) {
		System.out.println("Responding on endpoint request /api/posts/" + tagTitle + "/page/" + pageNumber + " GET request");
		Page<PostDTO> page = postService.getPostsByTag(tagTitle, pageNumber);
		Map<String,Object> ret = new HashMap<String,Object>();
		ret.put("totalPages", page.getTotalPages());
		ret.put("totalPosts", page.getTotalElements());
		ret.put("posts", page.getContent());
		ret.put("tag", tagService.findByTitle(tagTitle));
		ObjectMapper mapper = new ObjectMapper();
		String result = "";
		try {
			result = mapper.writeValueAsString(ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value = "/user/{userId}/{pageNumber}", method = RequestMethod.GET, produces="application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody String getPostsByUser(@PathVariable Long userId, @PathVariable int pageNumber) {
		System.out.println("Responding on endpoint request /api/posts/user/" + userId + "/page/" + pageNumber + " GET request");
		Page<PostDTO> page = postService.getPostsByUser(userId, pageNumber);
		Map<String,Object> ret = new HashMap<String,Object>();
		ret.put("totalPages", page.getTotalPages());
		ret.put("totalPosts", page.getTotalElements());
		ret.put("posts", page.getContent());
		ret.put("user", userService.findByID(userId));
		ObjectMapper mapper = new ObjectMapper();
		String result = "";
		try {
			result = mapper.writeValueAsString(ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value = "/search/{searchTerm}/{pageNumber}", method = RequestMethod.GET, produces="application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody String getPostsBySearchTerm(@PathVariable String searchTerm, @PathVariable int pageNumber) {
		System.out.println("Responding on endpoint request /api/posts/search/" + searchTerm + "/" + pageNumber + " GET request");
		Page<PostDTO> page = postService.getPostsByTerm(searchTerm, pageNumber);
		Map<String,Object> ret = new HashMap<String,Object>();
		ret.put("totalPages", page.getTotalPages());
		ret.put("totalPosts", page.getTotalElements());
		ret.put("posts", page.getContent());
		ObjectMapper mapper = new ObjectMapper();
		String result = "";
		try {
			result = mapper.writeValueAsString(ret);
			System.out.println("jackson:" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "/{postId}/comment", method = RequestMethod.POST, produces="application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody CommentDTO addComment(@RequestBody CommentDTO comment,@PathVariable String postId) {
		System.out.println("Responding on endpoint request /api/posts/" + postId + "/comment POST request");
		PostDTO post = postService.findByID(Long.parseLong(postId));
		if(post == null)
			throw new EntityNotFoundException(Post.class.getSimpleName() + " not found");
		UserDTO owner = securityUtils.getUserFromContext();
		comment.setPostedBy(owner);
		comment.setPostedInPost(post);
		return commentService.create(comment);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "/{postId}/tags", method = RequestMethod.POST, produces="application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public void addTags(@RequestBody List<Long> tagIds,@PathVariable String postId) {
		System.out.println("Responding on endpoint request /api/posts/" + postId + "/tags POST request");
		PostDTO post = postService.findByID(Long.parseLong(postId));
		if(post == null)
			throw new EntityNotFoundException(Post.class.getSimpleName() + " not found");
		UserDTO applicant = securityUtils.getUserFromContext();
		if(post.getCreatedBy().getId().longValue() != applicant.getId().longValue() )
			throw new IsNotOwner("Applicant is not owner of this " + Post.class.getSimpleName());
		postService.addTags(post, tagIds);
	}
	
	/*@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "/{postId}/share", method = RequestMethod.POST, produces="application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody String share(@RequestBody String socialId,@PathVariable String postId) {
		System.out.println("Responding on endpoint request /api/posts/" + postId + "/share POST request");
		PostDTO post = postService.findByID(Long.parseLong(postId));
		if(post == null)
			throw new EntityNotFoundException(Post.class.getSimpleName() + " not found");
		UserDTO applicant = securityUtils.getUserFromContext();
		if(socialId.equals(SocialProvider.facebook.toString())){
			facebookUtils.sharePost(applicant.getFacebookProfile().getAcessToken(),post);
		}
		return "";
	}*/
	
	@ExceptionHandler(IsNotOwner.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public @ResponseBody String handleIsNotOwner(IsNotOwner e) {
		System.out.println(e.getMessage());
		return e.getMessage();
	}
}
