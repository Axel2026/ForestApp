package com.agh.forest.Model;

import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static java.lang.Math.*;

@Data
@Builder
public class ForestPixel {

    private String id;
    private Temperature temperature;
    private boolean hasSensor;
    private Wind wind;
    private double pressure;
    private double humidity;
    private AirRating airRating;
    private PollutionGases pollutionGases;
    private double forestFireIndexValue;
    private double fieldPercentageDestroyed;
    private boolean isBeingBurned;
    private boolean isBeingExtinguish;
    private ForestFireExtingush forestFireExtingush;

    public ForestPixel createSimilarPixel(int maximumProbabilityValue){

        Random rand = new Random();
        Map<String, Double> probabilityForComponent = new HashMap<>();
        String [] names = {"Temperature", "Wind", "PollutionGases", "Other"};

        Arrays.stream(names).forEach(name -> {
            double probability = (double) ((rand.nextInt(maximumProbabilityValue * 2)) - maximumProbabilityValue) / 100;
            probabilityForComponent.put(name, probability);
        });
        ForestPixel newForestPixel = ForestPixel.builder()
                .airRating(this.airRating)
                .humidity(this.humidity)
                .pressure(this.pressure)
                .pollutionGases(this.pollutionGases.duplicate())
                .wind(this.wind.duplicate())
                .temperature(this.temperature.duplicate())
                .hasSensor(this.hasSensor)
                .fieldPercentageDestroyed(this.fieldPercentageDestroyed)
                .isBeingExtinguish(this.isBeingExtinguish)
                .forestFireExtingush(this.forestFireExtingush)
                .build();
        newForestPixel.temperature.applyProbabilityToFields(probabilityForComponent.get("Temperature"));
        newForestPixel.wind.applyProbabilityToFields(probabilityForComponent.get("Wind"));
        newForestPixel.humidity += probabilityForComponent.get("Other") * humidity;
        newForestPixel.pollutionGases.applyProbabilityToFields(probabilityForComponent.get("PollutionGases"));
        newForestPixel.calculateForestFireDAagerIndex();

        return newForestPixel;


    }

    public void calculateForestFireDAagerIndex(){

        double droughtFactor = getRandomDroughtFactor();
        double exponent = -0.45 + 0.987 * log(droughtFactor) - 0.0345 * getHumidity()  + 0.0338 * getTemperature().getCurrent() + 0.0234 * getWind().getSpeed();
        this.forestFireIndexValue = 2 * exp(exponent);

    }

    private int getRandomDroughtFactor(){
        Random ran = new Random();
        if (ran.nextDouble() < .1){
            return ran.nextInt(50) + 140;
        }
        else if (ran.nextDouble() < .1 + .3){
            return ran.nextInt(50) + 60;
        }
        else return ran.nextInt(50);

    }

    public ForestFireState convertFieldDestroyedToEnum(){
        if(this.fieldPercentageDestroyed == 0) return ForestFireState.NONE;
        else if(this.fieldPercentageDestroyed <= 25) return ForestFireState.LOW;
        else if(this.fieldPercentageDestroyed <= 50) return ForestFireState.MEDIUM;
        else if(this.fieldPercentageDestroyed <= 75) return ForestFireState.HIGH;
        else if(this.fieldPercentageDestroyed <= 99) return ForestFireState.EXTREME;
        else return ForestFireState.DESTROYED;
    }
    public void convertEnumToFieldDestroyed(ForestFireState state){
        switch (state){
            case NONE: {this.fieldPercentageDestroyed = 0; break;}
            case LOW: {this.fieldPercentageDestroyed =1; break; }
            case MEDIUM: {this.fieldPercentageDestroyed = 26; break; }
            case HIGH: {this.fieldPercentageDestroyed = 51; break; }
            case EXTREME: {this.fieldPercentageDestroyed = 76; }
        }
    }
    public int convertForestFireExtiguishToValue(){
        switch (forestFireExtingush){
            case LITTLE: return 15;
            case TINY: return 30;
            case VERY_SMALL: return 40;
            case SMALL: return 55;
            case MEDIUM: return 66;
            case BIG: return 77;
            case VERY_BIG: return 85;
            case EXTREME: return 95;
            case FULL: return 100;
        }
        return 100;
    }
    public ForestPixel createCopy(){
        return ForestPixel.builder()
                .airRating(this.getAirRating())
                .hasSensor(this.hasSensor)
                .fieldPercentageDestroyed(this.fieldPercentageDestroyed)
                .temperature(this.temperature)
                .forestFireIndexValue(this.forestFireIndexValue)
                .humidity(this.humidity)
                .isBeingBurned(this.isBeingBurned)
                .pollutionGases(this.pollutionGases)
                .id(this.id)
                .pressure(this.pressure)
                .wind(this.wind)
                .isBeingExtinguish(this.isBeingExtinguish)
                .forestFireExtingush(this.forestFireExtingush)
                .build();
    }


}
