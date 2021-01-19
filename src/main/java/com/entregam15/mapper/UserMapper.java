/**
 * 
 */
package com.entregam15.mapper;

import com.entregam15.dto.SignupRequestDTO;
import com.entregam15.dto.UserDTO;
import com.entregam15.entity.User;

/**
 * @author stefano
 *
 */
public class UserMapper extends AbstractMapper<User, UserDTO>{

	@Override
	public UserDTO fromEntity(User entity) {
		return UserDTO.builder()
				.id(entity.getId())
				.username(entity.getUserName())
				.build();
	}

	
	public User fromDTO(UserDTO dto) {
		return User.builder()
				.id(dto.getId())
				.userName(dto.getUsername())
				.build();
	}

	public User signup(SignupRequestDTO dto) {
		return User.builder()
				.userName(dto.getUsername())
				.password(dto.getPassword())
				.build();
	}
	
	
	
}
