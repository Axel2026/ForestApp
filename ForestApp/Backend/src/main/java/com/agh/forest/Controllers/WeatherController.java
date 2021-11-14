package com.agh.forest.Controllers;

import com.agh.forest.Model.ForestPixel;
import com.agh.forest.Services.WeatherService;
import com.agh.forest.Utilites.CreatingModels;
import com.agh.forest.dto.ForestPixelDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
public class WeatherController {

    private WeatherService weatherService;

    @PostMapping("/city")
    public ResponseEntity<String> setCity(@RequestBody String city){
        weatherService.setBoardWithCityName(city);
        return new ResponseEntity<>(city, HttpStatus.OK);
    }

}
