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
public class UserValidator {
	
	public static void signup(User user) {
		
				
		if(user.getPassword() == null || user.getPassword().isEmpty()) {
			throw new ValidateServiceException("The password is required");
		}
		
	}
	

}
