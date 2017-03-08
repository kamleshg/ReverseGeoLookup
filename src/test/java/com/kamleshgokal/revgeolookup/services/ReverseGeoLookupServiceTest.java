package com.kamleshgokal.revgeolookup.services;

import com.kamleshgokal.revgeolookup.ReverseGeoLookupApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

/**
 * Created by kgokal on 3/7/17.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration(classes = {ReverseGeoLookupApplication.class})
public class ReverseGeoLookupServiceTest {

    @Autowired
    private ReverseGeoLookupService service;

    @Test
    public void lookupResults() {
        String value = null;
        try {
            value = service.lookup("40.714224,-73.961452");
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assert value.contains("results");
    }

    @Test
    public void lookupFirstAddress() {
        String value = null;
        try {
            value = service.cachedLookupFirstFormattedAddress("40.714224,-73.961452");
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals("277 Bedford Ave, Brooklyn, NY 11211, USA", value);
    }

    @Test
    public void lookupInvalidAddress() {
        String value = null;
        try {
            value = service.cachedLookupFirstFormattedAddress("0,0");
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals("No results found.", value);
    }
}
