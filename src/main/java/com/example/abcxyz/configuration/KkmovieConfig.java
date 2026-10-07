package com.example.abcxyz.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

@Configuration
@Slf4j
public class KkmovieConfig {

    @Value("${kkmovie.base-url}")
    private String kkmovieBaseUrl;

    @Bean
    public RestClient kkmovieRestClient() {
        RestClient kkmovieRestClient = RestClient.builder()
                .baseUrl(this.kkmovieBaseUrl)
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                    log.error("Kkmovie service error: {}", response.getStatusText());
                    throw new RuntimeException("Kkmovie service error");
                })
                .build();

        log.info("KkmovieRestClient created successfully");

        return kkmovieRestClient;
    }
}
