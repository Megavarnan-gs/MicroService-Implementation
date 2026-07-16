package com.regulareedge.apigateway.filter;

import java.util.UUID;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * Ensures every request/response pair flowing through the gateway carries an
 * {@code X-Correlation-Id} header, generating one when the caller did not
 * supply it. Runs before every other filter so downstream filters and the
 * proxied services can rely on the header being present.
 */
@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {

	public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
	public static final String CORRELATION_ID_ATTRIBUTE = "correlationId";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String correlationId = request.getHeaders().getFirst(CORRELATION_ID_HEADER);

		if (correlationId == null || correlationId.isBlank()) {
			correlationId = UUID.randomUUID().toString();
		}

		final String finalCorrelationId = correlationId;

		ServerHttpRequest mutatedRequest = request.mutate()
				.header(CORRELATION_ID_HEADER, finalCorrelationId)
				.build();

		ServerHttpResponse response = exchange.getResponse();
		response.getHeaders().set(CORRELATION_ID_HEADER, finalCorrelationId);

		ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
		mutatedExchange.getAttributes().put(CORRELATION_ID_ATTRIBUTE, finalCorrelationId);

		return chain.filter(mutatedExchange);
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
