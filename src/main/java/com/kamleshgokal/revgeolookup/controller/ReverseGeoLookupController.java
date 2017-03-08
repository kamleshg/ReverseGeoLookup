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

    @RequestMapping("lookup")
    public ResponseEntity<?> lookup(String latlong) {
        if (latlong == null || latlong.isEmpty()) {
            LOGGER.info("Request with missing latlong value received");
            return new ResponseEntity<>("Missing required LatLong parameter!", HttpStatus.BAD_REQUEST);
        }
        try {
            return new ResponseEntity<>(service.cachedLookupFirstFormattedAddress(latlong), HttpStatus.OK);
        } catch (ReverseGeoLookupException e) {
            LOGGER.error("Lookup first formatted address failed", e);
            return new ResponseEntity<>("Exception: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("cache")
    public ResponseEntity<?> viewCache() {
        return new ResponseEntity<>(service.viewCache(), HttpStatus.OK);
    }

}