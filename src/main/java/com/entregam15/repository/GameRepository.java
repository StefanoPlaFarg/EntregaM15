/**
 * 
 */
package com.entregam15.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.entregam15.entity.Game;
import com.entregam15.entity.User;

/**
 * @author stefano
 *
 */

@Repository
public interface GameRepository extends JpaRepository<Game, Long>{

	public Optional<Game> findByUser(User user);
	public Page<Game> findAllByUser(User user, Pageable page);
	public Optional <List<Game>> findAllByUser (User user);
	
	
}
