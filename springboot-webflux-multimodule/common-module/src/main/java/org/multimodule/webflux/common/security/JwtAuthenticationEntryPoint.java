package org.multimodule.webflux.common.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationEntryPoint implements ServerAuthenticationEntryPoint{

	@Override
	public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
		exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		return Mono.fromRunnable(() -> {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		});
	}
	

}
