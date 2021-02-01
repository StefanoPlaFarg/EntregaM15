/**
 * 
 */
package com.entregam15.repository;


import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.entregam15.entity.Game;
import com.entregam15.entity.User;

/**
 * @author stefano
 *
 */

@Repository
public interface GameRepository extends MongoRepository<Game, ObjectId>{

	public Optional<Game> findByUser(User user);
	public Page<Game> findAllByUser(User user, Pageable page);
	
	
	public Optional <List<Game>> findAllByUser (User user);
	
	//@Query ( "{'user.id': ?0}")
	//public Optional <List<Game>> findAllByUserId (ObjectId idUser);
	
	
}
