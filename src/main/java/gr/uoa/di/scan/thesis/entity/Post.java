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
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="Posts")
public class Post {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(nullable=false,unique=true,length=50)
	private String title;

	@Column(nullable=false, length=10000)
	@Lob
	private String body;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(insertable=true, updatable=false, nullable=false)
	private Date created;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(insertable=true, updatable=true, nullable=true)
	private Date updated;

	@ManyToOne
	@JoinColumn(name="userId",insertable=true,updatable=false,nullable=false)
	private User createdBy;
	
	@OneToMany(mappedBy="postedInPost",cascade=CascadeType.ALL)
	@OrderBy("id ASC")
	private Set<Comment> comments = new HashSet<Comment>();
	
	@ManyToMany()
	@JoinTable(name="PostHasTag",
			joinColumns=@JoinColumn(name="postId",referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="tagId",referencedColumnName="id"))
	private Set<Tag> tags = new HashSet<Tag>();

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

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@PrePersist
	protected void onCreate() {
		this.created = new Date();
	}
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updated = new Date();
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
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
		Post other = (Post) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", title=" + title + ", body=" + body
				+ ", createdBy=" + createdBy.getId() + "]";
	}
	
}
