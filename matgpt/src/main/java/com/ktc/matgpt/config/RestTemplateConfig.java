package com.ktc.matgpt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Profile({"dev", "test"})
@Configuration
public class RestTemplateConfig {

    public static final String API_URL = "https://api.openai.com/v1/chat/completions";
    public static final Duration API_RESPONSE_TIMEOUT = Duration.ofSeconds(60);

    @Value("${chatgpt.api.key}")
    private String chatGptApiKey;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(API_RESPONSE_TIMEOUT) // 연결 타임아웃 설정
                .setReadTimeout(API_RESPONSE_TIMEOUT) // 읽기 타임아웃 설정

                .additionalInterceptors((request, body, execution) -> {
                    HttpHeaders headers = request.getHeaders();
                    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + chatGptApiKey);
                    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    return execution.execute(request, body);
                })
                .build();
    }
}
