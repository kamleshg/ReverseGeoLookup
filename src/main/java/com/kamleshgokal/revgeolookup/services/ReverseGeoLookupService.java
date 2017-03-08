package com.kamleshgokal.revgeolookup.services;

import com.kamleshgokal.revgeolookup.GlobalProperties;
import com.kamleshgokal.revgeolookup.exceptions.ReverseGeoLookupException;
import com.kamleshgokal.revgeolookup.util.Cache;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

/**
 * Created by kgokal on 3/7/17.
 */
@Component
public class ReverseGeoLookupService {
    static Logger LOGGER = LoggerFactory.getLogger(ReverseGeoLookupService.class);

    @Autowired
    GlobalProperties props;

    private static Cache<String, String[]> cache = new Cache<>(10, -1);

    public String lookup(String latlong) throws ReverseGeoLookupException {
        String URL = props.getApiUrl()
                + "?latlng=" + latlong + "&key=" + props.getApiKey();
        LOGGER.debug("The API URL is: " + URL);

        try (java.util.Scanner s = new java.util.Scanner(new java.net.URL(URL).openStream())) {
            String val = s.useDelimiter("\\A").next();
            LOGGER.debug("Returned: " + val);
            return val;
        } catch (MalformedURLException e) {
            LOGGER.error("MalformedURLException", e);
            throw new ReverseGeoLookupException(e.getMessage() , e);
        } catch (IOException e) {
            LOGGER.error("Failed calling API Service", e);
            throw new ReverseGeoLookupException(e.getMessage() , e);
        }
    }

    public String lookupFirstFormattedAddress(String latlong) throws ReverseGeoLookupException {
        String rawJsonResponse = lookup(latlong);
        try {
            JSONObject response = new JSONObject(rawJsonResponse);
            if (response.getString("status").equalsIgnoreCase("ZERO_RESULTS")) {
                return "No results found.";
            }

            JSONArray results = response.getJSONArray("results");
            JSONObject first = results.getJSONObject(0);
            String formatted_address = first.getString("formatted_address");
            return formatted_address;
        } catch (JSONException e) {
            LOGGER.error("Failed to parse response", e);
            throw new ReverseGeoLookupException("Error parsing response: ", e);
        }
    }

    public String cachedLookupFirstFormattedAddress(String latlong) throws ReverseGeoLookupException {
        if (cache.containsKey(latlong)) {
            LOGGER.debug("Cached item: " + latlong);
            return cache.get(latlong)[0];
        } else {
            String formatted_address = lookupFirstFormattedAddress(latlong);

            String time = new Date(System.currentTimeMillis()).toString();
            String[] values = {formatted_address, time};

            cache.put(latlong, values);
            return formatted_address;
        }
    }

    public String viewCache() {
        String cacheContents = "{\"Cache Contents\": [\n";
        for(String curr: cache.keySet()) {
            String address = cache.get(curr)[0];
            String time = cache.get(curr)[1];
            cacheContents += "{\"latlng\":\"" + curr + "\",\"address\":\"" + address + "\",\"time\":\"" + time + "\"},\n";
        }
        cacheContents = cacheContents.replaceAll(",$", "");//Strip last comma
        return cacheContents + "]}";
    }
}
