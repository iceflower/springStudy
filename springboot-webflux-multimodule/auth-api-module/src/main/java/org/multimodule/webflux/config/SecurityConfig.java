package org.multimodule.webflux.config;

import org.multimodule.webflux.common.security.JwtAccessDeniedHandler;
import org.multimodule.webflux.common.security.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
@ComponentScan(basePackages = {"org.multimodule.webflux.common.security"})
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	@Autowired
	private JwtAccessDeniedHandler jwtAccessDeniedHandler;

	@Autowired
	private ReactiveAuthenticationManager authenticationManager;
	
	@Autowired
	private ServerSecurityContextRepository securityContextRepository;
	
	
	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http.exceptionHandling()
		.authenticationEntryPoint(jwtAuthenticationEntryPoint)
		.accessDeniedHandler(jwtAccessDeniedHandler)
		.and()
		.csrf().disable()
		.formLogin().disable()
		.httpBasic().disable()
		.authenticationManager(authenticationManager)
		.securityContextRepository(securityContextRepository)
		.authorizeExchange()
		.pathMatchers(HttpMethod.OPTIONS).permitAll()
		.pathMatchers("/login").permitAll()
		.anyExchange().authenticated();
		
		return http.build();
	}

}
