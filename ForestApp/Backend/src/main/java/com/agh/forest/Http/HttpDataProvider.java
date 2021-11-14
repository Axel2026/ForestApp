package com.agh.forest.Http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;


public class HttpDataProvider {

    private static final String API_KEY = "232f35f28b855d404b1575a0d004047c";
    private static HttpClient httpClient = HttpClient.newBuilder().build();

    public static String getCurrentAirPollutionData(double lat, double lon) throws IOException, InterruptedException {

        String BASE_URL = "http://api.openweathermap.org/data/2.5/air_pollution?";
        String URL_QUERY_PARAMS = "lat={0}&lon={1}&appid={2}";
        String URL = MessageFormat.format(URL_QUERY_PARAMS, String.valueOf(lat), String.valueOf(lon), API_KEY);
        final HttpRequest request = makeRequest(BASE_URL + URL);

        HttpResponse.BodyHandler<String> stringBodyHandler = HttpResponse.BodyHandlers.ofString();
        var response = httpClient.send(request, stringBodyHandler);
        return response.body();
    }

    public static String getCurrentWeather(String cityName) throws IOException, InterruptedException{
        String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
        String URL_QUERY_PARAMS = "q={0}&appid={1}";
        String URL = MessageFormat.format(URL_QUERY_PARAMS, cityName, API_KEY);
        final HttpRequest request = makeRequest(BASE_URL + URL);

        HttpResponse.BodyHandler<String> stringBodyHandler = HttpResponse.BodyHandlers.ofString();
        var response = httpClient.send(request, stringBodyHandler);
        return response.body();
    }

    private static HttpRequest makeRequest(String URL) {
        return HttpRequest.newBuilder()
                        .uri(URI.create(URL))
                .header("Accept", "application/json")
                .GET()
                .build();
    }
}
