/**
 * 
 */
package com.entregam15.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.entregam15.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

//Wrapper of User and used for Spring Security to manage authorized users
public class UserPrincipal implements UserDetails {
	
	private User user;
	private Collection<? extends GrantedAuthority> authorities;
	
	//Grant "ROLE_USER" authorization for a logged user
	public static UserPrincipal create(User user) {
		List<GrantedAuthority> authorities = Collections
				.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
		return new UserPrincipal(user, authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUserName();
	}
    
	
	public void setPassword(String password) {
		 user.setPassword(password);
	}

	
	public void setUsername(String username) {
		user.setUserName(username);
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	// Per aconseguir un usuari de SecurityContextHolder (L'usuari que ha s'ha
	// autentificat i se l'ha autoritzat a utilitzar l'API)
	public static User getCurrentUser() {
		UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return principal.getUser();
	}
}