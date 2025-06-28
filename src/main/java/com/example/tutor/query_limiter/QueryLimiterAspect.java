package com.example.tutor.query_limiter;

import com.example.tutor.controller.BaseController;
import com.example.tutor.exception.QueryLimitExceededException;
import jakarta.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class QueryLimiterAspect {
    private final QueryLimiter queryLimiter;
    private final HttpServletRequest request;
    private final BaseController baseController;

    public QueryLimiterAspect(QueryLimiter queryLimiter, HttpServletRequest request, BaseController baseController) {
        this.queryLimiter = queryLimiter;
        this.request = request;
        this.baseController = baseController;
    }

    @Before("@annotation(QueryLimited)")
    public void checkRateLimit(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String clientIp = baseController.tryDetectRemoteClientIp(request);

        if (!queryLimiter.isAllowed(clientIp, methodName)) {
            throw new QueryLimitExceededException("error.too.many.request");
        }
    }
}
