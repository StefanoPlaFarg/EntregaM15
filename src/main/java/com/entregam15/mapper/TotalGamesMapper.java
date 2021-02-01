/**
 * 
 */
package com.entregam15.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.entregam15.dto.AverageRankingDTO;
import com.entregam15.dto.GameDTO;
import com.entregam15.dto.GamesUserDTO;
import com.entregam15.dto.TotalAverageGamesDTO;
import com.entregam15.dto.TotalGamesDTO;
import com.entregam15.entity.Game;
import com.entregam15.entity.TotalGames;
import com.entregam15.entity.User;

/**
 * @author stefano
 *
 */

//Convertidor de Entitites Rankings < ---- > a DTO
@Component
public class TotalGamesMapper extends AbstractMapper<TotalGames, TotalGamesDTO> {

	@Override
	public TotalGamesDTO fromEntity(TotalGames totalGames) {
		if (totalGames == null)return null;
		return TotalGamesDTO.builder()
				.userName(totalGames.getUser().getUserName())
				.averageRankingUser(totalGames.getAverageSuccess())
				.build();
	}

	private List<TotalGamesDTO> fromAllEntities(List<TotalGames> listTotalGames) {
		if (listTotalGames == null)
			return null;

		List<TotalGamesDTO> listTotalGamesDTO = listTotalGames.stream().map(TotalGames -> fromEntity(TotalGames))
				.collect(Collectors.toList());

		return listTotalGamesDTO;
	}

	public TotalAverageGamesDTO fromAllTotalGamesEntities(List<TotalGames> listTotalGames) {
		if (listTotalGames == null)
			return null;
		return TotalAverageGamesDTO.builder()
				.listTotalGamesUser(fromAllEntities(listTotalGames))
				.build();

	}
	
	public AverageRankingDTO fromAverageRanking(double averageRanking) {
		
		return AverageRankingDTO.builder()
				.averageRanking(averageRanking)
				.build();

	}
	

}
