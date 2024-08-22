package com.wefood.front.config;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * class: GptConfig.
 *
 * @author JBumLee
 * @version 2024/08/21
 */
@Configuration
public class GptConfig {

    @Value("${gpt.api-key}")
    private String apiKey;

    @Bean(name = "gptRestTemplate")
    public RestTemplate restTemplate(){
        RestTemplate template = new RestTemplate();
        template.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add(
                "Authorization"
                ,"Bearer " + apiKey);
            request.getHeaders().setContentType(APPLICATION_JSON);
            return execution.execute(request, body);
        });

        return template;
    }
}