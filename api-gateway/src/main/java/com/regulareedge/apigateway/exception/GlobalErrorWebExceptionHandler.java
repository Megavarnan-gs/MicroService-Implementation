package com.regulareedge.apigateway.exception;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

/**
 * Replaces WebFlux's default whitelabel error page with a consistent JSON
 * error body for any unhandled exception raised while routing a request
 * through the gateway.
 */
@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler extends DefaultErrorAttributes implements WebExceptionHandler {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		storeErrorInformation(ex, exchange);

		ServerRequest serverRequest = ServerRequest.create(exchange, java.util.List.of());
		Map<String, Object> errorAttributes = getErrorAttributes(serverRequest, ErrorAttributeOptions.defaults());

		HttpStatus status = resolveStatus(errorAttributes);

		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(status);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", Instant.now().toString());
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", errorAttributes.getOrDefault("message", ex.getMessage()));
		body.put("path", exchange.getRequest().getURI().getPath());

		byte[] bytes;
		try {
			bytes = objectMapper.writeValueAsBytes(body);
		} catch (Exception e) {
			bytes = "{\"error\":\"Internal Server Error\"}".getBytes(StandardCharsets.UTF_8);
		}

		DataBuffer buffer = response.bufferFactory().wrap(bytes);
		return response.writeWith(Mono.just(buffer));
	}

	private HttpStatus resolveStatus(Map<String, Object> errorAttributes) {
		Object statusAttr = errorAttributes.get("status");
		if (statusAttr instanceof Integer statusCode) {
			HttpStatus resolved = HttpStatus.resolve(statusCode);
			if (resolved != null) {
				return resolved;
			}
		}
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
}
