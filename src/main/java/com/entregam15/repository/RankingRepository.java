/**
 * 
 */
package com.entregam15.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.entregam15.entity.TotalGames;
import com.entregam15.entity.User;

/**
 * @author stefano
 *
 */

@Repository
public interface RankingRepository extends MongoRepository<TotalGames, ObjectId>{

	public Optional<TotalGames> findByUser(User user);

	
}