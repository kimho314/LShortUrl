package com.example.lshorturl.utils;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class NanoIdTest {
    @Test
    void createNanoIdTest(){
        String nanoId = NanoIdUtils.randomNanoId();
        log.info("nanoId : {}", nanoId);
        Assertions.assertNotNull(nanoId);
    }
}
