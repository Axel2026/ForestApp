package com.agh.forest.Model.agent;

import com.agh.forest.Model.ForestPixel;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Data
public class SensorAgent extends Agent  {

    private List<ForestPixel> forestPixels;

    @Override
    public List<ForestPixel> sendMessage() {
        return this.forestPixels;
    }

    public void refreshData(ForestPixel[][] board){
        List<String> ids = forestPixels.stream()
                .map(ForestPixel::getId)
                .collect(Collectors.toList());

        forestPixels = Arrays.stream(board)
                .flatMap(Arrays::stream)
                .filter(pixel -> ids.contains(pixel.getId()))
                .collect(Collectors.toList());
    }
}
