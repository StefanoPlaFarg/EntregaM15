/**
 * 
 */
package com.entregam15.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entregam15.entity.*;
import com.entregam15.repository.*;

import lombok.extern.slf4j.Slf4j;
/**
 * @author stefano
 *
 */



@Slf4j
@Service
public class GameService {
	
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private RankingRepository rankingRepository;
	
	
	//Games by User
	
	@Transactional
	public Game playAndSaveGame(User user) {
		try {
			
			//TODO: play a Game and save it into the repository
			
				
			return null;
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	
	
	public void deleteAllGamesByUser(User user) {
		try {
			
			//TODO: find Games by User and delete all them from repository
			Game game = gameRepository.findById(id)
					.orElseThrow(() -> new NoDataFoundException("This game doesn't exist"));
			
			gameRepository.delete(game);
			
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	
	public List<Game> findAllGamesbyUser(User user, Pageable page){
		try {
			
			//TODO: find Games by User and retrieve all them from the repository
			return gameRepository.findAll(page).toList();
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	
	
	//Rankings by User
	
	
	public List<TotalGames> findAllRankings(User user, Pageable page){
		try {
			//TODO: find TotalGames and retrieve all them from the repository
			return totalGamesRepository.findAll(page).toList();
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	
	public double findAverageRanking(){
		try {
			
			//TODO: get Average Ranking TotalGames and retrieve it from the repository
			return totalGamesRepository.findAll(page).toList();
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	public User findLoser(){
		try {
			
			//TODO: get Loser and retrieve it from the repository
			return totalGamesRepository.findAll(page).toList();
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	public User findWinner(){
		try {
			
			//TODO: get Winner and retrieve it from the repository
			return totalGamesRepository.findAll(page).toList();
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	
	
	

}
