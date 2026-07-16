package com.regulareedge.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * Logs method, path, correlation id and total execution time for every
 * request that passes through the gateway. Runs last so that the timing
 * captured reflects the complete filter chain (including the JWT check).
 */
@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

	private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		long startTime = System.currentTimeMillis();

		return chain.filter(exchange)
				.doFinally(signalType -> {
					long durationMs = System.currentTimeMillis() - startTime;
					String method = exchange.getRequest().getMethod() != null
							? exchange.getRequest().getMethod().name()
							: "UNKNOWN";
					String path = exchange.getRequest().getURI().getPath();
					String correlationId = resolveCorrelationId(exchange);
					int statusCode = exchange.getResponse().getStatusCode() != null
							? exchange.getResponse().getStatusCode().value()
							: -1;

					log.info("method={} path={} correlationId={} status={} durationMs={}",
							method, path, correlationId, statusCode, durationMs);
				});
	}

	private String resolveCorrelationId(ServerWebExchange exchange) {
		Object attribute = exchange.getAttributes().get(CorrelationIdFilter.CORRELATION_ID_ATTRIBUTE);
		if (attribute != null) {
			return attribute.toString();
		}
		String header = exchange.getRequest().getHeaders().getFirst(CorrelationIdFilter.CORRELATION_ID_HEADER);
		return header != null ? header : "unknown";
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}
}
