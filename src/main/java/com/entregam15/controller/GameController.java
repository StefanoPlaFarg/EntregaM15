/**
 * 
 */
package com.entregam15.controller;

/**
 * @author stefano
 *
 */

import java.util.List;

import org.bson.types.ObjectId;
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
	
	
	//API Users
	
	
	//http://localhost:8080/api/v1/POST/players  ---> CREATE a User
	@PostMapping(value = "/POST/players")
	public ResponseEntity<WrapperResponse<UserDTO>> createUser(@RequestBody SignupRequestDTO signupDTO) {

		User userToCreate = userMapper.signUp(signupDTO);

		User userCreated = userService.createUser(userToCreate);

		return new WrapperResponse<>(true, "success", userMapper.fromEntity(userCreated)).createResponse();

	}
	
	//http://localhost:8080/api/v1/PUT/players ---> UPDATE a Username
	@PutMapping(value = "/PUT/players")
    public ResponseEntity<WrapperResponse<UserDTO>> updateUser(@RequestBody UserDTO userDTO) {
		
		User userToUpdate = userMapper.fromDTO(userDTO);
	
		gameService.updateUser(userToUpdate);
		
		User userUpdated = userService.updateUser(userToUpdate);
		
		
		
		return new WrapperResponse<>(true, "success", userMapper.fromEntity(userUpdated)).createResponse();
	}
	
	//http://localhost:8080/api/v1/POST/login ---> LOGGIN
	@PostMapping(value = "/POST/login")
	public ResponseEntity<WrapperResponse<LoginResponseDTO>> login(@RequestBody LoginRequestDTO loginRequestDTO) {

		LoginResponseDTO loginResponseDTO = userService.login(loginRequestDTO);

		return new WrapperResponse<>(true, "success", loginResponseDTO).createResponse();

	}
	
	//API Game and Rankings

	//http://localhost:8080/api/v1/POST/players/1/games ---> (GET) User with iD = {id} plays a game
	@PostMapping(value = "/POST/players/{id}/games")
	public ResponseEntity<WrapperResponse<GameDTO>> play(@PathVariable(name = "id") ObjectId id) {

		User user = userService.findById(id);

		Game game = gameService.playAndSaveGame(user);

		return new WrapperResponse<>(true, "success", gameMapper.fromEntity(game)).createResponse();
	}


	
	//http://localhost:8080/api/v1/DELETE/players/1/games ---> (GET) User with iD = {id} delete all his games and rankings
	@DeleteMapping(value = "/DELETE/players/{id}/games")
	
	public ResponseEntity<?> deleteGames(@PathVariable(name = "id") ObjectId id) {
		
		User user = userService.findById(id);
		gameService.deleteAllGamesAndRankingByUser(user);
		
		
		
		return new WrapperResponse<>(true, "success", null).createResponse();
	}
	
	
	
	// http://localhost:8080/api/v1/GET/players/1/games?pageNumber=0&pageSize=10
	// ---> (GET) User with iD = {id} gets a set all his games (with a specified
	// Page)
	@GetMapping(value = "/GET/players/{id}/games")
	public ResponseEntity<WrapperResponse<GamesUserDTO>> findAllGamesByUser(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "5") int pageSize,
			@PathVariable(name = "id") ObjectId id) {

		Pageable page = PageRequest.of(pageNumber, pageSize);
		User user = userService.findById(id);

		List<Game> listGamesByUser = gameService.findAllGamesByUser(user, page);
		return new WrapperResponse<>(true, "success", gameMapper.fromAllEntitiesByUser(user, listGamesByUser))
				.createResponse();
	}

	// http://localhost:8080/api/v1/GET/players?pageNumber=0&pageSize=10 ----->
	// (GET) all Rankings of all Users
	@GetMapping(value = "/GET/players")
	public ResponseEntity<WrapperResponse<TotalAverageGamesDTO>> findAllRankings(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "5") int pageSize) {

		Pageable page = PageRequest.of(pageNumber, pageSize);

		List<User> existingUsers = userService.findAllUsers();

		List<TotalGames> listTotalGames = gameService.findGroupRankings(page, existingUsers);
		return new WrapperResponse<>(true, "success", totalGamesMapper.fromAllTotalGamesEntities(listTotalGames))
				.createResponse();
	}

	// http://localhost:8080/api/v1/GET/players/ranking ----> (GET) Average ranking
	// of all users
	@GetMapping(value = "/GET/players/ranking")
	public ResponseEntity<WrapperResponse<AverageRankingDTO>> findAverageRanking() {

		List<User> existingUsers = userService.findAllUsers();

		double averageRanking = gameService.findAverageRanking(existingUsers);

		return new WrapperResponse<>(true, "success", totalGamesMapper.fromAverageRanking(averageRanking))
				.createResponse();
	}

	// http://localhost:8080/api/v1/GET/players/ranking/loser ----> (GET) User with
	// worst ranking
	@GetMapping(value = "/GET/players/ranking/loser")
	public ResponseEntity<WrapperResponse<UserDTO>> findLoser() {

		List<User> existingUsers = userService.findAllUsers();

		User loser = gameService.findLoser(existingUsers);

		return new WrapperResponse<>(true, "success", userMapper.fromEntity(loser)).createResponse();
	}

	// http://localhost:8080/api/v1/GET/players/ranking/loser ----> (GET) User with
	// best ranking
	@GetMapping(value = "/GET/players/ranking/winner")
	public ResponseEntity<WrapperResponse<UserDTO>> findWinner() {

		List<User> existingUsers = userService.findAllUsers();

		User winner = gameService.findWinner(existingUsers);

		return new WrapperResponse<>(true, "success", userMapper.fromEntity(winner)).createResponse();
	}

}
