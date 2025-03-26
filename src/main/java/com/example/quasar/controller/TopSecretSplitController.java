package com.example.quasar.controller;

import com.example.quasar.model.Position;
import com.example.quasar.model.SatelliteData;
import com.example.quasar.model.TopSecretResponse;
import com.example.quasar.repository.SatelliteDataRepository;
import com.example.quasar.service.QuasarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/topsecret_split")
public class TopSecretSplitController {

    private final SatelliteDataRepository repository;
    private final QuasarService quasarService;
    private final ReentrantLock lock = new ReentrantLock();

    public TopSecretSplitController(SatelliteDataRepository repository, QuasarService quasarService) {
        this.repository = repository;
        this.quasarService = quasarService;
    }

    @Operation(summary = "Recibe la información parcial de un satélite")
    @ApiResponse(responseCode = "201", description = "Datos del satélite almacenados correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @PostMapping("/{satellite_name}")
    public ResponseEntity<Void> postSatelliteData(
            @PathVariable("satellite_name") String satelliteName,
            @Valid @RequestBody SatelliteData data) {
        // Forzar el nombre del satélite según la URL
        data.setName(satelliteName.toLowerCase());

        if (repository.existsById(data.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 Conflict
        }

        repository.save(data);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
    }

    @Operation(summary = "Obtiene la posición y el mensaje secreto")
    @ApiResponse(responseCode = "200", description = "Datos procesados correctamente")
    @ApiResponse(responseCode = "404", description = "No hay información suficiente")
    @GetMapping
    public ResponseEntity<TopSecretResponse> getTopSecretSplit() {
        lock.lock();
        try {
            List<SatelliteData> satellites = repository.findAll();

            if (satellites.size() < 3) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Position position = quasarService.getLocation(satellites);
            List<List<String>> messages = satellites.stream()
                    .map(SatelliteData::getMessage)
                    .toList();
            String message = quasarService.getMessage(messages);

            if (position == null || message == null || message.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            repository.deleteAll();

            TopSecretResponse response = new TopSecretResponse(position, message);
            return ResponseEntity.ok(response);
        } finally {
            lock.unlock();
        }
    }
}
