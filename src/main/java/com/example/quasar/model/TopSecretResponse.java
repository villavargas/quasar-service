package com.example.quasar.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta que contiene la posición y el mensaje secreto")
public class TopSecretResponse {

    @Schema(description = "Coordenadas de la posición", example = "{\"x\": -100.0, \"y\": 75.5}")
    private Position position;

    @Schema(description = "Mensaje reconstruido", example = "este es un mensaje secreto")
    private String message;

    public TopSecretResponse() {}

    public TopSecretResponse(Position position, String message) {
        this.position = position;
        this.message = message;
    }

    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
