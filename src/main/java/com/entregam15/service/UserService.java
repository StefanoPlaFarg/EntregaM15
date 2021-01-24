/**
 * 
 */
package com.entregam15.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
	@Transactional
	public User createUser(User user) {
		try {

			UserValidator.validateUser(user);
			
			User existUser = userRepository.findByUserName(user.getUserName()).orElse(null);
			
			if(existUser != null) throw new ValidateServiceException("This user alreay exists");
			
			return userRepository.save(user);
			
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	
	public User findById(Long id) {

		try {

			User existUser = userRepository.findById(id).orElse(null);

			if (existUser == null)
				throw new NoDataFoundException("This user doesn't exist");

			return existUser;
			
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}

	}
	
	@Transactional
	public User updateUser(User user) {
		try {

			
			User userToUpdate = userRepository.findById(user.getId()).orElse(null);
			
			if(userToUpdate == null) throw new NoDataFoundException("This user doesn't exist");
			
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
	
	
	public LoginResponseDTO login (LoginRequestDTO loginRequestDTO ) {
	try {
       		
		User user= userRepository.findByUserName(loginRequestDTO.getUserName())
				.orElseThrow(() -> new ValidateServiceException ("The user is incorrect") );
			
		if(!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword()))
			throw new ValidateServiceException("Usuario o password incorrectos");
		//if (!user.getPassword().equals(loginRequestDTO.getPassword())) throw new ValidateServiceException ("The password is incorrect") ;
			
		String jwtToken = createToken(user);
		
		return new LoginResponseDTO(userMapper.fromEntity(user),jwtToken);
			
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
		
		
	}
	
	public String createToken (User user) {
		
		Date now = new Date();
		Date expirationDate = new Date (now.getTime() + (1000*60*60)); //Expiration date after one hour (1000 ms, 60 s/min, 60 min/h)
		return Jwts.builder()
				.setSubject(user.getUserName())
				.setIssuedAt(now)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, jwtPassword)
				.compact();
	}
	
	
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
	
	public String getUsernameFromToken(String jwt) {
		try {
			return Jwts.parser().setSigningKey(jwtPassword).parseClaimsJws(jwt).getBody().getSubject();	
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ValidateServiceException("Invalid Token");
		}
		
	}
	
	
}
