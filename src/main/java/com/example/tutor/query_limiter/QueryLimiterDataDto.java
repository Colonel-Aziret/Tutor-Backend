package com.example.tutor.query_limiter;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class QueryLimiterDataDto {
    private long startTime;
    private AtomicInteger counter;

    public QueryLimiterDataDto(long startTime, AtomicInteger counter) {
        this.startTime = startTime;
        this.counter = counter;
    }
}
