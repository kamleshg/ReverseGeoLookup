package com.kamleshgokal.revgeolookup;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by kgokal on 3/8/17.
 */
@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties
public class GlobalProperties {

    private String apiKey;
    private String apiUrl;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }
}
