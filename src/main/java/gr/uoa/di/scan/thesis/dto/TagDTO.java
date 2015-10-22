package gr.uoa.di.scan.thesis.dto;

import gr.uoa.di.scan.thesis.entity.Identifiable;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.*;

@XmlRootElement
public class TagDTO implements Identifiable<Long>{

	private Long id;

	private String title;
	
	private String description;
	
	private Set<PostDTO> posts = new HashSet<PostDTO>();

	private int postsCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<PostDTO> getPosts() {
		return posts;
	}

	public void setPosts(Set<PostDTO> posts) {
		this.posts = posts;
	}

	public int getPostsCount() {
		return postsCount;
	}

	public void setPostsCount(int postsCount) {
		this.postsCount = postsCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TagDTO other = (TagDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}

