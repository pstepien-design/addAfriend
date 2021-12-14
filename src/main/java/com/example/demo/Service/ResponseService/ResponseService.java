package com.example.demo.Service.ResponseService;

import org.springframework.http.HttpStatus;

public interface ResponseService {

    public String createResponse(HttpStatus httpStatus, String phrase);
}
