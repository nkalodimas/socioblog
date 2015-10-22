package gr.uoa.di.scan.thesis.service;

import java.util.ArrayList;
import java.util.List;

import gr.uoa.di.scan.thesis.dto.PostDTO;
import gr.uoa.di.scan.thesis.entity.Post;
import gr.uoa.di.scan.thesis.entity.Tag;
import gr.uoa.di.scan.thesis.entity.User;
import gr.uoa.di.scan.thesis.exception.EntityNotFoundException;
import gr.uoa.di.scan.thesis.repository.PostRepository;
import gr.uoa.di.scan.thesis.repository.TagRepository;
import gr.uoa.di.scan.thesis.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("postService")
public class PostServiceImpl extends GenericServiceBase<Post, PostDTO, Long> implements PostService {

	private static final int PAGE_SIZE = 3;
	
	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TagRepository tagRepository;
	@Override
	JpaRepository<Post, Long> getRepository() {
		return postRepository;
	}

	@Override
	Class<Post> getTypeofEntity() {
		return Post.class;
	}

	@Override
	Class<PostDTO> getTypeofDTO() {
		return PostDTO.class;
	}

	@Override
	@Transactional(rollbackFor = EntityNotFoundException.class)
	public PostDTO create(PostDTO dto) throws EntityNotFoundException {
		if (userRepository.exists(dto.getCreatedBy().getId()))
			return super.create(dto);
			
		throw new EntityNotFoundException(getTypeofEntity().getSimpleName() + " not found");
	}

	@Transactional
	public Page<PostDTO> getPostsByPage(int pageNumber) {
		
		PageRequest request = new PageRequest(pageNumber-1, PAGE_SIZE, Sort.Direction.DESC, "created");//createdTime
		List<PostDTO> list = new ArrayList<PostDTO>();
		Page<Post> page = getRepository().findAll(request);
		for (Post post : page){
			list.add(mapper.map(post, getTypeofDTO()));
		}
		
		return new PageImpl<PostDTO>(list, request, page.getTotalElements());
	}

	@Transactional
	public Page<PostDTO> getPostsByTerm(String searchTerm, int pageNumber) {
		PageRequest request = new PageRequest(pageNumber-1, PAGE_SIZE, Sort.Direction.DESC, "created");//createdTime
		List<PostDTO> list = new ArrayList<PostDTO>();
		Page<Post> page = postRepository.findByTitleContaining(searchTerm, request);
		for (Post post : page){
			list.add(mapper.map(post, getTypeofDTO()));
		}
		
		return new PageImpl<PostDTO>(list, request, page.getTotalElements());

	}
	
	@Transactional
	public Page<PostDTO> getPostsByTag(String tagTitle, int pageNumber) {
		PageRequest request = new PageRequest(pageNumber-1, PAGE_SIZE, Sort.Direction.DESC, "created");//createdTime
		List<PostDTO> list = new ArrayList<PostDTO>();
		List<Tag> tags = new ArrayList<Tag>();
		Tag tag = tagRepository.findByTitle(tagTitle);
		if(tag == null)
			throw new EntityNotFoundException(Tag.class.getSimpleName() + " not found");
		tags.add(tag);
		Page<Post> page = postRepository.findPostsByTag(tags, request);
		for (Post post : page){
			list.add(mapper.map(post, getTypeofDTO()));
		}
		
		return new PageImpl<PostDTO>(list, request, page.getTotalElements());

	}

	@Transactional
	public Page<PostDTO> getPostsByUser(Long userId, int pageNumber) {
		PageRequest request = new PageRequest(pageNumber-1, PAGE_SIZE, Sort.Direction.DESC, "created");
		User user = userRepository.findOne(userId);
		if(user == null)
			throw new EntityNotFoundException(User.class.getSimpleName() + " not found");
		Page<Post> page = postRepository.findByCreatedBy(user, request);
		List<PostDTO> list = new ArrayList<PostDTO>();
		for (Post post : page){
			list.add(mapper.map(post, getTypeofDTO()));
		}
		
		return new PageImpl<PostDTO>(list, request, page.getTotalElements());

	}

	@Transactional
	public PostDTO addTags(PostDTO dto, List<Long> tagIds) {
		if (postRepository.exists(dto.getId())) {
			Post post = postRepository.findOne(dto.getId());
			post.getTags().clear();
			for (Long tagId : tagIds) {
				if(tagRepository.exists(tagId) ){
					post.getTags().add(tagRepository.findOne(tagId));
				}
				else
					throw new EntityNotFoundException(Tag.class.getSimpleName() + " not found");
			}
			return mapper.map(postRepository.save(post),getTypeofDTO());
		}
		throw new EntityNotFoundException(getTypeofEntity().getSimpleName() + " not found");
	}
}