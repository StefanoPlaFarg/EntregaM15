/**
 * 
 */
package com.entregam15.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.entregam15.entity.User;
import com.entregam15.exception.NoDataFoundException;
import com.entregam15.exception.ValidateServiceException;
import com.entregam15.repository.UserRespository;
import com.entregam15.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenAuthentificationFilter extends OncePerRequestFilter {

	@Autowired
	private UserService userService; //The same instance as the other userService from GameController (Singleton)

	@Autowired
	private UserRespository userRepository; //The same instance as the other userRepository from userService (Singleton)

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			String jwt = getJwtFromRequest(request);//Get the token from the request

			if (StringUtils.hasText(jwt) && userService.validateToken(jwt)) {//Validate the token
				
				//Get the user id from the token
				ObjectId idUserFromToken = new ObjectId (userService.getUserIdFromToken(jwt));
						
					
                
				//Get the user by his id
				User user = userRepository.findById(idUserFromToken)
						.orElseThrow(() -> new NoDataFoundException("The user doesn't exist"));

				//Create the wrapper for the user and give him authorities
				UserPrincipal userPrincipal = UserPrincipal.create(user);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userPrincipal,
						null, userPrincipal.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
				//Create authentication and authorization for the user in the pool of authenticated and authorized users 
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			log.error("Authentification Error", e);
		}
		//Change the filter
		filterChain.doFilter(request, response);
	}
    
	//Get the token from Request
	public String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}
}