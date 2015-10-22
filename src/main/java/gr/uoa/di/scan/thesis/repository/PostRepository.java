package gr.uoa.di.scan.thesis.repository;

import java.util.Collection;

import gr.uoa.di.scan.thesis.entity.Post;
import gr.uoa.di.scan.thesis.entity.Tag;
import gr.uoa.di.scan.thesis.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long>{

	public Page<Post> findByTitleContaining(String searchTerm, Pageable request);
	
	@Query("select post from Post post where post.title like %?1% or post.body like %?1%")
	public Page<Post> findSearchedPosts(String searchTerm,Pageable request);
	
	@Query("select post from Post post inner join post.tags tag where tag in (:tags)")
	public Page<Post> findPostsByTag(@Param("tags") Collection<Tag> tags, Pageable request);
	
	public Page<Post> findByCreatedBy(User user, Pageable request);
}
