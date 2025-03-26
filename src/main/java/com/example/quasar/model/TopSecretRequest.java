package com.example.quasar.model;

import java.util.List;

public class TopSecretRequest {
    private List<SatelliteData> satellites;

    public List<SatelliteData> getSatellites() {
        return satellites;
    }

    public void setSatellites(List<SatelliteData> satellites) {
        this.satellites = satellites;
    }
}
