package gr.uoa.di.scan.thesis.service;

import java.util.List;

import gr.uoa.di.scan.thesis.dto.SocialUserDTO;
import gr.uoa.di.scan.thesis.entity.SocialProvider;
import gr.uoa.di.scan.thesis.entity.SocialUser;

public interface SocialUserService extends GenericService<SocialUser, SocialUserDTO, Long>{
	public List<String> findBySocialProvider(SocialProvider socialProvider);
}
