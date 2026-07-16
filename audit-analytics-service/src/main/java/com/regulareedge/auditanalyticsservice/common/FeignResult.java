package com.regulareedge.auditanalyticsservice.common;

/**
 * Wraps the outcome of a best-effort call to a downstream microservice. {@code degraded}
 * is true when the resilience4j circuit-breaker fallback was invoked (the real call
 * failed, timed out, or the circuit was open) and {@code data} is therefore a fail-open
 * default rather than a verified upstream response. Callers use this to propagate a
 * "degraded" flag onto aggregated responses (e.g. CcoDashboardResponse) instead of
 * silently treating fallback data as authoritative.
 */
public final class FeignResult<T> {

    private final T data;
    private final boolean degraded;

    private FeignResult(T data, boolean degraded) {
        this.data = data;
        this.degraded = degraded;
    }

    public static <T> FeignResult<T> ok(T data) {
        return new FeignResult<>(data, false);
    }

    public static <T> FeignResult<T> degraded(T data) {
        return new FeignResult<>(data, true);
    }

    public T getData() {
        return data;
    }

    public boolean isDegraded() {
        return degraded;
    }
}
