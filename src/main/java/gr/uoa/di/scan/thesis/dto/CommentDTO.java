package gr.uoa.di.scan.thesis.dto;

import gr.uoa.di.scan.thesis.entity.Identifiable;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.*;

@XmlRootElement
public class CommentDTO implements Identifiable<Long>{

	private Long id;

	private String body;
	
	private Date dateCreated;
	
	private UserDTO postedBy;
	
	private PostDTO postedInPost;
	
	private CommentDTO postedInComment;
	
	private Set<CommentDTO> comments = new HashSet<CommentDTO>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public UserDTO getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(UserDTO postedBy) {
		this.postedBy = postedBy;
	}

	public PostDTO getPostedInPost() {
		return postedInPost;
	}

	public void setPostedInPost(PostDTO postedInPost) {
		this.postedInPost = postedInPost;
	}

	public CommentDTO getPostedInComment() {
		return postedInComment;
	}

	public void setPostedInComment(CommentDTO postedInComment) {
		this.postedInComment = postedInComment;
	}

	public Set<CommentDTO> getComments() {
		return comments;
	}

	public void setComments(Set<CommentDTO> comments) {
		this.comments = comments;
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
		CommentDTO other = (CommentDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
