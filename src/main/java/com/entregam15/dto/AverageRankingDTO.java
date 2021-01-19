/**
 * 
 */
package com.entregam15.dto;

/**
 * @author stefano
 *
 */

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
public class AverageRankingDTO {
	
    private double averageRanking;
}
