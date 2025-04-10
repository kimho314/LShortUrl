package com.example.sayhello.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class GreetingController {
    @GetMapping("/greeting")
    public String greet(){
        log.info("access greeting");

        List<String> greetings = Arrays.asList("Hi there", "Greetings", "Salutations");
        Random random = new Random();

        int randomNum = random.nextInt(greetings.size());
        return greetings.get(randomNum);
    }
}
