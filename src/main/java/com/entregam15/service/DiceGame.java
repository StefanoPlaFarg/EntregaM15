/**
 * 
 */
package com.entregam15.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author stefano
 *
 */

/**
 * @author stefano
 *
 */
/**
 * @author stefano
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class DiceGame {

	private int valueFirstDice;
	private int valueSecondDice;
	private int totalValueGame;
	private boolean isGameWon;


	//Play a game
	public void play() {

		valueFirstDice = (int) ((Math.random() * 5) + 1);
		valueSecondDice = (int) ((Math.random() * 5) + 1);
		totalValueGame = valueFirstDice + valueSecondDice;
		isGameWon=totalValueGame==7;
		

	}
	
	
}
