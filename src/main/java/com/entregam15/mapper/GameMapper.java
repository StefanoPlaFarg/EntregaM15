/**
 * 
 */
package com.entregam15.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.entregam15.dto.GameDTO;
import com.entregam15.dto.GamesUserDTO;
import com.entregam15.entity.Game;
import com.entregam15.entity.User;


/**
 * @author stefano
 *
 */
public class GameMapper extends AbstractMapper<Game, GameDTO> {
   
	@Override
	public GameDTO fromEntity(Game entity) {
		return GameDTO.builder()
				.valueFirstDice(entity.getValueFirstDice())
                .valueSecondDice(entity.getValueSecondDice())
				.totalValue(entity.getTotalValue())
				.gameWon(entity.isGameWon())
				.build();
	}
	
	

	private List<GameDTO> fromAllEntities(List<Game> listGames) {
		if (listGames == null)
			return null;

		List<GameDTO> listGamesDTO = listGames.stream().map(Game -> fromEntity(Game)).collect(Collectors.toList());

		return listGamesDTO;
	}
	
	
	public GamesUserDTO fromAllEntitiesByUSer (User user, List<Game> listGames) {
		
		return GamesUserDTO.builder()
				.userName(user.getUserName())
                .listGamesByUser(fromAllEntities(listGames))				
				.build();
		
	}
	
	
	
	
}
