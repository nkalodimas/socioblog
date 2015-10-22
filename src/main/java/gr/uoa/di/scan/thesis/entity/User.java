package gr.uoa.di.scan.thesis.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table( name="Users")
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(nullable=true)
	private String name;
	
	@Column(nullable=true)
	private String surname;
	
	@Column(nullable=false,unique=true)
	private String email;
	
	@Column(nullable=false,unique=false)
	private String username;
	
	@Column(nullable=true)
	private String password;
	
	@Column(nullable=true)
	private String description;
	
	@OneToMany(mappedBy="createdBy",cascade=CascadeType.ALL)
	private Set<Post> posts = new HashSet<Post>();
	
	@OneToMany(mappedBy="postedBy",cascade=CascadeType.ALL)
	private Set<Comment> comments = new HashSet<Comment>();
	
	@OneToMany(mappedBy="user",cascade=CascadeType.ALL)
	private Set<SocialUser> socialProfiles = new HashSet<SocialUser>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Post> getPosts() {
		return posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Set<SocialUser> getSocialProfiles() {
		return socialProfiles;
	}

	public void setSocialProfiles(Set<SocialUser> socialProfiles) {
		this.socialProfiles = socialProfiles;
	}

	public int getPostsCount() {
		return this.getPosts().size();
	}

	public void setPostsCount(int postsCount) {
	}

	public int getCommentsCount() {
		return this.getComments().size();
	}

	public void setCommentsCount(int commentsCount) {
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
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", surname=" + surname
				+ ", email=" + email + ", username=" + username + ", password="
				+ password + ", description=" + description + "]";
	}
	
	

}
