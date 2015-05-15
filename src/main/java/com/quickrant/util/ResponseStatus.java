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

    SUCCESS,    // Request was successful
    FAIL,       // Request was technically successful, but hit an error condition (e.g. reject save due to validation error)
    ERROR       // Request was unsuccessful (e.g. MongoClientException)

}
