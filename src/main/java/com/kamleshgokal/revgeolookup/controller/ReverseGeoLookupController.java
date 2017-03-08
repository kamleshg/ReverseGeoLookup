package com.kamleshgokal.revgeolookup.controller;

/**
 * Created by kgokal on 3/7/17.
 */

import com.kamleshgokal.revgeolookup.GlobalProperties;
import com.kamleshgokal.revgeolookup.exceptions.ReverseGeoLookupException;
import com.kamleshgokal.revgeolookup.services.ReverseGeoLookupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidParameterException;

@RestController
public class ReverseGeoLookupController {

    static Logger LOGGER = LoggerFactory.getLogger(ReverseGeoLookupController.class);

    @Autowired
    ReverseGeoLookupService service;

    @RequestMapping("/")
    public ResponseEntity<?> index() {
        LOGGER.info("Received request on root");
        return new ResponseEntity<>("Greetings from Spring Boot!", HttpStatus.OK);
    }

    @RequestMapping(value = "lookup", produces = "application/json")
    public ResponseEntity<?> lookup(String latlong) {
        if (latlong == null || latlong.isEmpty()) {
            LOGGER.info("Request with missing or invalid latlong values received");
            return new ResponseEntity<>("{\"error\":\"Missing required LatLong parameter!\"}", HttpStatus.BAD_REQUEST);
        }

        try {
            validate(latlong);
            String address = service.cachedLookupFirstFormattedAddress(latlong);
            String response = "{\"address\":\"" + address + "\"}";

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidParameterException e) {
            LOGGER.error("Parameters failed to validate", e);
            return new ResponseEntity<>("{\"error\":\"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
        } catch (ReverseGeoLookupException e) {
            LOGGER.error("Lookup first formatted address failed", e);
            return new ResponseEntity<>("{\"error\":\"" + e.getMessage() + "\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void validate(String latlong) {
        String[] values = latlong.split(",");
        if (values.length != 2) {
            LOGGER.debug("Found less then 2 values.");
            throw new InvalidParameterException("Expected 2 values.");
        } else if (!values[0].matches("^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$")) {
            LOGGER.debug("Latitude doesnt match format.");
            throw new InvalidParameterException("Latitude is invalid format.");
        } else if (!values[1].matches("^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$")) {
            LOGGER.debug("Longitude doesnt match format.");
            throw new InvalidParameterException("Longitude is invalid format.");
        }
    }

    @RequestMapping(value = "cache", produces = "application/json")
    public ResponseEntity<?> viewCache() {
        return new ResponseEntity<>(service.viewCache(), HttpStatus.OK);
    }
}