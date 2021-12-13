package com.example.demo.Controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Controller
public class HomeController {


    final String API_GREETING_POST = "localhost:9090/friendship";

    @PostMapping("/sendPostGreeting")
    public String sendPostGreeting(Model model){


       WebClient webClient = WebClient.builder()
                .baseUrl(API_GREETING_POST)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String request ="\"Hi from client\"";
        String key = "\"request\"";



            String response = webClient.post()
                .body(Mono.just("{"+ key + ":"+ request +"}"), String.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();


        System.out.println(response);
        model.addAttribute("greetings", response);
        return "index";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
}

