package com.quickrant.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseEntityFactory {

    @Autowired
    JsonResponseFactory jsonResponseFactory;

    public ResponseEntity ok(String message, Object data) {
        JsonResponse json = jsonResponseFactory.success(message, data);
        return new ResponseEntity(json, null, HttpStatus.OK);
    }

    public ResponseEntity ok(String message) {
        return ok(message, null);
    }

    public ResponseEntity created(String message, Object data, HttpHeaders headers) {
        JsonResponse json = jsonResponseFactory.success(message, data);
        return new ResponseEntity(json, headers, HttpStatus.CREATED);
    }

    public ResponseEntity created(String message, HttpHeaders headers) {
       return created(message, null, headers);
    }

    public ResponseEntity badRequest(String message, Object data) {
        JsonResponse json = jsonResponseFactory.failure(message, data);
        return new ResponseEntity(json, null, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity badRequest(String message) {
        return badRequest(message, null);
    }

}
