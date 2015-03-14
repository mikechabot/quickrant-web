package com.quickrant.util;

public class JsonResponse {

    private ResponseStatus status;
    private String message;
    private Object data;

    public JsonResponse(ResponseStatus status, String message, Object data) {
        if (status == null) throw new IllegalArgumentException("Status cannot be null");
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