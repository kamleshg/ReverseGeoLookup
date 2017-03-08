package com.kamleshgokal.revgeolookup.controller;

/**
 * Created by kgokal on 3/7/17.
 */
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReverseGeoLookupControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getGreetings() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Greetings from Spring Boot!")));
    }

    @Test
    public void getLookupWithOutLatlong() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/lookup").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(equalTo("Missing required LatLong parameter!")));
    }

    @Test
    public void getValidLookup1() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/lookup?latlong=40.714224,-73.961452").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("277 Bedford Ave, Brooklyn, NY 11211, USA")));
    }

    @Test
    public void getValidLookup2() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/lookup?latlong=34.070157,-84.333572").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("914-920 Upper Hembree Rd, Roswell, GA 30076, USA")));
    }

    @Test
    public void getWithBadLatLong() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/lookup?latlong=34.070157, -84.333572").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(containsString("Exception: Server returned HTTP response code: 400 for URL:")));
    }

    @Test
    public void getCacheAtLimit() throws Exception {
        loadCache();

        mvc.perform(MockMvcRequestBuilders.get("/cache").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("333570")))
                .andExpect(content().string(containsString("333571")))
                .andExpect(content().string(containsString("333572")))
                .andExpect(content().string(containsString("333573")))
                .andExpect(content().string(containsString("333574")))
                .andExpect(content().string(containsString("333575")))
                .andExpect(content().string(containsString("333576")))
                .andExpect(content().string(containsString("333577")))
                .andExpect(content().string(containsString("333578")))
                .andExpect(content().string(containsString("333579")));
    }

    private void loadCache() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/lookup?latlong=34.070157,-84.333570").accept(MediaType.APPLICATION_JSON));
        mvc.perform(MockMvcRequestBuilders.get("/lookup?latlong=34.070157,-84.333571").accept(MediaType.APPLICATION_JSON));
        mvc.perform(MockMvcRequestBuilders.get("/lookup?latlong=34.070157,-84.333572").accept(MediaType.APPLICATION_JSON));
        mvc.perform(MockMvcRequestBuilders.get("/lookup?latlong=34.070157,-84.333573").accept(MediaType.APPLICATION_JSON));
        mvc.perform(MockMvcRequestBuilders.get("/lookup?latlong=34.070157,-84.333574").accept(MediaType.APPLICATION_JSON));
        mvc.perform(MockMvcRequestBuilders.get("/lookup?latlong=34.070157,-84.333575").accept(MediaType.APPLICATION_JSON));
        mvc.perform(MockMvcRequestBuilders.get("/lookup?latlong=34.070157,-84.333576").accept(MediaType.APPLICATION_JSON));
        mvc.perform(MockMvcRequestBuilders.get("/lookup?latlong=34.070157,-84.333577").accept(MediaType.APPLICATION_JSON));
        mvc.perform(MockMvcRequestBuilders.get("/lookup?latlong=34.070157,-84.333578").accept(MediaType.APPLICATION_JSON));
        mvc.perform(MockMvcRequestBuilders.get("/lookup?latlong=34.070157,-84.333579").accept(MediaType.APPLICATION_JSON));
    }


    @Test
    public void getCacheAboveLimitOfTen() throws Exception {
        loadCache();

        mvc.perform(MockMvcRequestBuilders.get("/lookup?latlong=34.070157,-84.333580").accept(MediaType.APPLICATION_JSON));
        mvc.perform(MockMvcRequestBuilders.get("/lookup?latlong=34.070157,-84.333581").accept(MediaType.APPLICATION_JSON));

        mvc.perform(MockMvcRequestBuilders.get("/cache").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("333572")))
                .andExpect(content().string(containsString("333573")))
                .andExpect(content().string(containsString("333574")))
                .andExpect(content().string(containsString("333575")))
                .andExpect(content().string(containsString("333576")))
                .andExpect(content().string(containsString("333577")))
                .andExpect(content().string(containsString("333578")))
                .andExpect(content().string(containsString("333579")))
                .andExpect(content().string(containsString("333580")))
                .andExpect(content().string(containsString("333581")));
    }
}
