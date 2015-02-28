package com.quickrant.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class ResponseEntityFactory {

    @Autowired
    JsonResponseFactory jsonResponseFactory;

    public ResponseEntity ok(String message, Object data) {
        JsonResponseFactory.JsonResponse json = jsonResponseFactory.success(message, data);
        return new ResponseEntity(json, null, HttpStatus.OK);
    }

    public ResponseEntity ok(String message) {
        return ok(message, null);
    }

    public ResponseEntity created(String message, Object data, URI location) {
        JsonResponseFactory.JsonResponse json = jsonResponseFactory.success(message, data);
        HttpHeaders header = new HttpHeaders();
        header.setLocation(location);
        return new ResponseEntity(json, header, HttpStatus.CREATED);
    }

    public ResponseEntity badRequest(String message) {
        JsonResponseFactory.JsonResponse json = jsonResponseFactory.failure(message, null);
        return new ResponseEntity(json, null, HttpStatus.BAD_REQUEST);
    }

}
