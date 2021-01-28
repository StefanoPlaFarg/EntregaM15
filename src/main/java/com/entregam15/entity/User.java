/**
 * 
 */
package com.entregam15.entity;


import java.time.LocalDateTime;
import java.util.List;



import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author stefano
 *
 */



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Document(collection = "USERS")
public class User {
	

	
	@Id
	private ObjectId id;

	private String userName;

	private String password;

	private LocalDateTime registrationDate;

	private List<Game> listGames;

	private TotalGames totalGames;

}
