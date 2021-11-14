package com.agh.forest.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Temperature {

    private Double current;
    private Double temperatureMin;
    private Double temperatureMax;

    public static Double convertKelvinToCelcius(Double temp){
        return temp - 272.15;
    }
    public  void applyProbabilityToFields(Double probability){
        this.current += probability * this.current;
        this.temperatureMax += probability * this.temperatureMax;
        this.temperatureMin += probability * this.temperatureMin;
    }
    public Temperature duplicate(){
        return new Temperature(current, temperatureMin, temperatureMax);
    }

}
