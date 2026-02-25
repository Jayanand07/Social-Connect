package com.example.repository;

import java.sql.Timestamp;
import java.util.List;

public interface ApiUsageIndexRepository {

    List<UsageRecord> findUsageRecordsByUser(String userId);

    List<UsageRecord> findUsageRecordsByEndpoint(String endpoint);

    List<UsageRecord> findUsageRecordsByTimestamp(Timestamp timestamp);

    long countRequestsInTimeWindow(Timestamp start, Timestamp end);

    double getAverageResponseTime(String endpoint);

    List<UsageRecord> findSlowRequests(long threshold);

    void deleteOldRecords(Timestamp timestamp);
}