/**
 * 
 */
package com.entregam15.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entregam15.dto.UserDTO;
import com.entregam15.entity.*;
import com.entregam15.exception.*;
import com.entregam15.repository.*;
import com.entregam15.validator.UserValidator;

import lombok.extern.slf4j.Slf4j;
/**
 * @author stefano
 *
 */



@Slf4j
@Service
public class UserService {

	@Autowired
	private UserRespository userRepository;
	
	@Transactional
	public User createUser(User user) {
		try {

			UserValidator.validateUser(user);
			
			User existUser = userRepository.findByUsername(user.getUserName()).orElse(null);
			
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
	
}
