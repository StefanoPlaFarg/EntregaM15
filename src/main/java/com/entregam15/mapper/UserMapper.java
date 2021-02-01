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

//Convertidor de Entitites User < ---- > a DTO
@Component
public class UserMapper extends AbstractMapper<User, UserDTO>{
   
	private LocalDateTime registrationDate;
	//private static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern ("dd-MM-yyyy HH:mm:ss");
	
	
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

		String username;

		// registrationDate = LocalDateTime.now();
		// String now = registrationDate.format(dateTimeFormat);
		// registrationDate = LocalDateTime.parse(now,dateTimeFormat);

		registrationDate = LocalDateTime.now();

		if (dto.getUsername().isEmpty()) { //if the user doesnt provid a username, create it with "ANONIM"

			username = "ANONIM";

		} else {

			username = dto.getUsername();

		}

		return User.builder().userName(username).password(dto.getPassword()).registrationDate(registrationDate).build();
	}
	
	
	
}
