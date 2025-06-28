package com.example.tutor.query_limiter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class QueryLimiter {
    @Value("${maxRequestsPerIp}")
    private int maxRequestsPerIp;

    @Value("${timeInSeconds}")
    private long timeInSeconds;
    private final ConcurrentHashMap<String, QueryLimiterDataDto> requestMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    public boolean isAllowed(String ipAddress, String methodName) {
        long currentTime = System.currentTimeMillis();

        // Уникальный ключ для IP и метода
        String key = ipAddress + ":" + methodName;

        // Получаем или создаем блокировку для ключа
        ReentrantLock lock = lockMap.computeIfAbsent(key, k -> new ReentrantLock());

        lock.lock();
        try {
            // Получаем или создаем данные для ключа
            QueryLimiterDataDto requestData = requestMap.computeIfAbsent(key,
                    k -> new QueryLimiterDataDto(currentTime, new AtomicInteger(0)));

            // Проверяем истечение временного окна
            if (currentTime - requestData.getStartTime() > timeInSeconds) {
                requestData.setStartTime(currentTime); // Сбрасываем окно
                requestData.getCounter().set(1); // Сбрасываем счетчик
                return true;
            }

            // Проверяем, не превышен ли лимит запросов
            return requestData.getCounter().incrementAndGet() <= maxRequestsPerIp;
        } finally {
            lock.unlock();
        }
    }
}
