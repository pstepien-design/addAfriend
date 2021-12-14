package com.example.demo.Service.Request;

import org.springframework.stereotype.Service;


public interface RequestService{

    public String request(String url, String method, String sourceEmail, String destinationEmail);

}
