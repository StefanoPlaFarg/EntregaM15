/**
 * 
 */
package com.entregam15.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.entregam15.dto.SignupRequestDTO;
import com.entregam15.dto.UserDTO;
import com.entregam15.dto.UserRequestDTO;
import com.entregam15.entity.User;

/**
 * @author stefano
 *
 */
@Component
public class UserMapper extends AbstractMapper<User, UserDTO>{
   
	private LocalDateTime registrationDate;
	private DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern ("dd/MM/yyyy hh:mm:ss");
	
	
	@Override
	public UserDTO fromEntity(User entity) {
		if (entity==null) return null;
		return UserDTO.builder()
				.id(entity.getId())
				.username(entity.getUserName())
				.build();
	}

	
	public User fromDTO(UserDTO dto) {
		if (dto==null) return null;
		return User.builder()
				.id(dto.getId())
				.userName(dto.getUsername())
				.build();
	}

	public User signUp(SignupRequestDTO dto) {
		
		String userName;
		String now  = LocalDateTime.now().format(dateTimeFormat);
		registrationDate = LocalDateTime.parse(now, dateTimeFormat);
		
		if (dto.getUsername().isEmpty()) {
			
			userName = "ANONIM";
			
		}else {
			
			userName = dto.getUsername();
			
		}
		
		
		return User.builder()
				.userName(userName )
				.password(dto.getPassword())
				.registrationDate(registrationDate)
				.build();
	}
	
	
	
}
