package com.regulareedge.apigateway.filter;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.regulareedge.apigateway.config.JwtProperties;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

/**
 * Lightweight, purely reactive JWT gate for the gateway. It does not perform
 * full Spring Security authentication (no SecurityContext, no filter chain
 * beans) - it simply validates the signature and expiry of the bearer token
 * on every request except the public auth endpoints, and short-circuits with
 * a 401 JSON body when the token is missing or invalid. The original
 * Authorization header is left untouched so the downstream business service
 * can re-validate/parse the token itself.
 */
@Component
public class JwtAuthenticationGlobalFilter implements GlobalFilter, Ordered {

	private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationGlobalFilter.class);

	private static final String[] PUBLIC_PATH_PREFIXES = {
			"/auth/",
			"/swagger-ui",
			"/v3/api-docs",
			"/webjars",
			"/actuator"
	};
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";

	private final JwtProperties jwtProperties;
	private final ObjectMapper objectMapper;

	public JwtAuthenticationGlobalFilter(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String path = request.getURI().getPath();

		if (isPublicPath(path)) {
			return chain.filter(exchange);
		}

		String authHeader = request.getHeaders().getFirst(AUTHORIZATION_HEADER);

		if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
			return unauthorized(exchange, "Missing or malformed Authorization header");
		}

		String token = authHeader.substring(BEARER_PREFIX.length());

		try {
			validate(token);
			return chain.filter(exchange);
		} catch (ExpiredJwtException e) {
			log.debug("Rejected expired JWT for path {}: {}", path, e.getMessage());
			return unauthorized(exchange, "Token has expired");
		} catch (JwtException | IllegalArgumentException e) {
			log.debug("Rejected invalid JWT for path {}: {}", path, e.getMessage());
			return unauthorized(exchange, "Invalid token");
		}
	}

	private boolean isPublicPath(String path) {
		if (path == null) {
			return false;
		}
		for (String prefix : PUBLIC_PATH_PREFIXES) {
			if (path.startsWith(prefix) || path.contains(prefix)) {
				return true;
			}
		}
		return false;
	}

	private void validate(String token) {
		SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
		Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token);
	}

	private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		Map<String, Object> body = Map.of(
				"timestamp", java.time.Instant.now().toString(),
				"status", HttpStatus.UNAUTHORIZED.value(),
				"error", "Unauthorized",
				"message", message,
				"path", exchange.getRequest().getURI().getPath());

		byte[] bytes;
		try {
			bytes = objectMapper.writeValueAsBytes(body);
		} catch (Exception e) {
			bytes = ("{\"error\":\"Unauthorized\"}").getBytes(StandardCharsets.UTF_8);
		}

		DataBuffer buffer = response.bufferFactory().wrap(bytes);
		return response.writeWith(Mono.just(buffer));
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 1;
	}
}
