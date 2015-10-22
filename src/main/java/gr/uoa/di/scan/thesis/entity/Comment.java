package gr.uoa.di.scan.thesis.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="Comments")
public class Comment {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(nullable=false)
	private String body;
	
	@Column(insertable=true,updatable=false,nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;
	
	@ManyToOne
	@JoinColumn(name="userId",insertable=true,updatable=false,nullable=false)
	private User postedBy;
	
	@ManyToOne
	@JoinColumn(name="postId",insertable=true,updatable=false,nullable=true)
	private Post postedInPost;
	
	@ManyToOne
	@JoinColumn(name="commentId",insertable=true,updatable=false,nullable=true)
	private Comment postedInComment;
	
	@OneToMany(mappedBy="postedInComment",cascade=CascadeType.ALL)
	private Set<Comment> comments = new HashSet<Comment>();

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

	public User getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(User postedBy) {
		this.postedBy = postedBy;
	}

	public Post getPostedInPost() {
		return postedInPost;
	}

	public void setPostedInPost(Post postedInPost) {
		this.postedInPost = postedInPost;
	}

	public Comment getPostedInComment() {
		return postedInComment;
	}

	public void setPostedInComment(Comment postedInComment) {
		this.postedInComment = postedInComment;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
	
	@PrePersist
	protected void dateCreated() {
		dateCreated = new Date();
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
		Comment other = (Comment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	
	
}
