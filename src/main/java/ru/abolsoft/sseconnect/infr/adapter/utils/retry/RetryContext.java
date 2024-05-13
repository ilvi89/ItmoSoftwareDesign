package ru.abolsoft.sseconnect.infr.adapter.utils.retry;

import lombok.Data;

@Data
public class RetryContext {
    private int maxAttempts;
    private long delay;
    private double multiplier;

    public RetryContext(int maxAttempts, long delay, double multiplier) {
        this.maxAttempts = maxAttempts;
        this.delay = delay;
        this.multiplier = multiplier;
    }
}
