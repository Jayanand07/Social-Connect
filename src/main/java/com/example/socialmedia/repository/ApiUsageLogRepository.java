package com.example.socialmedia.repository;

import com.example.socialmedia.entity.ApiUsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA Repository for API usage tracking.
 *
 * Provides queries to answer:
 * - How many requests hit each endpoint?
 * - Who are the heaviest API users?
 * - Which endpoints are slow?
 * - What's failing?
 */
public interface ApiUsageLogRepository extends JpaRepository<ApiUsageLog, Long> {

    /** Find all usage records for a specific user */
    List<ApiUsageLog> findByUserIdOrderByTimestampDesc(String userId);

    /** Find all usage records for a specific endpoint */
    List<ApiUsageLog> findByEndpointOrderByTimestampDesc(String endpoint);

    /** Count total requests in a time window */
    long countByTimestampBetween(LocalDateTime start, LocalDateTime end);

    /** Count requests per user in a time window */
    @Query("SELECT a.userId, COUNT(a) FROM ApiUsageLog a " +
            "WHERE a.timestamp BETWEEN :start AND :end " +
            "GROUP BY a.userId ORDER BY COUNT(a) DESC")
    List<Object[]> countRequestsByUserInWindow(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    /** Find slow requests (response time above threshold) */
    List<ApiUsageLog> findByResponseTimeMsGreaterThanOrderByResponseTimeMsDesc(long thresholdMs);

    /** Find failed requests (status >= 400) */
    List<ApiUsageLog> findByStatusCodeGreaterThanEqualOrderByTimestampDesc(int statusCode);

    /** Average response time per endpoint */
    @Query("SELECT a.endpoint, AVG(a.responseTimeMs) FROM ApiUsageLog a " +
            "WHERE a.timestamp >= :since " +
            "GROUP BY a.endpoint ORDER BY AVG(a.responseTimeMs) DESC")
    List<Object[]> averageResponseTimeByEndpoint(@Param("since") LocalDateTime since);

    /** Clean up old records to prevent table bloat */
    @Modifying
    @Query("DELETE FROM ApiUsageLog a WHERE a.timestamp < :cutoff")
    int deleteOlderThan(@Param("cutoff") LocalDateTime cutoff);
}
