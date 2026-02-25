import net.jodah.expiringmap.ExpiringMap;
import net.jodah.expiringmap.ExpirationPolicy;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RateLimitingInterceptor extends OncePerRequestFilter {
    private final ExpiringMap<String, Integer> requestCounts = ExpiringMap.builder()
            .expirationPolicy(ExpirationPolicy.CREATED)
            .variableExpiration()
            .build();
    private final long requestLimit = 100; // limit of requests
    private final Duration limitDuration = Duration.ofMinutes(1); // duration for which the limit is applied

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();
        int requestCount = requestCounts.getOrDefault(clientIp, 0);

        if (requestCount >= requestLimit) {
            throw new RateLimitExceededException();
        }

        requestCounts.put(clientIp, requestCount + 1, limitDuration.toMillis(), TimeUnit.MILLISECONDS);
        filterChain.doFilter(request, response);
    }

    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public static class RateLimitExceededException extends RuntimeException {
        public RateLimitExceededException() {
            super("Rate limit exceeded");
        }
    }
}