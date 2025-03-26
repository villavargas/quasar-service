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
class TopSecretSplitControllerIntegrationTest {

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
    void testTopSecretSplitHappyPath() throws Exception {
        // Simular envíos de datos para los tres satélites

        SatelliteData kenobi = new SatelliteData();
        kenobi.setName("kenobi");
        kenobi.setDistance(100f);
        kenobi.setMessage(Arrays.asList("este", "", "", "mensaje", ""));
        kenobi.setVersion(1);
        // Se fuerza el nombre desde la URL en el controlador, pero se puede enviar vacío o cualquier valor
        mockMvc.perform(post("/topsecret_split/kenobi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kenobi)))
                .andExpect(status().isCreated());

        SatelliteData skywalker = new SatelliteData();
        skywalker.setName("skywalker");
        skywalker.setDistance(115.5f);
        skywalker.setMessage(Arrays.asList("", "es", "", "", "secreto"));
        skywalker.setVersion(1);
        mockMvc.perform(post("/topsecret_split/skywalker")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(skywalker)))
                .andExpect(status().isCreated());

        SatelliteData sato = new SatelliteData();
        sato.setName("sato");
        sato.setDistance(142.7f);
        sato.setMessage(Arrays.asList("este", "", "un", "", ""));
        sato.setVersion(1);
        mockMvc.perform(post("/topsecret_split/sato")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sato)))
                .andExpect(status().isCreated());

        // Realizar GET para procesar la información
        mockMvc.perform(get("/topsecret_split"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position.x").exists())
                .andExpect(jsonPath("$.position.y").exists())
                .andExpect(jsonPath("$.message").value("este es un mensaje secreto"));
    }

    @Test
    void testTopSecretSplitNotFoundWhenIncomplete() throws Exception {
        // Solo se envía la información de un satélite
        SatelliteData kenobi = new SatelliteData();
        kenobi.setName("kenobi");
        kenobi.setDistance(100f);
        kenobi.setMessage(Arrays.asList("este", "", "", "mensaje", ""));
        kenobi.setVersion(1);
        mockMvc.perform(post("/topsecret_split/kenobi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kenobi)))
                .andExpect(status().isCreated());

        // Al realizar GET, se espera un 404 por falta de información completa
        mockMvc.perform(get("/topsecret_split"))
                .andExpect(status().isNotFound());
    }
}
