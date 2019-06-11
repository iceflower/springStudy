package org.multimodule.webflux.common.security;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.multimodule.webflux.common.entity.Role;
import org.multimodule.webflux.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	@SuppressWarnings("unchecked")
	public Mono<Authentication> authenticate(Authentication authentication) {
		String authToken = authentication.getCredentials().toString();
		String username;
		try {
			username = jwtUtil.getUsernameFromToken(authToken);
		} catch (Exception e) {
			username = null;
		}
		if (username != null && jwtUtil.validateToken(authToken)) {
			Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
			List<String> rolesMap = claims.get("role", List.class);
			ArrayList<Role> roles = new ArrayList<Role>();
			for (String role : rolesMap) {
				roles.add(Role.valueOf(role));
			}
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
					username,
					null,
					roles.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toList())
					);
			return Mono.just(auth);
		} else {
			return Mono.empty();
		}

	}
}