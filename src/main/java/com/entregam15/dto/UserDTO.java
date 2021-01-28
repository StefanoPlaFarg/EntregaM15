/**
 * 
 */
package com.entregam15.dto;

import org.bson.types.ObjectId;

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
public class UserDTO {
	private String id;
	private String username;
}