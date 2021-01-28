/**
 * 
 */
package com.entregam15.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entregam15.dto.LoginRequestDTO;
import com.entregam15.dto.LoginResponseDTO;
import com.entregam15.dto.UserDTO;
import com.entregam15.entity.*;
import com.entregam15.exception.*;
import com.entregam15.mapper.UserMapper;
import com.entregam15.repository.*;
import com.entregam15.security.UserPrincipal;
import com.entregam15.validator.UserValidator;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
/**
 * @author stefano
 *
 */



@Slf4j
@Service
public class UserService {

	@Autowired
	UserMapper userMapper;
	
	@Autowired
	private UserRespository userRepository;
	
	@Value ("${jwt.password}")
	private String jwtPassword;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//Create and save a new user
	public User createUser(User user) {
		try {

			// Validate if the new user has the password
			UserValidator.validateUser(user);

			List<User> listExistingUsers = new ArrayList<User>();

			listExistingUsers = userRepository.findAllByUserName(user.getUserName());

			//Check if the user exists and is not ANONIM
			if (listExistingUsers.size() != 0) {

				for (User existingUser : listExistingUsers) {

					if (user.getUserName().equals(existingUser.getUserName()) && !user.getUserName().equals("ANONIM")) {
						

						throw new ValidateServiceException("This user already exists");

					}

				}

			}

			//Encode the password
			String encoder = passwordEncoder.encode(user.getPassword());
			user.setPassword(encoder);

			return userRepository.save(user);

		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	//Get a User by his id
	public User findById(ObjectId id) {

		try {

			// Check if the user exists and matches with the loggin user
			User user = UserPrincipal.getCurrentUser();

			if (user == null)
				throw new NoDataFoundException("This user doesn't exist");

			if (user.getId() != id) // The id delivered doesnt match with id of the loggin user
				throw new ValidateServiceException("The user is anauthorized to handle games of other users");

			return user;

		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}

	}
	
	
	//Get all Users who have Games
	public List<User> findAllUsersWithGames() {

		try {

			List<User> existingUsersWithGames = userRepository.findAll().stream()
					.filter(User -> User.getListGames().size() != 0).collect(Collectors.toList());

			if (existingUsersWithGames == null)
				throw new NoDataFoundException("This user doesn't exist");

			return existingUsersWithGames;

		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}

	}
	
	
	
	
	
	//Update the username of a user
	public User updateUser(User user) {
		try {

			User userToUpdate = UserPrincipal.getCurrentUser();

			if (userToUpdate == null)
				throw new NoDataFoundException("This user doesn't exist");

			userToUpdate.setUserName(user.getUserName());

			return userRepository.save(userToUpdate);

		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	//Authentificate a user
	public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
		try {
            
			
			//Find the user that mathces username and password with the loginrequestDTO
			List<User> listExistingUsers = new ArrayList<User>();
			User user = null;

			listExistingUsers = userRepository.findAllByUserName(loginRequestDTO.getUsername());

			if (listExistingUsers.size() == 0) {

				throw new ValidateServiceException("The user is incorrect");
			} else {

				boolean matchPassword = false;

				for (User existingUser : listExistingUsers) {

					if (passwordEncoder.matches(loginRequestDTO.getPassword(), existingUser.getPassword())) {

						matchPassword = true;
						user = existingUser;

					}

					if (matchPassword == true)
						break;
				}

				if (matchPassword == false)
					throw new ValidateServiceException("The password is incorrect");

			}

			// Create the token
			String jwtToken = createToken(user);

			// Return logged user and the token
			return new LoginResponseDTO(userMapper.fromEntity(user), jwtToken);

		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}

	}
	
	//Create a token through his id
	public String createToken (User user) {
		
		Date now = new Date();
		Date expirationDate = new Date (now.getTime() + (1000*60*60)); //Expiration time of token:  after one hour (1000 ms, 60 s/min, 60 min/h)
		
		String subject = user.getId().toString();
		
		System.out.println("subject: "+ subject);
		
		
		return Jwts.builder()				
				.setSubject(subject)
				.setIssuedAt(now)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, jwtPassword)
				.compact();
	}
	
	
	//Validate the token used by a user
	public boolean validateToken (String token) {
		
		try {
			Jwts.parser().setSigningKey(jwtPassword).parseClaimsJws(token);
			return true;
		}catch (UnsupportedJwtException e) {
			log.error("JWT in a particular format/configuration that does not match the format expected");
		}catch (MalformedJwtException e) {
			log.error(" JWT was not correctly constructed and should be rejected");
		}catch (SignatureException e) {
			log.error("Signature or verifying an existing signature of a JWT failed");
		}catch (ExpiredJwtException e) {
			log.error("JWT was accepted after it expired and must be rejected");
		}
		return false;
		
	}
	
	
	//Get the id of the user by his token
	public String getUserIdFromToken(String jwt) {
		try {
			return Jwts.parser().setSigningKey(jwtPassword).parseClaimsJws(jwt).getBody().getSubject();	
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ValidateServiceException("Invalid Token");
		}
		
	}
	
}
