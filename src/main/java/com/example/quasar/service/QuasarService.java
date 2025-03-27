package com.example.quasar.service;

import com.example.quasar.model.Position;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuasarService {

    private static final Logger logger = LoggerFactory.getLogger(QuasarService.class);

    // Coordenadas conocidas de los satélites (según reto)
    private final Position kenobiPos = new Position(-500f, -200f);
    private final Position skywalkerPos = new Position(100f, -100f);
    private final Position satoPos = new Position(500f, 100f);

    /**
     * Calcula la posición del emisor a partir de las distancias recibidas.
     * Se utiliza una aproximación mediante trilateración.
     */
    public Position getLocation(List<?> satellites) {
    	logger.info("Iniciando cálculo de ubicación con {} satélites", satellites.size());
        Float dKenobi = null, dSkywalker = null, dSato = null;
        for (Object s : satellites) {
            String name = "";
            Float distance = null;
            if (s instanceof com.example.quasar.model.SatelliteData) {
                com.example.quasar.model.SatelliteData sat = (com.example.quasar.model.SatelliteData) s;
                name = sat.getName();
                distance = sat.getDistance();
            } else if (s instanceof com.example.quasar.model.SatelliteData) {
                com.example.quasar.model.SatelliteData sat = (com.example.quasar.model.SatelliteData) s;
                name = sat.getName();
                distance = sat.getDistance();
            }
            if ("kenobi".equalsIgnoreCase(name)) {
                dKenobi = distance;
            } else if ("skywalker".equalsIgnoreCase(name)) {
                dSkywalker = distance;
            } else if ("sato".equalsIgnoreCase(name)) {
                dSato = distance;
            }
        }
        if (dKenobi == null || dSkywalker == null || dSato == null) {
            return null;
        }

        // Coordenadas de cada satélite
        double[] x1 = {kenobiPos.getX(), kenobiPos.getY()};
        double[] x2 = {skywalkerPos.getX(), skywalkerPos.getY()};
        double[] x3 = {satoPos.getX(), satoPos.getY()};
        
        double[][] positions = {x1,x2,x3};

        double d1 = dKenobi;
        double d2 = dSkywalker;
        double d3 = dSato;
        
        double[] distances = {d1,d2,d3};
        
        TrilaterationFunction trilaterationFunction = new TrilaterationFunction(positions, distances);
        NonLinearLeastSquaresSolver nSolver = new NonLinearLeastSquaresSolver(trilaterationFunction, new LevenbergMarquardtOptimizer());

   
        
        logger.debug("Ubicación calculada: x={}, y={}", nSolver.solve().getPoint().toArray());

        double[] points = nSolver.solve().getPoint().toArray();
        
        float x = (float)points[0];
        float y = (float)points[1];
        
        return new Position(x, y);
    }

    /**
     * Reconstruye el mensaje a partir de los arreglos de palabras.
     */
    public String getMessage(List<List<String>> messages) {
    	logger.info("Iniciando reconstrucción del mensaje");
        if (messages == null || messages.isEmpty()) {
        	logger.error("No se recibieron mensajes válidos para reconstrucción");
            return null;
        }
        int maxLength = messages.stream().mapToInt(List::size).max().orElse(0);
        List<String> result = new ArrayList<>();
        for (int i = 0; i < maxLength; i++) {
            String word = "";
            for (List<String> msg : messages) {
                if (i < msg.size() && !msg.get(i).trim().isEmpty()) {
                    word = msg.get(i).trim();
                    break;
                }
            }
            if (!word.isEmpty()) {
                result.add(word);
            }
        }
        String reconstructedMessage = String.join(" ", result);
        logger.debug("Mensaje reconstruido: {}", reconstructedMessage);
        return reconstructedMessage;
    }
}
