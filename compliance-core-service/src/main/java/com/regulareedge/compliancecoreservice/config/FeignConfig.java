package com.regulareedge.compliancecoreservice.config;

import com.regulareedge.compliancecoreservice.common.constants.AppConstants;
import com.regulareedge.compliancecoreservice.common.constants.SecurityConstants;
import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

    /**
     * Propagates the incoming Authorization header and correlation id onto outgoing
     * Feign calls so downstream services (e.g. auth-service) see the same caller
     * context and requests remain traceable end-to-end.
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();

                String authHeader = request.getHeader(SecurityConstants.HEADER_NAME);
                if (authHeader != null) {
                    requestTemplate.header(SecurityConstants.HEADER_NAME, authHeader);
                }

                String correlationId = request.getHeader(AppConstants.CORRELATION_ID_HEADER);
                if (correlationId != null) {
                    requestTemplate.header(AppConstants.CORRELATION_ID_HEADER, correlationId);
                }
            }
        };
    }
}
