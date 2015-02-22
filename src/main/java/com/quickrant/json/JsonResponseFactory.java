package com.quickrant.json;

import org.springframework.stereotype.Component;

@Component
public class JsonResponseFactory {

    public JsonResponse success(String message, Object data) {
        return new JsonResponse(true, message, data);
    }

    public JsonResponse failure(String message, Object data) {
        return new JsonResponse(false, message, data);
    }
}
