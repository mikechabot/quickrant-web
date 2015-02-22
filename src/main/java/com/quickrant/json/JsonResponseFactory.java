package com.quickrant.json;

public class JsonResponseFactory {

    public static JsonResponse success(String message, Object data) {
        return new JsonResponse(true, message, data);
    }

    public static JsonResponse failure(String message, Object data) {
        return new JsonResponse(false, message, data);
    }

}
