package com.example.demo.Service.ResponseService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ResponseServiceImp implements ResponseService {
    @Override
    public String createResponse(HttpStatus httpStatus, String phrase) {
        String key = "\"response\"";
        String requestDetails = "1 "+httpStatus+" "+phrase+" ";
        String requestBody ="\""+requestDetails+"\"";

       return "{"+ key + ":"+ requestBody +"}";
    }
}
