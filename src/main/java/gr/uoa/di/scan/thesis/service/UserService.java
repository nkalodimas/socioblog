package gr.uoa.di.scan.thesis.service;

import gr.uoa.di.scan.thesis.dto.UserDTO;
import gr.uoa.di.scan.thesis.entity.User;

public interface UserService extends GenericService<User, UserDTO, Long>{
	
	public UserDTO getFullUser(Long id);
	
	public UserDTO findByEmail(String email);
	
	public UserDTO logIn(String accessToken, String socialId, StringBuilder key);

	public UserDTO signUp(String accessToken, String socialId, StringBuilder key);
}
