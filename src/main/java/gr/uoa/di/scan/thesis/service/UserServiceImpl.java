package gr.uoa.di.scan.thesis.service;

import gr.uoa.di.scan.thesis.dto.UserDTO;
import gr.uoa.di.scan.thesis.entity.SocialProvider;
import gr.uoa.di.scan.thesis.entity.SocialUser;
import gr.uoa.di.scan.thesis.entity.User;
import gr.uoa.di.scan.thesis.exception.FailedLoginException;
import gr.uoa.di.scan.thesis.exception.FailedSignUpException;
import gr.uoa.di.scan.thesis.repository.SocialUserRepository;
import gr.uoa.di.scan.thesis.repository.UserRepository;
import gr.uoa.di.scan.thesis.social.FacebookUtils;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.token.DefaultToken;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("userService")
public class UserServiceImpl extends GenericServiceBase<User, UserDTO, Long> implements UserService, UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SocialUserRepository socialUserRepository;
	
	@Autowired
	private FacebookUtils fbUtils;
	
	@Autowired
	private TokenService tokenService;

	@Override
	JpaRepository<User, Long> getRepository() {
		
		return userRepository;
	}
	
	@Override
	Class<User> getTypeofEntity() {
		
		return User.class;
	}

	@Override
	Class<UserDTO> getTypeofDTO() {
		
		return UserDTO.class;
	}

	@Transactional
	public UserDTO findByEmail(String email) {
		
		User user = userRepository.findByEmail(email);
		
		if (user == null)
			return null;
		
		return mapper.map(user, UserDTO.class);
	}
	
	@Transactional
	public UserDTO getFullUser(Long id) {
		
		User user = userRepository.findOne(id);
		
		if (user == null)
			return null;
		
		return mapper.map(user, UserDTO.class, "fullUser");
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		UserDetails user = new org.springframework.security.core.userdetails.User(username, "realPass", 
				true, true, true, true, authorities );
		return user;
	}

	@Transactional
	public UserDTO logIn(String accessToken, String socialId, StringBuilder key) {
		// Create Facebook connection from access token and get Facebook User
		// Search db for SocialUser with same socialId and return his User
		// Also refresh accessToken in db.
		if(socialId.equals(SocialProvider.facebook.toString())) {
			org.springframework.social.facebook.api.User fbUser = fbUtils.getFbUser(accessToken);
			if(fbUser == null)
				throw new FailedLoginException("Facebook user's data could not be retrieved by using this access token");
			SocialUser socialUser = socialUserRepository.findBySocialProviderAndSocialId(SocialProvider.facebook, fbUser.getId());
			if(socialUser == null)
				throw new FailedLoginException("User does not exist");
			User user = socialUser.getUser();
			DefaultToken token = (DefaultToken) tokenService.allocateToken(user.getId().toString());
			key.append(token.getKey());
			//fb get long token with short
			//String longLivedToken = fbUtils.getLongLivedToken(accessToken);
			socialUser.setAccessToken(accessToken);
			//socialUser.setLongLivedAccessToken(longLivedToken);
			socialUserRepository.saveAndFlush(socialUser);
			return mapper.map(user, UserDTO.class);
		}
		throw new FailedLoginException("Login is provided only via facebook");
	}

	@Transactional
	public UserDTO signUp(String accessToken, String socialId, StringBuilder key) {
		// Check if exists same SocialUser with same socialid
		// Create Facebook connection from access token and get Facebook User
		// Insert in db User+SocialUser filled with data fromFBUser
		if(socialId.equals(SocialProvider.facebook.toString())){
			org.springframework.social.facebook.api.User fbUser = fbUtils.getFbUser(accessToken);
			if(fbUser == null)
				throw new FailedSignUpException("Facebook user's data could not be retrieved by using this access token");
			SocialUser socialUser = socialUserRepository.findBySocialProviderAndSocialId(SocialProvider.facebook, fbUser.getId());
			if(socialUser != null)
				throw new FailedSignUpException("User already exists");
			// create user
			User user = new User();
			user.setEmail(fbUser.getEmail());
			user.setUsername(fbUser.getName());
			user.setName(fbUser.getFirstName());
			user.setSurname(fbUser.getLastName());
			System.out.println("FB user:" + user.toString() );
			user = userRepository.saveAndFlush(user);
			// create social user for user
			socialUser = new SocialUser();
			//String longLivedToken = fbUtils.getLongLivedToken(accessToken);
			socialUser.setAccessToken(accessToken);
			//socialUser.setLongLivedAccessToken(longLivedToken);
			socialUser.setLink(fbUser.getLink());
			socialUser.setSocialProvider(SocialProvider.facebook);
			socialUser.setSocialId(fbUser.getId());
			socialUser.setUser(user);
			socialUserRepository.saveAndFlush(socialUser);
			//login user
			DefaultToken token = (DefaultToken) tokenService.allocateToken(user.getId().toString());
			key.append(token.getKey());
			return mapper.map(user, UserDTO.class);
		}
		throw new FailedSignUpException("Sign Up is provided only via Facebook");
	}

}
