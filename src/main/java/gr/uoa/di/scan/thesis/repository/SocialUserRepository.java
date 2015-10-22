package gr.uoa.di.scan.thesis.repository;

import java.util.List;

import gr.uoa.di.scan.thesis.entity.SocialProvider;
import gr.uoa.di.scan.thesis.entity.SocialUser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialUserRepository extends JpaRepository<SocialUser, Long> {

	public List<SocialUser> findBySocialProvider(SocialProvider socialProvider);
	
	public SocialUser findBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId);
}
