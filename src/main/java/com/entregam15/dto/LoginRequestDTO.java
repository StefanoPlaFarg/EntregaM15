/**
 * 
 */
package com.entregam15.dto;

/**
 * @author stefano
 *
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {

	private String username;
	private String password;
	
}
