package com.example.curso_api_rest_java.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(String mesage){
        super(mesage);

    }
}
