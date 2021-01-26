/**
 * 
 */
package com.entregam15.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entregam15.entity.Game;
import com.entregam15.entity.TotalGames;
import com.entregam15.entity.User;

/**
 * @author stefano
 *
 */

@Repository
public interface RankingRepository extends JpaRepository<TotalGames, Long>{

	public Optional<TotalGames> findByUser(User user);

	
}