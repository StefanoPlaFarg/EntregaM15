/**
 * 
 */
package com.entregam15.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TotalAverageGamesDTO {
	
	private List<TotalGamesUserDTO> listTotalGamesUser;

}
