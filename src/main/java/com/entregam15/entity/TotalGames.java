/**
 * 
 */
package com.entregam15.entity;




import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor


@Document(collection = "RANKINGS")
public class TotalGames {

	@Id 
	private ObjectId id;

	private User user;

	private double averageSuccess;

}
