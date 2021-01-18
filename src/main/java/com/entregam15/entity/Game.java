/**
 * 
 */
package com.entregam15.entity;

/**
 * @author stefano
 *
 */

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="GAMES")
public class Game {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="FK_USER", nullable = false)
	private User user;
	
	@Column(name="VALUE_FIRST_DICE", nullable = false)
	private int valueFirstDice;
	
	@Column(name="VALUE_SECOND_DICE", nullable = false)
	private int valueSecondDice;
	
	@Column(name="TOTAL_VALUE", nullable = false)
	private int totalValue;
	

}
