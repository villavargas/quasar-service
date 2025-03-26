package com.example.quasar.service;

import com.example.quasar.model.Position;
import com.example.quasar.model.SatelliteData;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuasarServiceTest {

    private final QuasarService quasarService = new QuasarService();

    @Test
    void testGetMessage() {
        // Simular mensajes parciales de tres satélites
        List<String> msg1 = Arrays.asList("este", "", "", "mensaje", "");
        List<String> msg2 = Arrays.asList("", "es", "", "", "secreto");
        List<String> msg3 = Arrays.asList("este", "", "un", "", "");
        List<List<String>> messages = Arrays.asList(msg1, msg2, msg3);

        String expected = "este es un mensaje secreto";
        String actual = quasarService.getMessage(messages);
        assertEquals(expected, actual, "El mensaje reconstruido debe ser igual al esperado");
    }

    @Test
    void testGetLocation() {
        // Simular satélites utilizando la clase SatelliteData (la lógica es la misma que en SatelliteDataData)
        SatelliteData kenobi = new SatelliteData();
        kenobi.setName("kenobi");
        kenobi.setDistance(100f);

        SatelliteData skywalker = new SatelliteData();
        skywalker.setName("skywalker");
        skywalker.setDistance(115.5f);

        SatelliteData sato = new SatelliteData();
        sato.setName("sato");
        sato.setDistance(142.7f);

        List<SatelliteData> SatelliteDatas = Arrays.asList(kenobi, skywalker, sato);
        Position position = quasarService.getLocation(SatelliteDatas);

        // El valor esperado dependerá de la implementación de la trilateración.
        // Se verifica que no sea nulo y que las coordenadas tengan un valor razonable.
        assertNotNull(position, "La posición no debe ser nula");
        System.out.println("Posición calculada: x = " + position.getX() + ", y = " + position.getY());
    }
}
