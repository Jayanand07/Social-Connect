package com.example.interceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
@Slf4j
@Component
public class ApiMetricsInterceptor implements HandlerInterceptor {
    private final MeterRegistry meterRegistry;
    private static final String START_TIME = "startTime";
    public ApiMetricsInterceptor(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_TIME, System.currentTimeMillis());
        meterRegistry.counter("api.requests.total", "endpoint", request.getRequestURI(), "method", request.getMethod()).increment();
        log.debug("API Request: {} {}", request.getMethod(), request.getRequestURI());
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            long startTime = (Long) request.getAttribute(START_TIME);
            long duration = System.currentTimeMillis() - startTime;
            String endpoint = request.getRequestURI();
            String method = request.getMethod();
            int statusCode = response.getStatus();
            Timer.builder("api.response.time")
                .description("API response time")
                .tag("endpoint", endpoint)
                .tag("method", method)
                .tag("status", String.valueOf(statusCode))
                .register(meterRegistry)
                .record(duration, java.util.concurrent.TimeUnit.MILLISECONDS);
            if (statusCode >= 400) {
                meterRegistry.counter("api.errors.total", "endpoint", endpoint, "status", String.valueOf(statusCode)).increment();
                log.warn("API Error: {} {} - Status: {}", method, endpoint, statusCode);
            }
            if (statusCode == 429) {
                meterRegistry.counter("api.ratelimit.exceeded", "endpoint", endpoint).increment();
                log.warn("Rate limit exceeded for endpoint: {}", endpoint);
            }
            if (ex != null) {
                meterRegistry.counter("api.exceptions.total", "exception", ex.getClass().getSimpleName(), "endpoint", endpoint).increment();
                log.error("API Exception: {} {} - {}", method, endpoint, ex.getMessage());
            }
        } catch (Exception e) {
            log.error("Error in ApiMetricsInterceptor.afterCompletion", e);
        }
    }
}