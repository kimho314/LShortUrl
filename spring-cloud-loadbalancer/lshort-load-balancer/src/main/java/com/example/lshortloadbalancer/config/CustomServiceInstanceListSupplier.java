package com.example.lshortloadbalancer.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

public class CustomServiceInstanceListSupplier implements ServiceInstanceListSupplier {

    private final String serviceId;

    public CustomServiceInstanceListSupplier(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String getServiceId() {
        return this.serviceId;
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return Flux.just(
            Arrays.asList(new DefaultServiceInstance(serviceId + "1", serviceId, "localhost", 8080, false))
//                new DefaultServiceInstance(serviceId + "2", serviceId, "127.0.0.1", 8081, false))
        );
    }
}
