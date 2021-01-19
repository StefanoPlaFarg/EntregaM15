/**
 * 
 */
package com.entregam15.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author stefano
 *
 */



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="USERS")
public class User {

	 @Id
	 @Column (name ="ID")
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	 	 
	 @Column(name="NAME", nullable = false)
	 private String userName;
	 
	 @Column(name="PASSWORD", nullable = false)
	 private String password;
	 
	 @Column(name="REGISTRATION DATE", nullable = false)
	 private LocalDateTime registrationDate;
	 
	 @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	 private List<Game> listGames;
	 
	 @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	 private TotalGames totalGames;
}
