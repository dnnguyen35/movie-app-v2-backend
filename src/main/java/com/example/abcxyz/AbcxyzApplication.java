package com.example.abcxyz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@SpringBootApplication
@RestController
public class AbcxyzApplication {

    @GetMapping("/check")
    public String check() {
        return String.format("Everything still ok - %s", Instant.now().toString());
    }

    public static void main(String[] args) {
        SpringApplication.run(AbcxyzApplication.class, args);
    }

}
