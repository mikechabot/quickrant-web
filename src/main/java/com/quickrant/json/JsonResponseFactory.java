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

    public class JsonResponse {

        private boolean success;
        private String message;
        private Object data;

        public JsonResponse() { }

        public JsonResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
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

        @Override
        public String toString() {
            return "JsonResponse{" +
                    "success=" + success +
                    ", message='" + message + '\'' +
                    ", data=" + data +
                    '}';
        }
    }

}

