package com.kamleshgokal.revgeolookup.exceptions;

/**
 * Created by kgokal on 3/7/17.
 */
public class ReverseGeoLookupException extends Exception {

    public ReverseGeoLookupException(String message) {
        super(message);
    }

    public ReverseGeoLookupException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
