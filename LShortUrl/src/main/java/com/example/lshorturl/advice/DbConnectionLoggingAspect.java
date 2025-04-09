package com.example.lshorturl.advice;

import com.example.lshorturl.utils.DataSourceUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DbConnectionLoggingAspect {
    private final DataSourceUtil dataSourceUtil;

    public DbConnectionLoggingAspect(DataSourceUtil dataSourceUtil) {
        this.dataSourceUtil = dataSourceUtil;
    }

    @Around("execution(* com.example.lshorturl.service.*.*(..))")
    public Object dbConnectionLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        dataSourceUtil.log();

        Object retVal = joinPoint.proceed();

        return retVal;
    }
}
