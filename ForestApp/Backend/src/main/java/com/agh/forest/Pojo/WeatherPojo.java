package com.agh.forest.Pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeatherPojo {

    @JsonProperty("temp")
    private Double temperature;
    @JsonProperty("temp_min")
    private Double temperatureMin;
    @JsonProperty("temp_max")
    private Double temperatureMax;
    private Double pressure;
    private Double humidity;
}
