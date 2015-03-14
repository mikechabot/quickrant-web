package com.quickrant.util;

public enum ResponseStatus {

    SUCCESS,
    ERROR,
    FAIL;

    public String toString(ResponseStatus status) {
        return status.toString().toLowerCase();
    }

}
