package com.example.quasar.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "satellite_data")
public class SatelliteData {

    @Id
    private String name;  // Kenobi, Skywalker o Sato

    @NotNull(message = "La distancia es obligatoria")
    private Float distance;

    @ElementCollection
    @CollectionTable(name = "satellite_message", joinColumns = @JoinColumn(name = "satellite_name"))
    @Column(name = "word")
    @NotEmpty(message = "El mensaje no puede estar vacío")
    private List<String> message;
    
    // Campo para manejo optimista de concurrencia
    @Version
    private Integer version;

    // Getters & Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Float getDistance() { return distance; }
    public void setDistance(Float distance) { this.distance = distance; }

    public List<String> getMessage() { return message; }
    public void setMessage(List<String> message) { this.message = message; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
}
