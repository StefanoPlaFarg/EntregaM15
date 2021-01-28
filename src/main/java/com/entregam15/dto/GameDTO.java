/**
 * 
 */
package com.entregam15.dto;

import org.bson.types.ObjectId;

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

	private ObjectId idGame;		
    private int valueFirstDice;	
	private int valueSecondDice;	
	private int totalValue;
	private boolean gameWon;
	
}