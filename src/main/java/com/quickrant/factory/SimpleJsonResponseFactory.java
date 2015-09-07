package com.quickrant.factory;


import com.quickrant.util.ResponseStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class SimpleJsonResponseFactory extends JsonResponseEntityFactory {

    public ResponseEntity success() {
        return getResponseEntity(HttpStatus.OK, ResponseStatus.SUCCESS, null, null, null);
    }

    public ResponseEntity success (String message) {
        return getResponseEntity(HttpStatus.OK, ResponseStatus.SUCCESS, message, null, null);
    }

    public ResponseEntity success (String message, Object data) {
        return getResponseEntity(HttpStatus.OK, ResponseStatus.SUCCESS, message, data, null);
    }

    public ResponseEntity success (String message, Object data, HttpHeaders headers) {
        return getResponseEntity(HttpStatus.OK, ResponseStatus.SUCCESS, message, data, headers);
    }

    public ResponseEntity fail (String message) {
        return getResponseEntity(HttpStatus.OK, ResponseStatus.FAIL, message, null, null);
    }

    public ResponseEntity error (String message) {
        return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ResponseStatus.ERROR, message, null, null);
    }

}
