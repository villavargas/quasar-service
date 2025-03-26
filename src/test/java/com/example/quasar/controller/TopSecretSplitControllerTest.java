package com.example.quasar.controller;

import com.example.quasar.model.SatelliteData;
import com.example.quasar.repository.SatelliteDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TopSecretSplitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SatelliteDataRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void testPostSatelliteData_Created() throws Exception {
        SatelliteData kenobi = new SatelliteData();
        kenobi.setDistance(100f);
        kenobi.setMessage(Arrays.asList("este", "", "", "mensaje", ""));

        mockMvc.perform(post("/topsecret_split/kenobi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kenobi)))
                .andExpect(status().isCreated());
    }

    @Test
    void testPostSatelliteData_Conflict() throws Exception {
        SatelliteData kenobi = new SatelliteData();
        kenobi.setDistance(100f);
        kenobi.setMessage(Arrays.asList("este", "", "", "mensaje", ""));

        mockMvc.perform(post("/topsecret_split/kenobi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kenobi)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/topsecret_split/kenobi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kenobi)))
                .andExpect(status().isConflict());
    }

    @Test
    void testGetTopSecretSplit_NotFound() throws Exception {
        mockMvc.perform(get("/topsecret_split"))
                .andExpect(status().isNotFound());
    }
}
