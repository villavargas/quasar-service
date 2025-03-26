package com.example.quasar.controller;

import com.example.quasar.model.Position;
import com.example.quasar.model.SatelliteData;
import com.example.quasar.model.TopSecretRequest;
import com.example.quasar.model.TopSecretResponse;
import com.example.quasar.service.QuasarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/topsecret")
public class TopSecretController {
    private static final Logger logger = LoggerFactory.getLogger(TopSecretController.class);

    private final QuasarService quasarService;

    public TopSecretController(QuasarService quasarService) {
        this.quasarService = quasarService;
    }

    /**
     * Procesa la información de los satélites para calcular la posición y reconstruir el mensaje.
     * @param request Datos recibidos de los satélites.
     * @return HTTP 200 con la información procesada o 404 si no se pudo determinar la ubicación o el mensaje.
     */
    @PostMapping
    public ResponseEntity<TopSecretResponse> topSecret(@RequestBody TopSecretRequest request) {
        logger.info("📡 Recibiendo datos de {} satélites", request.getSatellites().size());

        // Extraer listas de mensajes y distancias
        List<SatelliteData> satellites = request.getSatellites();
        List<List<String>> messages = satellites.stream().map(SatelliteData::getMessage).collect(Collectors.toList());

        logger.debug("📊 Datos de satélites recibidos: {}", satellites);

        // Calcular la posición
        Position position = quasarService.getLocation(satellites);
        if (position == null) {
            logger.error("❌ No se pudo calcular la posición del emisor.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Reconstruir el mensaje
        String message = quasarService.getMessage(messages);
        if (message == null || message.trim().isEmpty()) {
            logger.error("❌ No se pudo reconstruir el mensaje.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        logger.info("✅ Posición obtenida: x={}, y={}", position.getX(), position.getY());
        logger.info("📜 Mensaje reconstruido: '{}'", message);

        return ResponseEntity.ok(new TopSecretResponse(position, message));
    }
}
