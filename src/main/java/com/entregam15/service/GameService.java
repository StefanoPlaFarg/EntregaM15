/**
 * 
 */
package com.entregam15.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entregam15.dto.TotalGamesDTO;
import com.entregam15.entity.*;
import com.entregam15.exception.*;
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
			
			//Play the Game
			DiceGame diceGame = DiceGame.builder().build();
			diceGame.play();

			//Save the game into Game Repository
			Game game = Game.builder()
					.user(user)
					.valueFirstDice(diceGame.getValueFirstDice())
					.valueSecondDice(diceGame.getValueSecondDice())
					.totalValue(diceGame.getTotalValueGame())
					.gameWon(diceGame.isGameWon())
					.build();

			gameRepository.save(game);
			
			//Update and save the Average Success into ranking Repository
			TotalGames totalGames = user.getTotalGames();
			totalGames.setAverageSuccess(calcUpdatedRanking(user));
			rankingRepository.save(totalGames);			
			return game;
			
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	
	
	public void deleteAllGamesAndRankingByUser(User user) {
		try {

			if (user.getListGames() == null)
				new NoDataFoundException("The user has no games");

			// Delete TotalGames of User from RankingRepository
			deleteAllRankingsByUser(user);

			// Delete Games of User from GamesRepository
			List<Game> listGamesToBeRemoved = gameRepository.findAll().stream()
					.filter(Game -> Game.getUser().getId() == user.getId()).collect(Collectors.toList());

			gameRepository.deleteAll(listGamesToBeRemoved);

		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	

	public List<Game> findAllGamesByUser(User user) {
		try {
            
			if (user.getListGames()==null) new NoDataFoundException("The user has no games");
			
			return user.getListGames();

		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	
	
	
	
	
	public List<Game> findGamesByUser(User user, Pageable page){
		try {
						
			return gameRepository.findAll(page).toList();
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	
	
	//Rankings 	
	
	public TotalGames findAverageRankingByUser(User user) {
		try {
			
			if ( user.getTotalGames()==null )new NoDataFoundException("The user has no games");
			return rankingRepository.findById(user.getTotalGames().getId()).get();

		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	
	  
       private double calcUpdatedRanking (User user) {
		
			try {

				if (user.getTotalGames() == null) {// The user hasn't neither games nor totalgames yet

					return 0.0;

				} else {

					int counterGamesWonByUser = user.getListGames().stream().filter(Game -> Game.isGameWon() == true)
							.map(Game -> 1).reduce(0, (sum, value) -> sum + value);

					int totalGamesByUser = user.getListGames().stream().map(Game -> 1).reduce(0,
							(sum, value) -> sum + value);

					double updatedAverageRanking = counterGamesWonByUser / totalGamesByUser;

					return updatedAverageRanking;

				}

   		} catch (ValidateServiceException | NoDataFoundException e) {
   			log.info(e.getMessage(), e);
   			throw e;
   		} catch (Exception e) {
   			log.error(e.getMessage(), e);
   			throw new GeneralServiceException(e.getMessage(), e);
   		}
		
		
	}
	
       
       public void deleteAllRankingsByUser(User user) {
   		try {
   			
   			if ( user.getTotalGames()==null )new NoDataFoundException("The user has no games");
   			rankingRepository.deleteById(user.getTotalGames().getId());

   		} catch (ValidateServiceException | NoDataFoundException e) {
   			log.info(e.getMessage(), e);
   			throw e;
   		} catch (Exception e) {
   			log.error(e.getMessage(), e);
   			throw new GeneralServiceException(e.getMessage(), e);
   		}
   	}
       
       
	
	public List<TotalGames> findGroupRankings( Pageable page) {
		try {

			return rankingRepository.findAll(page).toList();
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	public List<TotalGames> findAllRankings() {
		try {
			
			if (rankingRepository.findAll()==null) new NoDataFoundException("There are no games");
			return rankingRepository.findAll();
			
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
			
			if (rankingRepository.findAll()==null) new NoDataFoundException("There are no games");
			
			double counterAverageSuccessAllUsers= rankingRepository.findAll().stream()   				      				   
  				   .map(TotalGames -> TotalGames.getAverageSuccess())    				      				   
  				   .reduce(0.0, (sum, value) -> sum + value);
  		   
  		   int counterTotalGames= rankingRepository.findAll().stream() 
   				   .map(TotalGames -> 1)    				      				   
  				   .reduce(0, (sum, value) -> sum + value);
  		
				double AverageRanking = counterAverageSuccessAllUsers/counterTotalGames;
  		   
 		       return AverageRanking; 
			
			
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	public User findLoser() {
		try {
			
            if (rankingRepository.findAll()==null) new NoDataFoundException("There are no games");
            
			User loser;
			TotalGames worstTotalgames = TotalGames.builder().id(null).user(null).averageSuccess(1).build();
            			
			rankingRepository.findAll().stream().forEach(TotalGames -> {

				if (TotalGames.getAverageSuccess() <= worstTotalgames.getAverageSuccess()) {
					worstTotalgames.setId(TotalGames.getId());
					worstTotalgames.setUser(TotalGames.getUser());
					worstTotalgames.setAverageSuccess(TotalGames.getAverageSuccess());

				}
			});

			loser = worstTotalgames.getUser();

			return loser;

		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	public User findWinner() {
		try {
			
			if (rankingRepository.findAll()==null) new NoDataFoundException("There are no games");
			
			User winner;
			TotalGames bestTotalgames = TotalGames.builder().id(null).user(null).averageSuccess(0).build();

			rankingRepository.findAll().stream().forEach(TotalGames -> {

				if (TotalGames.getAverageSuccess() >= bestTotalgames.getAverageSuccess()) {
					bestTotalgames.setId(TotalGames.getId());
					bestTotalgames.setUser(TotalGames.getUser());
					bestTotalgames.setAverageSuccess(TotalGames.getAverageSuccess());

				}
			});

			winner = bestTotalgames.getUser();

			return winner;

		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	
	

}
