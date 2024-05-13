package ru.abolsoft.sseconnect.infr.adapter.utils.retry;

public interface RetryHandler {
    void executeAfterFirstRetry();
    void executeBeforeRetry();
    void executeAfterRetry();
}


