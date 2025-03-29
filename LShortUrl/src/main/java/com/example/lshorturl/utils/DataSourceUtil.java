package com.example.lshorturl.utils;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataSourceUtil {

    private final HikariDataSource dataSource;

    public DataSourceUtil(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void log() {
        HikariPoolMXBean hikariPoolMXBean = dataSource.getHikariPoolMXBean();
        if (hikariPoolMXBean != null) {
            log.info("Active Connections: " + hikariPoolMXBean.getActiveConnections());
            log.info("Total Connections: " + hikariPoolMXBean.getTotalConnections());
            log.info("Idle Connections: " + hikariPoolMXBean.getIdleConnections());
        }
    }
}
