package com.regulareedge.apigateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Binds the shared JWT configuration (same values used by every business
 * service) so the gateway can validate incoming bearer tokens without
 * performing full Spring Security authentication.
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

	/**
	 * Shared HMAC secret used to sign/verify JWTs across all RegulareEdge
	 * services.
	 */
	private String secret;

	/**
	 * Access token expiration, in milliseconds (informational here — the
	 * gateway only validates the signature/expiry embedded in the token).
	 */
	private long accessTokenExpirationMs;

	/**
	 * Refresh token expiration, in milliseconds.
	 */
	private long refreshTokenExpirationMs;

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public long getAccessTokenExpirationMs() {
		return accessTokenExpirationMs;
	}

	public void setAccessTokenExpirationMs(long accessTokenExpirationMs) {
		this.accessTokenExpirationMs = accessTokenExpirationMs;
	}

	public long getRefreshTokenExpirationMs() {
		return refreshTokenExpirationMs;
	}

	public void setRefreshTokenExpirationMs(long refreshTokenExpirationMs) {
		this.refreshTokenExpirationMs = refreshTokenExpirationMs;
	}
}
