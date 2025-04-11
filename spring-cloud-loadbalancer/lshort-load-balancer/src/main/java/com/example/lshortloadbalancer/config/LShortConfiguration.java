package com.example.lshortloadbalancer.config;

import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class LShortConfiguration {

    @Bean
    @Primary
    public ServiceInstanceListSupplier serviceInstanceListSupplier() {
        return new CustomServiceInstanceListSupplier("LShortUrl");
    }
}
