package com.example.quasar.repository;

import com.example.quasar.model.SatelliteData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SatelliteDataRepository extends JpaRepository<SatelliteData, String> {
}
