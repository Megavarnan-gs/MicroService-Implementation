package com.regulareedge.apigateway.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;

import com.regulareedge.apigateway.config.JwtProperties;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

class JwtAuthenticationGlobalFilterTest {

	private static final String TEST_SECRET =
			"RegulareEdgeSecretKeyForJwtSigningMustBeAtLeast256BitsLongForHS256Algorithm2026";

	private JwtProperties jwtProperties;
	private JwtAuthenticationGlobalFilter filter;

	@BeforeEach
	void setUp() {
		jwtProperties = new JwtProperties();
		jwtProperties.setSecret(TEST_SECRET);
		jwtProperties.setAccessTokenExpirationMs(3600000);
		jwtProperties.setRefreshTokenExpirationMs(604800000);
		filter = new JwtAuthenticationGlobalFilter(jwtProperties);
	}

	@Test
	void allowsPublicAuthPathsWithoutToken() {
		MockServerHttpRequest request = MockServerHttpRequest.post("/auth/login").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		GatewayFilterChain chain = mock(GatewayFilterChain.class);
		when(chain.filter(exchange)).thenReturn(Mono.empty());

		filter.filter(exchange, chain).block();

		verify(chain, times(1)).filter(exchange);
	}

	@Test
	void rejectsRequestWithMissingToken() {
		MockServerHttpRequest request = MockServerHttpRequest.get("/notifications/1").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		GatewayFilterChain chain = mock(GatewayFilterChain.class);

		filter.filter(exchange, chain).block();

		assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		verify(chain, times(0)).filter(exchange);
	}

	@Test
	void rejectsRequestWithInvalidToken() {
		MockServerHttpRequest request = MockServerHttpRequest.get("/notifications/1")
				.header("Authorization", "Bearer not-a-real-jwt")
				.build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		GatewayFilterChain chain = mock(GatewayFilterChain.class);

		filter.filter(exchange, chain).block();

		assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		verify(chain, times(0)).filter(exchange);
	}

	@Test
	void rejectsExpiredToken() {
		SecretKey key = Keys.hmacShaKeyFor(TEST_SECRET.getBytes(StandardCharsets.UTF_8));
		String expiredToken = Jwts.builder()
				.subject("test-user")
				.issuedAt(Date.from(Instant.now().minusSeconds(7200)))
				.expiration(Date.from(Instant.now().minusSeconds(3600)))
				.signWith(key)
				.compact();

		MockServerHttpRequest request = MockServerHttpRequest.get("/notifications/1")
				.header("Authorization", "Bearer " + expiredToken)
				.build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		GatewayFilterChain chain = mock(GatewayFilterChain.class);

		filter.filter(exchange, chain).block();

		assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		verify(chain, times(0)).filter(exchange);
	}

	@Test
	void allowsRequestWithValidToken() {
		SecretKey key = Keys.hmacShaKeyFor(TEST_SECRET.getBytes(StandardCharsets.UTF_8));
		String validToken = Jwts.builder()
				.subject("test-user")
				.issuedAt(Date.from(Instant.now()))
				.expiration(Date.from(Instant.now().plusSeconds(3600)))
				.signWith(key)
				.compact();

		MockServerHttpRequest request = MockServerHttpRequest.get("/notifications/1")
				.header("Authorization", "Bearer " + validToken)
				.build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		GatewayFilterChain chain = mock(GatewayFilterChain.class);
		when(chain.filter(exchange)).thenReturn(Mono.empty());

		filter.filter(exchange, chain).block();

		verify(chain, times(1)).filter(exchange);
	}
}
