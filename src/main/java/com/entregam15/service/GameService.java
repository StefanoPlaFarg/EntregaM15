/**
 * 
 */
package com.entregam15.service;


import java.util.List;
import java.util.Optional;
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
	
	
	
	//USER
	
	// Update username of Games and Rankings

	public void updateUser(User newUser) {

		try {

			User userToUpdate = UserPrincipal.getCurrentUser();
			
			
			List<Game> listGamesUser = gameRepository.findAllByUser(userToUpdate).orElse(null);
			TotalGames rankingUser = rankingRepository.findByUser(userToUpdate).orElse(null);

			
			
			// Games

		
			

			for (Game game : listGamesUser) {

				game.getUser().setUserName(newUser.getUserName());

				gameRepository.save(game);
			

			}

			// Ranking

			if (rankingUser != null) {

				rankingUser.getUser().setUserName(newUser.getUserName());
				rankingRepository.save(rankingUser);

			}
			
			

		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}

	}
	
	//GAMES
	
	//Play a Game
	public Game playAndSaveGame(User user) {
		try {

			// Play the Game
			DiceGame diceGame = DiceGame.builder().build();
			diceGame.play();

			// Save the game into Game Repository
			Game newGame = Game.builder().user(user).valueFirstDice(diceGame.getValueFirstDice())
					.valueSecondDice(diceGame.getValueSecondDice()).totalValue(diceGame.getTotalValueGame())
					.gameWon(diceGame.isGameWon()).build();

			gameRepository.save(newGame);

			return newGame;

		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	
	
	
	//Delete All Games of a User and his ranking
	public void deleteAllGamesAndRankingByUser(User user) {
		try {

			List<Game> listGames = gameRepository.findAllByUser(user).orElse(null);
			
			if (listGames == null)
				throw new NoDataFoundException("The user has no games");

			// Delete TotalGames of User from RankingRepository
			deleteTankingByUser(user);

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

	//Get a set of Games (Page) of a User 
	public List<Game> findAllGamesByUser(User user, Pageable page) {
		try {

             List<Game> listGames = gameRepository.findAllByUser(user).orElse(null);
			
			if (listGames == null)
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
	
	private List<Game> findAllGamesByUser(User user) {
		try {

			List<Game> listGamesUser = gameRepository.findAllByUser(user).orElse(null);

			return listGamesUser;

		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	
	
	//RANKINGS	
	
		
	
	//Get the value of the current ranking of a user through his games
	private double calcRanking(User user) {

		
		List<Game> listGames = gameRepository.findAllByUser(user).orElse(null);

		try {

			if (listGames == null)
				throw new NoDataFoundException("The user has no games yet");

			
			//Get the number of won games
			double counterGamesWonByUser = 0;

			for (Game game : listGames) {

				if (game.isGameWon())
					counterGamesWonByUser++;

			}

			//Get the number games 
			double totalGamesByUser = (double) (listGames.size());

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
	
	//Save the updated ranking of a user
	public TotalGames updateRanking(User user) {

		try {

			TotalGames totalGames = rankingRepository.findByUser(user).orElse(null);

			if (totalGames == null) { // The user hasn't neither games nor totalgames yet

				TotalGames newTotalGames = TotalGames.builder().user(user).averageSuccess(calcRanking(user)).build();

				totalGames = newTotalGames;

				rankingRepository.save(totalGames);

			} else {

				totalGames.setAverageSuccess(calcRanking(user));
				rankingRepository.save(totalGames);

			}

			return totalGames;
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}

	}
	
	// Get all updated rankings of user who has games
	public List<TotalGames> UpdateAllRankings(List<User> existingUsers) {

		try {
 
			if 	 (existingUsers.size()== 0)
				throw new NoDataFoundException("The are no users");
			
			for (User user : existingUsers) {

				List<Game> listGamesUser = findAllGamesByUser(user);

				if (listGamesUser.size() != 0) {

					updateRanking(user);

				}

			}

			return rankingRepository.findAll();

		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}

	}
	
	
	
	// Get the updated ranking of a user
	public TotalGames findAverageRankingByUser(User user) {
		try {

			return updateRanking(user);

		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
     
	
	// Delete the ranking of a user
	private void deleteTankingByUser(User user) {

		try {

			TotalGames totalGames = rankingRepository.findByUser(user).orElse(null);

			if (totalGames == null)
				throw new NoDataFoundException("The user has no games yet");

			rankingRepository.deleteById(totalGames.getId());

		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
       
       
	//Get a set of rankings (Page)
	public List<TotalGames> findGroupRankings(Pageable page, List<User> existingUsers) {
		try {

			UpdateAllRankings(existingUsers);

			return rankingRepository.findAll(page).toList();

		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}
	
	
	//Get the average ranking of all users	
	public double findAverageRanking(List<User> existingUsers){
		try {
			
			if (rankingRepository.findAll()==null) throw new NoDataFoundException("There are no games yet");
			
			
			UpdateAllRankings(existingUsers);
			
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
	
	
	//Get the worst user by his ranking	
	public User findLoser(List<User> existingUsers) {
		try {

			
			if (rankingRepository.findAll().isEmpty()) 	throw new NoDataFoundException("There are no games");
			
			UpdateAllRankings(existingUsers);
			

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
	
	//Get the best user by his ranking	
	public User findWinner(List<User> existingUsers) {
		try {

			
			
			if (rankingRepository.findAll().isEmpty())
				throw new NoDataFoundException("There are no games");
			
			UpdateAllRankings(existingUsers);

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
