package com.agh.forest.data;

import com.agh.forest.Model.ForestPixel;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class BoardCalculation {
    private final double a = 0.03;
    private final double b = 0.05;
    private final double c = 0.01;
    private final double d = 0.3;


    public  int calculateDistanceBetweenPixelsDegree(ForestPixel pixel, ForestPixel neighbour){
        Long[] rowIdColId = this.getRowColNumberFromPixelId(pixel);
        Long[] rowIdColIdNeighbour = this.getRowColNumberFromPixelId(neighbour);
        if(rowIdColIdNeighbour[0] < rowIdColId[0] && rowIdColIdNeighbour[1] < rowIdColId[1]) return 135;
        else if(rowIdColIdNeighbour[0] < rowIdColId[0] && rowIdColIdNeighbour[1].equals(rowIdColId[1])) return 180;
        else if(rowIdColIdNeighbour[0] < rowIdColId[0] && rowIdColIdNeighbour[1] > rowIdColId[1]) return 225;
        else if(rowIdColIdNeighbour[0].equals(rowIdColId[0]) && rowIdColIdNeighbour[1] < rowIdColId[1]) return 90;
        else if(rowIdColIdNeighbour[0].equals(rowIdColId[0]) && rowIdColIdNeighbour[1] > rowIdColId[1]) return 270;
        else if(rowIdColIdNeighbour[0] > rowIdColId[0] && rowIdColIdNeighbour[1] < rowIdColId[1]) return 45;
        else if(rowIdColIdNeighbour[0] > rowIdColId[0] && rowIdColIdNeighbour[1].equals(rowIdColId[1])) return 0;
        else if(rowIdColIdNeighbour[0] > rowIdColId[0] && rowIdColIdNeighbour[1] > rowIdColId[1]) return 270;
        else return 360;


    }
    public int calculateForestFWindDirectionToForestPosition(ForestPixel pixel, int distanceToAnotherPixel){
       int value = (int) (pixel.getWind().getDeg() - distanceToAnotherPixel);
       int factor =  25;
        for(int i=0; i<=180; i=i+23){
            if( value < i) return factor;
            factor = factor - 3;
        }
        return factor;

    }
    public int calculateDistanceBetweenPixelsDegreeV2(ForestPixel pixel, ForestPixel neighbour){
        Long[] rowIdColId = this.getRowColNumberFromPixelId(pixel);
        Long[] rowIdColIdNeighbour = this.getRowColNumberFromPixelId(neighbour);
        float angle = (float) Math.toDegrees(Math.atan2(rowIdColId[0] - rowIdColIdNeighbour[0], rowIdColId[1] - rowIdColIdNeighbour[1]));

        if(angle < 0){
            angle += 360;
        }

        return (int) angle;
    }

    public Long[] getRowColNumberFromPixelId(ForestPixel forestPixel){
        String[] split = forestPixel.getId().split(":");
        return  Arrays.stream(split)
                .map(Long::parseLong)
                .toArray(Long[]::new);
    }

    public int getMinRange(int range, int windowSize) {
        return IntStream.iterate(range, r-> r - 1)
                .limit(Math.round(windowSize/2.0))
                .filter(number -> number >= 0)
                .min()
                .orElse(0);
    }

    public int getMaxRange(int range, int windowSize, int maxRange) {
        return IntStream.iterate(range, r -> r +1)
                .limit(Math.round(windowSize / 2.0))
                .filter(number -> number <= maxRange)
                .max()
                .orElse(range);
    }
    public double calculateFireRising(ForestPixel pixel, List<ForestPixel> neighbours){
        double ownForestFireSpread = this.computeOwnForestFireSpread(pixel);
        double fieldDestroyed = neighbours.stream()
                .filter(ForestPixel::isBeingBurned)
                .map(neighbour -> {
                    int distance = calculateDistanceBetweenPixelsDegree(pixel, neighbour);
                    return computeForestFireSpread(neighbour, distance);
                })
                .mapToDouble(element -> element)
                .sum();

        return fieldDestroyed + ownForestFireSpread;
    }
    public double computeForestFireSpread(ForestPixel pixel, int angleWindDirection){
        double W= (int) Math.pow((pixel.getWind().getSpeed()/ 0.836), -2.0/3);
        double R0 = a * pixel.getTemperature().getCurrent() + b * W + c * (100 - pixel.getHumidity()) - d;
        double KW = Math.exp((0.1783 * pixel.getWind().getSpeed() * Math.cos(Math.toRadians(angleWindDirection))));
        double K0 = Math.exp((pixel.getForestFireIndexValue() - 2) * (1.6 -0.1)/ (50 -2) + 0.1);
        double TK = 0.5;
        return R0 * KW * K0 * TK;

    }
    public double computeOwnForestFireSpread(ForestPixel pixel){
        return (normalizeBurning(pixel) * 3  * (a * pixel.getTemperature().getCurrent()
                + b * pixel.getForestFireIndexValue()  + c * ( 100 - pixel.getHumidity()))  );
    }
    private double normalizeBurning(ForestPixel pixel){
        return (pixel.getFieldPercentageDestroyed() / 100.0 * 900 - 1) * ( 6 - 2) / ( 900 - 1) + 2;
    }


    public boolean calculateProbabilityOfStartingBurning(ForestPixel pixel, List<ForestPixel> neighbours){
        var probability = neighbours.stream()
                .filter(ForestPixel::isBeingBurned)
                .filter(pixelF -> pixelF.getFieldPercentageDestroyed() > 15)
                .map(neighbour -> {
                    Long[] rowIdColIdNeighbour = this.getRowColNumberFromPixelId(neighbour);
                    int distance = this.calculateDistanceBetweenPixelsDegree(pixel, neighbour);
                    return this.calculateProbabilityOfStartingBurning(neighbour, distance);

                })
                .mapToDouble(element -> element)
                .sum();
        return  Math.random() <= probability;


    }
    public double calculateProbabilityOfStartingBurning(ForestPixel pixel, int angleWindDirection){
        var value = 0.1 * pixel.getWind().getSpeed() * Math.cos(Math.toRadians(angleWindDirection)) + 1.5;
        value = value * normalizeBurning(pixel);
        return value / 800;
    }

    public boolean calculeOwnBurning(ForestPixel currentPixel) {
        double forestFireIndexValue = currentPixel.getForestFireIndexValue() / 4000.0 ;

        var name = Math.random();
        return  Math.random() <= forestFireIndexValue;

    }

}
