package gr.uoa.di.scan.thesis.service;

import java.util.ArrayList;
import java.util.List;

import gr.uoa.di.scan.thesis.dto.SocialUserDTO;
import gr.uoa.di.scan.thesis.entity.SocialProvider;
import gr.uoa.di.scan.thesis.entity.SocialUser;
import gr.uoa.di.scan.thesis.repository.SocialUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service("socialUserService")
public class SocialUserServiceImpl extends GenericServiceBase<SocialUser, SocialUserDTO, Long> implements SocialUserService {

	@Autowired
	SocialUserRepository socialUserRepository;
	@Override
	JpaRepository<SocialUser, Long> getRepository() {
		return socialUserRepository;
	}

	@Override
	Class<SocialUser> getTypeofEntity() {
		return SocialUser.class;
	}

	@Override
	Class<SocialUserDTO> getTypeofDTO() {
		return SocialUserDTO.class;
	}

	@Override
	public List<String> findBySocialProvider(
			SocialProvider socialProvider) {
		List<SocialUser> socialUsers = socialUserRepository.findBySocialProvider(socialProvider);
		List<String> accessTokens = new ArrayList<String>();
		for (SocialUser socialUser : socialUsers) {
			accessTokens.add(socialUser.getAccessToken());
		}
		return accessTokens;
	}

}
