/**
 * 
 */
package com.entregam15.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.entregam15.entity.User;

/**
 * @author stefano
 *
 */

@Repository
public interface UserRespository extends JpaRepository<User, Long>{

	public Optional<User> findByUserName(String userName);
	
}
