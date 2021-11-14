package com.agh.forest.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollutionGases {

    private Double co;
    private Double no;
    private Double no2;
    private Double o3;
    private Double so2;
    private Double pm2_5;
    private Double pm10;
    private Double nh3;

    public PollutionGases duplicate(){
        return new PollutionGases(co, no, no2, o3, so2, pm2_5, pm10, nh3);
    }
    public void applyProbabilityToFields(double probability){
        co += co * probability;
        no += no * probability;
        no2 += no2 * probability;
        o3 += o3 * probability;
        so2 += so2 * probability;
        pm2_5 += pm2_5 * probability;
        pm10 += pm10 * probability;
        nh3 += nh3 * probability;
    }
}
