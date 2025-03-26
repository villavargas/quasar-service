package com.example.quasar.controller;

import com.example.quasar.model.Position;
import com.example.quasar.model.SatelliteData;
import com.example.quasar.model.TopSecretResponse;
import com.example.quasar.repository.SatelliteDataRepository;
import com.example.quasar.service.QuasarService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/topsecret_split")
public class TopSecretSplitController {
    private static final Logger logger = LoggerFactory.getLogger(TopSecretSplitController.class);

    private final SatelliteDataRepository repository;
    private final QuasarService quasarService;
    private final ReentrantLock lock = new ReentrantLock();

    public TopSecretSplitController(SatelliteDataRepository repository, QuasarService quasarService) {
        this.repository = repository;
        this.quasarService = quasarService;
    }

    /**
     * Recibe la información de un satélite específico y la almacena en la base de datos.
     * @param satelliteName Nombre del satélite (viene en la URL).
     * @param data Datos de distancia y mensaje del satélite.
     * @return HTTP 201 si se almacenó correctamente, 409 si ya existe.
     */
    @PostMapping("/{satellite_name}")
    public ResponseEntity<Void> postSatelliteData(@PathVariable("satellite_name") String satelliteName,
                                                  @Valid @RequestBody SatelliteData data) {
        logger.info("📡 Recibiendo datos del satélite '{}': distancia={}, mensaje={}",
                satelliteName, data.getDistance(), data.getMessage());

        data.setName(satelliteName.toLowerCase());

        if (repository.existsById(data.getName())) {
            logger.warn("⚠️ Intento de sobreescribir datos del satélite '{}'", satelliteName);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        repository.save(data);
        logger.debug("✅ Datos almacenados correctamente para el satélite '{}'", satelliteName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Procesa la información almacenada y devuelve la ubicación y el mensaje reconstruido.
     * @return HTTP 200 con los datos procesados, o 404 si la información es insuficiente.
     */
    @GetMapping
    public ResponseEntity<TopSecretResponse> getTopSecretSplit() {
        lock.lock();
        try {
            logger.info("🚀 Procesando datos almacenados para obtener ubicación y mensaje");

            List<SatelliteData> satellites = repository.findAll();
            logger.debug("📊 Se encontraron {} satélites en la base de datos", satellites.size());

            if (satellites.size() < 3) {
                logger.warn("⚠️ No hay suficiente información en la base de datos para calcular la posición.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Cálculo de posición y mensaje
            Position position = quasarService.getLocation(satellites);
            List<List<String>> messages = satellites.stream().map(SatelliteData::getMessage).toList();
            String message = quasarService.getMessage(messages);

            if (position == null || message == null || message.trim().isEmpty()) {
                logger.error("❌ No se pudo calcular la posición o reconstruir el mensaje.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Eliminar datos procesados para evitar duplicaciones
            repository.deleteAll();
            logger.info("🗑️ Datos procesados y eliminados correctamente.");

            logger.info("✅ Posición obtenida: x={}, y={}", position.getX(), position.getY());
            logger.info("📜 Mensaje reconstruido: '{}'", message);

            return ResponseEntity.ok(new TopSecretResponse(position, message));
        } finally {
            lock.unlock();
        }
    }
}
