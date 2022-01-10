package com.agh.forest.dto;

import com.agh.forest.Model.ForestFireIndex;
import com.agh.forest.Model.ForestFireState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForestPixelDto {

    private double temperature;
    private double windStrength;
    private double windDirection;
    private double humidity;
    private double pressure;
    private double id;
    private boolean hasSensor;
    private ForestFireIndex forestFireIndex;
    private boolean isBeingBurned;
    private boolean isBeingExtinguished;
    private String forestType;
    private ForestFireState forestFireState;

}
