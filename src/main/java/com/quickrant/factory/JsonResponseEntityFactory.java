package com.quickrant.factory;

import com.quickrant.http.ResponseStatus;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.apache.log4j.Logger;

public class JsonResponseEntityFactory {

    private static Logger log = Logger.getLogger(JsonResponseEntityFactory.class);

    protected ResponseEntity getResponseEntity(HttpStatus httpStatus, ResponseStatus responseStatus, String message, Object data, HttpHeaders headers) {

        if (httpStatus == null) throw new IllegalArgumentException("HttpStatus cannot be null");
        if (responseStatus == null) throw new IllegalArgumentException("ResponseStatus cannot be null");

        JsonResponse jsonResponse = new JsonResponse(responseStatus, message, data);
        return new ResponseEntity(jsonResponse, headers, httpStatus);
    }

    /**
     * This gets unpacked on the front-end
     */
    private class JsonResponse {

        private ResponseStatus status;
        private String message;
        private Object data;

        public JsonResponse(ResponseStatus status, String message, Object data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }

        public ResponseStatus getStatus() {
            return status;
        }

        public void setStatus(ResponseStatus status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

    }

}
