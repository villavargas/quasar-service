package com.example.quasar.controller;

import com.example.quasar.model.Position;
import com.example.quasar.model.SatelliteData;
import com.example.quasar.model.TopSecretRequest;
import com.example.quasar.model.TopSecretResponse;
import com.example.quasar.service.QuasarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TopSecretController {

    private final QuasarService quasarService;

    public TopSecretController(QuasarService quasarService) {
        this.quasarService = quasarService;
    }

    @PostMapping("/topsecret")
    public ResponseEntity<TopSecretResponse> topSecret(@RequestBody TopSecretRequest request) {
        List<SatelliteData> satellites = request.getSatellites();
        // Extraer todas las listas de mensajes de cada satélite
        List<List<String>> messages = satellites.stream()
                .map(SatelliteData::getMessage)
                .collect(Collectors.toList());

        Position position = quasarService.getLocation(satellites);
        String message = quasarService.getMessage(messages);

        if (position == null || message == null || message.trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        TopSecretResponse response = new TopSecretResponse(position, message);
        return ResponseEntity.ok(response);
    }

    // Para el Nivel 3 se pueden crear endpoints adicionales, por ejemplo:
    // POST /topsecret_split/{satellite_name} para ir recibiendo la información de cada satélite
    // GET  /topsecret_split para retornar la posición y el mensaje si se tiene la información suficiente.
    // La lógica sería similar, almacenando la información de cada satélite en una estructura compartida (por ejemplo, en memoria)
    // y realizando el procesamiento cuando la información esté completa.
}
