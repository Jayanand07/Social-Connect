package com.example.entity;

/**
 * Entity class for tracking API usage.
 */
public class ApiUsageIndex {
    private String apiEndpoint;
    private int requestCount;
    private long timestamp;

    public ApiUsageIndex(String apiEndpoint, int requestCount, long timestamp) {
        this.apiEndpoint = apiEndpoint;
        this.requestCount = requestCount;
        this.timestamp = timestamp;
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}