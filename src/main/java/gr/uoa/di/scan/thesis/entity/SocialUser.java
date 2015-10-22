package gr.uoa.di.scan.thesis.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table( name="SocialUsers")
public class SocialUser {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private SocialProvider socialProvider;
	
	@Column(nullable=true)
	private String accessToken;
	
	@Column(nullable=true)
	private String longLivedAccessToken;
	
	@Column(nullable=true)
	private String socialId;
	
	@Column(nullable=true)
	private String link;
	
	@ManyToOne
	@JoinColumn(name="userId",insertable=true,updatable=false,nullable=false)
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SocialProvider getSocialProvider() {
		return socialProvider;
	}

	public void setSocialProvider(SocialProvider socialProvider) {
		this.socialProvider = socialProvider;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getLongLivedAccessToken() {
		return longLivedAccessToken;
	}

	public void setLongLivedAccessToken(String longLivedAccessToken) {
		this.longLivedAccessToken = longLivedAccessToken;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
		SocialUser other = (SocialUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
