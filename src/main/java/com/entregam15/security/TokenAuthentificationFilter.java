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
			String jwt = getJwtFromRequest(request);

			if (StringUtils.hasText(jwt) && userService.validateToken(jwt)) {
				
				
				// String username = userService.getUsernameFromToken(jwt);

				Long idUserFromToken = Long.valueOf(userService.getUserIdFromToken(jwt));

				User user = userRepository.findById(idUserFromToken)
						.orElseThrow(() -> new NoDataFoundException("The user doesn't exist"));

				UserPrincipal userPrincipal = UserPrincipal.create(user);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userPrincipal,
						null, userPrincipal.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			log.error("Authentification Error", e);
		}
		filterChain.doFilter(request, response);
	}

	public String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}
}