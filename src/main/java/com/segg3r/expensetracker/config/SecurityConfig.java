package com.segg3r.expensetracker.config;

import com.segg3r.expensetracker.auth.UserDetailsMongoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeRequests()
					.antMatchers("/", "/#/home").authenticated()
					.antMatchers("/actuator/*").permitAll()
					.antMatchers("/api/*").authenticated()
					.antMatchers("/api/auth/login").permitAll()
					.anyRequest().authenticated()
					.and()
				.formLogin()
					.loginPage("/#/login")
					.successForwardUrl("/#/home")
					.permitAll()
					.and()
				.logout()
					.permitAll();
	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		return new UserDetailsMongoService();
	}

}