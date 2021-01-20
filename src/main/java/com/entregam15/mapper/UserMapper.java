/**
 * 
 */
package com.entregam15.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.entregam15.dto.SignupRequestDTO;
import com.entregam15.dto.UserDTO;
import com.entregam15.entity.User;

/**
 * @author stefano
 *
 */
public class UserMapper extends AbstractMapper<User, UserDTO>{
   
	private LocalDateTime regDate;
	
	
	@Override
	public UserDTO fromEntity(User entity) {
		return UserDTO.builder()
				.id(entity.getId())
				.username(entity.getUserName())
				.build();
	}

	
	public User fromDTO(UserDTO dto) {
		return User.builder()				
				.userName(dto.getUsername())
				.build();
	}

	public User signUp(SignupRequestDTO dto) {
		
		String userName;
		
		if (dto.getUsername().isEmpty()) {
			
			userName = "ANONIM";
			
		}else {
			
			userName = dto.getUsername();
			
		}
		
		
		return User.builder()
				.userName(userName )
				.password(dto.getPassword())
				.registrationDate(regDate)
				.build();
	}
	
	
	
}
