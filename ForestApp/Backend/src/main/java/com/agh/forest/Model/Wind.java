package com.agh.forest.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wind {

    private Double speed;
    private Double deg;

    public void applyProbabilityToFields(double probability){
        speed += speed * probability;
        deg += deg * probability;
    }
    public Wind duplicate(){
        return new Wind(speed, deg);
    }

}
