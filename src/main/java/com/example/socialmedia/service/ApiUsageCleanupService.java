package com.example.socialmedia.service;

import com.example.socialmedia.repository.ApiUsageLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Scheduled service for API usage index maintenance.
 *
 * - Cleans up old usage logs to prevent database bloat
 * - Logs summary statistics periodically
 *
 * This runs automatically via @EnableScheduling on the main application class.
 */
@Service
public class ApiUsageCleanupService {

    private static final Logger log = LoggerFactory.getLogger(ApiUsageCleanupService.class);

    private final ApiUsageLogRepository usageLogRepository;

    public ApiUsageCleanupService(ApiUsageLogRepository usageLogRepository) {
        this.usageLogRepository = usageLogRepository;
    }

    /**
     * Cleanup old API usage records (older than 30 days).
     * Runs every day at 2 AM.
     */
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void cleanupOldUsageLogs() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
        int deleted = usageLogRepository.deleteOlderThan(cutoff);
        log.info("API usage cleanup: deleted {} records older than {}", deleted, cutoff);
    }

    /**
     * Log summary stats every hour for observability.
     */
    @Scheduled(fixedRate = 3600000) // every hour
    public void logUsageSummary() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        LocalDateTime now = LocalDateTime.now();

        long totalRequests = usageLogRepository.countByTimestampBetween(oneHourAgo, now);
        log.info("API usage last hour: {} total requests", totalRequests);

        var slowRequests = usageLogRepository
                .findByResponseTimeMsGreaterThanOrderByResponseTimeMsDesc(5000);
        if (!slowRequests.isEmpty()) {
            log.warn("Found {} slow API calls (>5s) in last check", slowRequests.size());
        }
    }
}
