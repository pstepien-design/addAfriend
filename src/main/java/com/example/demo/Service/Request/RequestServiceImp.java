package com.example.demo.Service.Request;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.security.auth.message.callback.SecretKeyCallback;
import java.util.Arrays;

@Service
public class RequestServiceImp implements RequestService {
    @Override

    public String request(String url, String method, String sourceEmail, String destinationEmail, String srcHost, String dstHost) {
        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();




        String requestDetails = method+" "+sourceEmail+" "+ srcHost +" "+destinationEmail+" "+ dstHost+" "+1;
        String key = "\"request\"";
        String requestBody ="\""+requestDetails+"\"";
        String request ="{"+ key + ":"+ requestBody +"}";

        String response = webClient.post()
                .body(Mono.just(request), String.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;

    }
}
