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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.entregam15.security.RestAuthenticationEntryPoint;
import com.entregam15.security.TokenAuthentificationFilter;

//Configuration JWT
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Bean
	public TokenAuthentificationFilter createTokenAuthenticationFilter() {
		return new TokenAuthentificationFilter(); 
	}
	
	@Bean
	public PasswordEncoder createPasswordEncoder() {
		return new BCryptPasswordEncoder();
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
			.antMatchers("/",             //Urls --> Swagger
                    "/error",       
                    "/favicon.ico",
                    "/**/*.png",
                    "/**/*.gif",
                    "/**/*.svg",
                    "/**/*.jpg",
                    "/**/*.html",
                    "/**/*.css",
                    "/**/*.js",
                    "/**/*.woff2"
	        			)
                    .permitAll()
			
			
				.antMatchers(
						"/POST/login",            //login - > This Url doesnt need authentication
						"/POST/players",          //sign up -> This Url doesnt need authentication
						"/v2/api-docs",           //Urls --> Swagger
						"/webjars/**",            //Urls --> Swagger
	            		"/swagger-resources/**"   //Urls --> Swagger
						)
					.permitAll() 
				.anyRequest()              //the rest of the URls need authentication
					.authenticated();
		
		http.addFilterBefore(createTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}