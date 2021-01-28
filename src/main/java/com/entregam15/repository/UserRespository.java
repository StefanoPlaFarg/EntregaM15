/**
 * 
 */
package com.entregam15.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.entregam15.entity.User;

/**
 * @author stefano
 *
 */

@Repository
public interface UserRespository extends MongoRepository<User, ObjectId>{

	public Optional<User> findByUserName(String userName);
	public List<User> findAllByUserName(String userName);
	
}
