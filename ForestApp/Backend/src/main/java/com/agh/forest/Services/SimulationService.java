package com.agh.forest.Services;

import com.agh.forest.Model.ForestPixel;
import com.agh.forest.data.ForestSimulationApp;
import com.agh.forest.dto.Summary;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@Data
@AllArgsConstructor
public class SimulationService {

    private ForestSimulationApp forestSimulationApp;
    public void startSimulation() {
       forestSimulationApp.startSimulation();
    }

    public Summary endSimulation() {
        forestSimulationApp.getScheduledExecutorService().shutdown();
        Summary summary = new Summary();
        summary.setNumberOfDestroyedFields(
                Arrays.stream(forestSimulationApp.getBoard())
                .flatMap(Arrays::stream)
                .map(ForestPixel::getFieldPercentageDestroyed)
                .mapToDouble(element -> element)
                .filter(value -> value >= 99)
                .count());
        summary.setAllDamagedFields(Arrays.stream(forestSimulationApp.getBoard())
                .flatMap(Arrays::stream)
                .filter(element -> element.getFieldPercentageDestroyed() > 0)
                .collect(Collectors.toMap(ForestPixel::getId,
                        value -> (double) Math.round(value.getFieldPercentageDestroyed() * 10) / 10 )));
        summary.setStartingDate(forestSimulationApp.getAgentDashboard().getStartingDate());
        summary.setEndingDate(forestSimulationApp.getAgentDashboard().getDate());
        double sumFieldPercentageDestroyed = Arrays.stream(forestSimulationApp.getBoard())
                .flatMap(Arrays::stream)
                .map(ForestPixel::getFieldPercentageDestroyed)
                .mapToDouble(element -> element)
                .map(element -> {
                    if(element > 100) return 100;
                    return element;
                })
                .sum();
        summary.setOverallPercentageDestroyed(
                (double) Math.round( 10 * sumFieldPercentageDestroyed / (forestSimulationApp.COLUMN_SIZE_BOARD * forestSimulationApp.ROW_SIZE_BOARD)) / 10)  ;
        return summary;
    }
}
