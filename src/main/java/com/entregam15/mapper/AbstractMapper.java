/**
 * 
 */
package com.entregam15.mapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractMapper<E, D> {

	public abstract D fromEntity(E entity);

}