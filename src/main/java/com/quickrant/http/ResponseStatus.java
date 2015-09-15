package com.quickrant.http;

public enum ResponseStatus {

    SUCCESS,    // Both the HTTP and business request were successful
    FAIL,       // The HTTP request was successful, but the business request hit a failure condition (e.g. failed validation)
    ERROR       // Either the HTTP or business request hit an error condition (e.g. MongoClientException, 401 Bad Request)

}
