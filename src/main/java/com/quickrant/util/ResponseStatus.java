package com.quickrant.util;

/**
 * ResponseStatus objects are sent back to the client along with
 * an HTTP response status (e.g. 200, 403, 500).
 *
 * This object in used in JavaScript to determine whether the request
 * was successful or not.
 *
 */
public enum ResponseStatus {

    SUCCESS,    // HTTP request and business request were both successful
    FAIL,       // HTTP request was successful, but the business request hit a failure condition (e.g. reject save due to validation error)
    ERROR       // Business request was unsuccessful (e.g. MongoClientException)

}
