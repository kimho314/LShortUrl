package com.example.lshorturl.utils;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import io.seruco.encoding.base62.Base62;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class Base62Test {
    private final Base62 standardEncoder = Base62.createInstance();

    @Test
    void encodeTest(){
        String uuid = NanoIdUtils.randomNanoId();;
        log.info("uuid : {}", uuid);

        byte[] encode = standardEncoder.encode(uuid.getBytes(StandardCharsets.UTF_8));
        log.info("encode: {}", new String(encode, StandardCharsets.UTF_8));
        byte[] decode = standardEncoder.decode(encode);
        String decodedStr  = new String(decode, StandardCharsets.UTF_8);
        log.info("decoded : {}", decodedStr);

        Assertions.assertEquals(uuid, decodedStr);
    }
}
