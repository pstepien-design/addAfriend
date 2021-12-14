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

    public String request(String url, String method, String sourceEmail, String destinationEmail) {
        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();


        String[] email1 = sourceEmail.split("");
        String[] email2 = destinationEmail.split("");
        String srcMail= "";
        String srcHost= "";
        String dstHost= "";
        String dstMail= "";
        for( int i=0; i<email1.length; i++){
            int at = Arrays.asList(email1).indexOf("@");
            if(i< at){
                srcMail += (email1[i]);
            }
            else if(i> at){
                srcHost += (email1[i]);
            }
        }
        for( int i=0; i<email2.length; i++){
            int at = Arrays.asList(email2).indexOf("@");
            if(i< at){
                dstMail += (email2[i]);
            }
            else if(i> at){
                dstHost += (email2[i]);
            }
        }

        String requestDetails = method+" "+srcMail+" "+ srcHost +" "+dstMail+" "+ dstHost+" "+1;
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
