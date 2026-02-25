package com.example.socialmedia.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * JPA Entity for tracking every API call.
 *
 * Answers the questions:
 * - Which module / endpoint is being hit?
 * - Who is calling it?
 * - How long is it taking?
 * - Is it failing?
 *
 * Database indexes on userId, endpoint, and timestamp ensure
 * fast query performance for dashboards and alerting.
 */
@Entity
@Table(name = "api_usage_log", indexes = {
        @Index(name = "idx_api_usage_user", columnList = "userId"),
        @Index(name = "idx_api_usage_endpoint", columnList = "endpoint"),
        @Index(name = "idx_api_usage_timestamp", columnList = "timestamp")
})
public class ApiUsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The authenticated user who made the request (null for anonymous) */
    @Column(name = "userId")
    private String userId;

    /** HTTP method: GET, POST, PUT, DELETE */
    @Column(nullable = false, length = 10)
    private String method;

    /** The API endpoint path, e.g. /api/posts, /api/chat/send */
    @Column(nullable = false, length = 500)
    private String endpoint;

    /** HTTP response status code */
    private int statusCode;

    /** Response time in milliseconds */
    private long responseTimeMs;

    /** Client IP address */
    @Column(length = 45)
    private String clientIp;

    /** When the request was made */
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    /** Error message if the request failed */
    @Column(length = 2000)
    private String errorMessage;

    public ApiUsageLog() {
    }

    public ApiUsageLog(String userId, String method, String endpoint,
            int statusCode, long responseTimeMs, String clientIp) {
        this.userId = userId;
        this.method = method;
        this.endpoint = endpoint;
        this.statusCode = statusCode;
        this.responseTimeMs = responseTimeMs;
        this.clientIp = clientIp;
        this.timestamp = LocalDateTime.now();
    }

    // ===== Getters & Setters =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public long getResponseTimeMs() {
        return responseTimeMs;
    }

    public void setResponseTimeMs(long responseTimeMs) {
        this.responseTimeMs = responseTimeMs;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
