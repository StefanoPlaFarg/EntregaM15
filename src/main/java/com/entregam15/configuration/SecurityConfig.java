/**
 * 
 */
package com.entregam15.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.entregam15.security.RestAuthenticationEntryPoint;
import com.entregam15.security.TokenAuthentificationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Bean
	public TokenAuthentificationFilter createTokenAuthenticationFilter() {
		return new TokenAuthentificationFilter(); 
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.cors()
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
			.csrf()
				.disable()
			.formLogin()
				.disable()
			.httpBasic()
				.disable()
			.exceptionHandling()
				.authenticationEntryPoint(new RestAuthenticationEntryPoint())
				.and()
			.authorizeRequests()
				.antMatchers(
						"/POST/login",    //login
						"/POST/players"   //sign up -> create User
						)
					.permitAll() 
				.anyRequest()
					.authenticated();
		
		http.addFilterBefore(createTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}