package com.example.abcxyz.configuration;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Configuration
@Slf4j
public class TmdbConfig {

    @Value("${tmdb.base-url}")
    private String tmdbBaseUrl;

    @Value("${tmdb.api-key}")
    private String tmdbApiKey;

    @Bean
    public RestClient tmdbRestClient() {

        RestClient tmdbRestCLient = RestClient.builder()
                .baseUrl(this.tmdbBaseUrl)
                .requestInterceptor((request, body, execution) -> {
                    URI newUriWithApiKey = UriComponentsBuilder
                            .fromUri(request.getURI())
                            .queryParam("api_key", this.tmdbApiKey)
                            .build(true)
                            .toUri();

                    HttpRequest requestWithApiKey = new HttpRequestWrapper(request) {
                        @Override
                        @NonNull
                        public URI getURI() {
                            return newUriWithApiKey;
                        }
                    };

                    log.info("TmdbRestClient request: {}", requestWithApiKey.getURI().toURL().toString());

                    return execution.execute(requestWithApiKey, body);
                })
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                    log.warn("Tmdb service error: {}", response.getStatusText());
                    throw new RuntimeException("Tmdb service error");
                })
                .build();

        log.info("TmdbRestClient created successfully");

        return tmdbRestCLient;
    }
}
