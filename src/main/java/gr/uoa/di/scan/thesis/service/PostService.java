package gr.uoa.di.scan.thesis.service;

import java.util.List;

import org.springframework.data.domain.Page;

import gr.uoa.di.scan.thesis.dto.PostDTO;
import gr.uoa.di.scan.thesis.entity.Post;

public interface PostService extends GenericService<Post, PostDTO, Long>{
	
	public PostDTO addTags(PostDTO dto, List<Long> tagIds);
	
	public Page<PostDTO> getPostsByPage(int pageNumber);
	
	public Page<PostDTO> getPostsByTerm(String searchTerm, int pageNumber);
	
	public Page<PostDTO> getPostsByTag(String tagTitle, int pageNumber);
	
	public Page<PostDTO> getPostsByUser(Long userId, int pageNumber);
}
