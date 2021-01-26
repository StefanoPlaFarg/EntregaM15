/**
 * 
 */
package com.entregam15.controller;

/**
 * @author stefano
 *
 */

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.entregam15.dto.*;
import com.entregam15.entity.*;
import com.entregam15.mapper.*;
import com.entregam15.service.*;
import com.entregam15.wrapper.*;




@RestController
public class GameController {
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GameMapper gameMapper;
	
	@Autowired
	private TotalGamesMapper totalGamesMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	
	//Users
	
	@PostMapping(value = "/POST/players")
	public ResponseEntity<WrapperResponse<UserDTO>> createUser(@RequestBody SignupRequestDTO signupDTO) {

		User userToCreate = userMapper.signUp(signupDTO);

		User userCreated = userService.createUser(userToCreate);

		return new WrapperResponse<>(true, "success", userMapper.fromEntity(userCreated)).createResponse();

	}
	
	@PutMapping(value = "/PUT/players")
    public ResponseEntity<WrapperResponse<UserDTO>> updateUser(@RequestBody UserDTO userDTO) {
		
		User userToUpdate = userMapper.fromDTO(userDTO);
	
		User userUpdated = userService.updateUser(userToUpdate);
		
		return new WrapperResponse<>(true, "success", userMapper.fromEntity(userUpdated)).createResponse();
	}
	
	@PostMapping(value = "/POST/login")
	public ResponseEntity<WrapperResponse<LoginResponseDTO>> login(@RequestBody LoginRequestDTO loginRequestDTO) {

		LoginResponseDTO loginResponseDTO = userService.login(loginRequestDTO);

		return new WrapperResponse<>(true, "success", loginResponseDTO).createResponse();

	}
	
	// Game

	@PostMapping(value = "/POST/players/{id}/games")
	public ResponseEntity<WrapperResponse<GameDTO>> play(@PathVariable(name = "id") Long id) {

		User user = userService.findById(id);

		Game game = gameService.playAndSaveGame(user);

		return new WrapperResponse<>(true, "success", gameMapper.fromEntity(game)).createResponse();
	}

	@DeleteMapping(value = "/DELETE/players/{id}/games")
	public ResponseEntity<?> deleteGames(@PathVariable(name = "id") Long id) {
		
		User user = userService.findById(id);
		gameService.deleteAllGamesAndRankingByUser(user);
		
		
		
		return new WrapperResponse<>(true, "success", null).createResponse();
	}
	
	@GetMapping(value = "/GET/players/{id}/games")
	public ResponseEntity<WrapperResponse<GamesUserDTO>> findAllGamesByUser(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "5") int pageSize,
			@PathVariable(name="id") Long id){
		
		Pageable page = PageRequest.of(pageNumber, pageSize);
		User user = userService.findById(id);
		
		List<Game> listGamesByUser =gameService.findAllGamesByUser(user, page);
		return new WrapperResponse<>(true, "success", gameMapper.fromAllEntitiesByUser(user, listGamesByUser))
				.createResponse();
	}
	
	
	
	
	
	@GetMapping(value = "/GET/players")
	public ResponseEntity<WrapperResponse<TotalAverageGamesDTO>> findAllRankings(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "5") int pageSize
			){
		
		Pageable page = PageRequest.of(pageNumber, pageSize);
		
		List<User> existingUsersWithGames = userService.findAllUsersWithGames();
		
		List<TotalGames> listTotalGames =gameService.findGroupRankings(page, existingUsersWithGames);
		return new WrapperResponse<>(true, "success", totalGamesMapper.fromAllTotalGamesEntities(listTotalGames))
				.createResponse();
	}
	
	
	
	@GetMapping(value = "/GET/players/ranking")
      public ResponseEntity<WrapperResponse<AverageRankingDTO>> findAverageRanking() {
		
		List<User> existingUsersWithGames = userService.findAllUsersWithGames();
		double averageRanking = gameService.findAverageRanking(existingUsersWithGames);
		
		return new WrapperResponse<>(true, "success", totalGamesMapper.fromAverageRanking(averageRanking)).createResponse();
	}

	
	@GetMapping(value = "/GET/players/ranking/loser")
	public ResponseEntity<WrapperResponse<UserDTO>> findLoser() {

		List<User> existingUsersWithGames = userService.findAllUsersWithGames();
		
		User loser = gameService.findLoser(existingUsersWithGames);

		return new WrapperResponse<>(true, "success", userMapper.fromEntity(loser)).createResponse();
	}
	
	@GetMapping(value = "/GET/players/ranking/winner")
	public ResponseEntity<WrapperResponse<UserDTO>> findWinner() {
     
		List<User> existingUsersWithGames = userService.findAllUsersWithGames();
		User winner = gameService.findWinner(existingUsersWithGames);
		
		return new WrapperResponse<>(true, "success", userMapper.fromEntity(winner)).createResponse();
	}

}
