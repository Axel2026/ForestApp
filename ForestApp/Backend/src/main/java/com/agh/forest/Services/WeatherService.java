package com.agh.forest.Services;

import com.agh.forest.Model.ForestPixel;
import com.agh.forest.Model.Wind;
import com.agh.forest.data.ForestSimulationApp;
import com.agh.forest.dto.ForestPixelDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Data
@AllArgsConstructor
public class WeatherService {

    ForestSimulationApp forestSimulationApp;

    public void setBoardWithCityName(String cityName){
        forestSimulationApp.createBoard(cityName);
    }





}
