package com.agh.forest.Utilites;

import com.agh.forest.Model.ForestFireExtingush;
import com.fasterxml.jackson.databind.JsonNode;
import com.agh.forest.Http.HttpDataProvider;
import com.agh.forest.Model.AirRating;
import com.agh.forest.Model.ForestPixel;
import com.agh.forest.Model.Temperature;
import com.agh.forest.Pojo.PollutionRestPojo;
import com.agh.forest.Pojo.QualityRatePollutionPojo;
import com.agh.forest.Pojo.WeatherPojo;
import com.agh.forest.Pojo.WeatherRestPojo;
import com.agh.forest.json.Json;

import java.io.IOException;

public class CreatingModels {

    public static Temperature createTemperature(WeatherPojo weatherPojo) {
        return Temperature.builder()
                .current(Temperature.convertKelvinToCelcius(weatherPojo.getTemperature()))
                .temperatureMax(Temperature.convertKelvinToCelcius(weatherPojo.getTemperatureMax()))
                .temperatureMin(Temperature.convertKelvinToCelcius(weatherPojo.getTemperatureMin()))
                .build();

    }

    public static ForestPixel createForestPixel(PollutionRestPojo pollutionPojo, WeatherRestPojo weatherPojo) {
        ForestPixel pixel = ForestPixel.builder()
                .airRating(connvertIntRatingToEnumAirRating(pollutionPojo.getList().get(0).getMain()))
                .humidity(weatherPojo.getWeather().getHumidity())
                .pressure(weatherPojo.getWeather().getPressure())
                .temperature(createTemperature(weatherPojo.getWeather()))
                .wind(weatherPojo.getWind())
                .pollutionGases(pollutionPojo.getList().get(0).getComponents())
                .hasSensor(false)
                .fieldPercentageDestroyed(0)
                .isBeingExtinguish(false)
                .forestFireExtingush(ForestFireExtingush.LITTLE)
                .build();
        pixel.calculateForestFireDAagerIndex();
        return pixel;

    }

    public static AirRating connvertIntRatingToEnumAirRating(QualityRatePollutionPojo value) {
        switch (value.aqi) {
            case 1:
                return AirRating.GOOD;
            case 2:
                return AirRating.FAIR;
            case 3:
                return AirRating.MODERATE;
            case 4:
                return AirRating.POOR;
            case 5:
                return AirRating.VERY_POOR;
        }
        return AirRating.GOOD;

    }

    public static ForestPixel createForestPixelFromrApi(String cityName) throws IOException, InterruptedException {

        String weatherResult = HttpDataProvider.getCurrentWeather(cityName);
        JsonNode weatherJsonNode = Json.parse(weatherResult);
        WeatherRestPojo weatherRestPojo = Json.fromJson(weatherJsonNode, WeatherRestPojo.class);

        String airPollutionData = HttpDataProvider.getCurrentAirPollutionData(weatherRestPojo.getCoord().lat, weatherRestPojo.getCoord().getLon());
        JsonNode node2 = Json.parse(airPollutionData);
        PollutionRestPojo pollutionRestPojo = Json.fromJson(node2, PollutionRestPojo.class);

        return createForestPixel(pollutionRestPojo, weatherRestPojo);


    }

}
