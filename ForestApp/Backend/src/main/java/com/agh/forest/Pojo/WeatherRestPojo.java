package com.agh.forest.Pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import com.agh.forest.Model.Wind;

@Data
public class WeatherRestPojo {

    private Cordinates coord;
    @JsonProperty("main")
    private WeatherPojo weather;
    private Wind wind;
}
