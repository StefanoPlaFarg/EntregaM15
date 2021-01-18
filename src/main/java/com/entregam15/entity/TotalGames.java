/**
 * 
 */
package com.entregam15.entity;


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
@Table(name="TOTAL_GAMES")
/**
 * @author stefano
 *
 */
public class TotalGames {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name="FK_USER", nullable = false)
	private User user;
	
	@Column(name="AVERAGE_SUCCESS", nullable = false)
	private double averageSuccess;

}
