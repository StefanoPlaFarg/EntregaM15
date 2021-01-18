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

@Component

public class Game {

	private int valueFirstDice;
	private int valueSecondDice;

	/**
	 * Metode que retorna un boolea (true) si els dos daus tirats sumen 7
	 * @return
	 */
	public boolean isGameWon() {

		valueFirstDice = (int) ((Math.random() * 5) + 1);
		valueSecondDice = (int) ((Math.random() * 5) + 1);
		return (valueFirstDice + valueSecondDice) == 7;

	}
	
	
}
