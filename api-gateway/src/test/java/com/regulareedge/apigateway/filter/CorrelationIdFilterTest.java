package com.regulareedge.apigateway.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

class CorrelationIdFilterTest {

	private final CorrelationIdFilter filter = new CorrelationIdFilter();

	@Test
	void addsCorrelationIdWhenAbsent() {
		MockServerHttpRequest request = MockServerHttpRequest.get("/notifications/1").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		GatewayFilterChain chain = mock(GatewayFilterChain.class);
		ArgumentCaptor<ServerWebExchange> exchangeCaptor = ArgumentCaptor.forClass(ServerWebExchange.class);
		when(chain.filter(exchangeCaptor.capture())).thenReturn(Mono.empty());

		filter.filter(exchange, chain).block();

		ServerWebExchange downstreamExchange = exchangeCaptor.getValue();
		String downstreamHeader = downstreamExchange.getRequest()
				.getHeaders()
				.getFirst(CorrelationIdFilter.CORRELATION_ID_HEADER);

		assertThat(downstreamHeader).isNotNull().isNotBlank();
		assertThat(exchange.getResponse().getHeaders().getFirst(CorrelationIdFilter.CORRELATION_ID_HEADER))
				.isEqualTo(downstreamHeader);
	}

	@Test
	void preservesExistingCorrelationId() {
		String existingId = "existing-correlation-id";
		MockServerHttpRequest request = MockServerHttpRequest.get("/notifications/1")
				.header(CorrelationIdFilter.CORRELATION_ID_HEADER, existingId)
				.build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		GatewayFilterChain chain = mock(GatewayFilterChain.class);
		ArgumentCaptor<ServerWebExchange> exchangeCaptor = ArgumentCaptor.forClass(ServerWebExchange.class);
		when(chain.filter(exchangeCaptor.capture())).thenReturn(Mono.empty());

		filter.filter(exchange, chain).block();

		ServerWebExchange downstreamExchange = exchangeCaptor.getValue();
		String downstreamHeader = downstreamExchange.getRequest()
				.getHeaders()
				.getFirst(CorrelationIdFilter.CORRELATION_ID_HEADER);

		assertThat(downstreamHeader).isEqualTo(existingId);
		assertThat(exchange.getResponse().getHeaders().getFirst(CorrelationIdFilter.CORRELATION_ID_HEADER))
				.isEqualTo(existingId);
	}
}
