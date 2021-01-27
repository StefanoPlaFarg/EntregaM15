/**
 * 
 */
package com.entregam15.validator;

import com.entregam15.entity.User;
import com.entregam15.exception.ValidateServiceException;

/**
 * @author stefano
 *
 */

//Validates if a user provides the password when he signs up
public class UserValidator {
	
	public static void validateUser(User user) {
		
				
		if(user.getPassword() == null || user.getPassword().isEmpty()) {
			throw new ValidateServiceException("The password is required");
		}
		
	}
	

}
