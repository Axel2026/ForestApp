package com.agh.forest.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class Summary {

    private double numberOfDestroyedFields;
    private LocalDateTime startingDate;
    private LocalDateTime endingDate;
    private Map<String, Double> allDamagedFields = new HashMap<>();
    private double overallPercentageDestroyed;

}
