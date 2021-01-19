/**
 * 
 */
package com.entregam15.dto;

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
public class GameDTO {

	private Long idGame;		
    private int valueFirstDice;	
	private int valueSecondDice;	
	private int totalValue;
	
}