/**
 * 
 */
package com.entregam15.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.entregam15.entity.Game;

/**
 * @author stefano
 *
 */

@Repository
public interface GameRepository extends JpaRepository<Game, Long>{


	
}
