package com.quickrant.factory;

import com.quickrant.util.JsonResponse;
import com.quickrant.util.ResponseStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class ResponseEntityFactory {

    public ResponseEntity ok() {
        return generate(HttpStatus.OK, ResponseStatus.SUCCESS, null, null, null);
    }

    public ResponseEntity ok(String message, Object data) {
        return generate(HttpStatus.OK, ResponseStatus.SUCCESS, null, message, data);
    }

    public ResponseEntity badRequest(String message) {
        return generate(HttpStatus.BAD_REQUEST, ResponseStatus.FAIL, null, message, null);
    }

    public ResponseEntity created(String message, String location) {
        return generate(HttpStatus.CREATED, ResponseStatus.SUCCESS, getLocationHeader(location), message, null);
    }

    public ResponseEntity generate(HttpStatus httpStatus, ResponseStatus responseStatus, HttpHeaders headers, String message, Object data) {
        if (httpStatus == null) throw new IllegalArgumentException("HttpStatus cannot be null");
        if (responseStatus == null) throw new IllegalArgumentException("JsonResponseStatus cannot be null");

        if (httpStatus == HttpStatus.CREATED) {
            if (responseStatus != ResponseStatus.SUCCESS) throw new IllegalArgumentException("Cannot have an unsuccessful creation event");
            if (headers == null || headers.getLocation() == null) throw new IllegalArgumentException("HTTP Created (201) requires a Location header");
        }

        JsonResponse json = getJsonResponse(responseStatus, message, data);
        ResponseEntity responseEntity = getResponseEntity(httpStatus, headers, json);

        return responseEntity;

    }

    public ResponseEntity getResponseEntity(HttpStatus status, HttpHeaders headers, Object data) {
        return new ResponseEntity(data, headers, status);
    }

    public JsonResponse getJsonResponse(ResponseStatus responseStatus, String message, Object data) {
        return new JsonResponse(responseStatus, message, data);
    }

    private HttpHeaders getLocationHeader(String location) {
        HttpHeaders header = new HttpHeaders();
        header.setLocation(URI.create(location));
        return header;
    }

}
