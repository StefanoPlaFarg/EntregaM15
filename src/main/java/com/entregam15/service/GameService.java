/**
 * 
 */
package com.entregam15.service;


import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.entregam15.entity.*;
import com.entregam15.exception.*;
import com.entregam15.repository.*;
import com.entregam15.security.UserPrincipal;

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
	
	//@Transactional
	public Game playAndSaveGame(User user) {
		try {			
			
					
			//Play the Game
			DiceGame diceGame = DiceGame.builder().build();
			diceGame.play();

			//Save the game into Game Repository
			Game newGame = Game.builder()
					.user(user)
					.valueFirstDice(diceGame.getValueFirstDice())
					.valueSecondDice(diceGame.getValueSecondDice())
					.totalValue(diceGame.getTotalValueGame())
					.gameWon(diceGame.isGameWon())
					.build();

			gameRepository.save(newGame);
			
			
			
			//Update and save the Average Success into ranking Repository
			TotalGames totalGames = user.getTotalGames();
			
			if (totalGames==null ) { // The user hasn't neither games nor totalgames yet
             
				TotalGames newTotalGames = TotalGames.builder()
				.user(user)
				.averageSuccess(calcUpdatedRanking(user, newGame))				
				.build();		
				
				totalGames=newTotalGames;
				
				rankingRepository.save(totalGames);
				
			}else {
				
				totalGames.setAverageSuccess(calcUpdatedRanking(user, newGame));
				rankingRepository.save(totalGames);	
				
			}
			
			
			
			
			return newGame;
			
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	
	private double calcUpdatedRanking(User user, Game newGame) {

		//
		//List <Game> listGames = gameRepository.findAllbyUserName(user.getUserName());
		
		try {

			if (user.getListGames() == null)
				throw new NoDataFoundException("The user has no games");

			/*
			 * double counterGamesWonByUser = (double)user.getListGames().stream()
			 * .filter(Game -> Game.isGameWon() == true) .count();
			 * 
			 * //.map(Game -> 1) //.reduce(0, (sum, value) -> sum + value);
			 * 
			 * double totalGamesByUser = (double)user.getListGames().stream().count();
			 * //.map(Game -> 1) //.reduce(0, (sum, value) -> sum + value);
			 * 
			 */

			double counterGamesWonByUser = 0;

			for (Game game : user.getListGames()) {

				if (game.isGameWon())
					counterGamesWonByUser++;

			}
			
			if (newGame.isGameWon()) counterGamesWonByUser++;

			double totalGamesByUser = (double) ( user.getListGames().size() +1)  ;

				System.out.println("counterGamesWonByUser "+counterGamesWonByUser );
			System.out.println("totalGamesByUser "+totalGamesByUser );
			System.out.println("result "+counterGamesWonByUser / totalGamesByUser);
			
			if (counterGamesWonByUser == 0)
				return 0;
			
			return (double) counterGamesWonByUser / totalGamesByUser;
			
			
			
			
			

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
				throw new NoDataFoundException("The user has no games");

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

	/*public List<Game> findAllGamesByUser(User user) {
		try {
            
			if (user.getListGames()==null) throw new NoDataFoundException("The user has no games");
			
			return user.getListGames();

		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	*/
	
	public List<Game> findAllGamesByUser(User user, Pageable page) {
		try {

			if (user.getListGames() == null)
				throw new NoDataFoundException("The user has no games");

			return gameRepository.findAllByUser(user, page).toList();

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
			
			if ( user.getTotalGames()==null )throw new NoDataFoundException("The user has no games");
			return rankingRepository.findById(user.getTotalGames().getId()).get();

		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	
	  
     
	
       
       private void deleteAllRankingsByUser(User user) {
   		try {
   			
   			if ( user.getTotalGames()==null )throw new NoDataFoundException("The user has no games");
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
			
			if (rankingRepository.findAll()==null) throw new NoDataFoundException("There are no games");
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
			
			if (rankingRepository.findAll()==null) throw new NoDataFoundException("There are no games");
			
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
	
	@Transactional
	public User findLoser() {
		try {

			if (rankingRepository.findAll().isEmpty()) 	throw new NoDataFoundException("There are no games");
			

			TotalGames worstTotalgames = TotalGames.builder().id(null).user(null).averageSuccess(1).build();

			rankingRepository.findAll().stream().forEach(TotalGames -> {

				if (TotalGames.getAverageSuccess() <= worstTotalgames.getAverageSuccess()) {
					worstTotalgames.setId(TotalGames.getId());
					worstTotalgames.setUser(TotalGames.getUser());
					worstTotalgames.setAverageSuccess(TotalGames.getAverageSuccess());

				}
			});

			User loser = worstTotalgames.getUser();		

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

			if (rankingRepository.findAll().isEmpty())
				throw new NoDataFoundException("There are no games");

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
