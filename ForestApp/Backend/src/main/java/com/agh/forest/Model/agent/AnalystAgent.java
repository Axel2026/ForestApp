package com.agh.forest.Model.agent;

import com.agh.forest.Model.ForestFireIndex;
import com.agh.forest.Model.ForestPixel;
import com.agh.forest.dto.MessageDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnalystAgent extends Agent{

    @Getter
    Map<String, List<ForestPixel>> dangerousForestPixels;
    Map<String, List<ForestPixel>> burningPixels;
    private List<String> previousBurningPixels = new ArrayList<>();
    private List<ForestPixel> burningPixelsList = new ArrayList<>();
    private BlockingDeque<ForestPixel> bq = new LinkedBlockingDeque<>();
    private boolean firstIteration = true;
    private final static double DANGEROUS_INDEX_VALUE = 16;

    public AnalystAgent() {
        dangerousForestPixels = new HashMap<>();
        burningPixels = new HashMap<>();
    }

    @Override
    public List<MessageDto> sendMessage() {
        return this.messages;

    }

    public void createMessage(LocalDateTime time) {
        if(firstIteration) {
            List<MessageDto> messagesToAdd = dangerousForestPixels.entrySet().stream()
                    .filter(element -> !element.getValue().isEmpty())
                    .map(entry -> {
                        String dangerousFieldNames = entry.getValue().stream()
                                .map(ForestPixel::getId)
                                .map(this::parseID)
                                .collect(Collectors.joining(" "));
                        String warningText = "Czujnik wykrył, że pola " + dangerousFieldNames + " stanowią wysokie " +
                                "zagrożenie wybuchnięcia pożaru ";
                        String sensorName = "Analyst Agent #" + parseID(entry.getKey());
                        return new MessageDto(sensorName, time, warningText);
                    })
                    .collect(Collectors.toList());
            List<MessageDto> burning = burningPixels.entrySet().stream()
                    .filter(element -> !element.getValue().isEmpty())
                    .map(entry -> {
                        String burningFieldNames = entry.getValue().stream()
                                .map(ForestPixel::getId)
                                .map(this::parseID)
                                .collect(Collectors.joining(" "));
                        String warningText = "Czujnik wykrył, że pola: " + burningFieldNames + " płoną !!!";
                        String sensorName = "Analyst Agent #" + parseID(entry.getKey());
                        return new MessageDto(sensorName, time, warningText);
                    })
                    .collect(Collectors.toList());

            messages.addAll(messagesToAdd);
            messages.addAll(burning);
            firstIteration = false;
        }
        else {
            String pixelID = burningPixelsList.stream()
                    .map(pixel -> parseID(pixel.getId()))
                    .collect(Collectors.joining(" "));
            String warningText = "Czujnik wykrył, że pola: " + pixelID + " płoną !!!";
            String sensorName = "Analyst Agent #" + 1;
            messages.add(new MessageDto(sensorName, time, warningText));
        }
    }

    public void analyzeForestFields(List<ForestPixel> pixels, String id){

        List<ForestPixel> filteredPixels = pixels.stream()
                .filter(pixel -> pixel.getForestFireIndexValue() > DANGEROUS_INDEX_VALUE)
                .collect(Collectors.toList());
        dangerousForestPixels.put(id, filteredPixels);

       List<ForestPixel> burningPixelsList = pixels.stream()
                .filter(ForestPixel::isBeingBurned)
                .collect(Collectors.toList());
       burningPixels.put(id, burningPixelsList);

    }
    public List<ForestPixel> sendDangerousForestPixels(){
        return this.dangerousForestPixels.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.toList());
    }
    public List<ForestPixel> getBurningPixelsList(){
        return this.burningPixels.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.toList());
    }

    public boolean checkDiffrencesBetweenPixels(){

        if(firstIteration) {
            previousBurningPixels = burningPixels.entrySet().stream().
                    flatMap(entry -> entry.getValue().stream()).map(ForestPixel::getId).collect(Collectors.toList());
            return true;
        }
        burningPixelsList = burningPixels.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream())
                .filter(element -> !previousBurningPixels.contains(element.getId()))
                .collect(Collectors.toList());

        previousBurningPixels = burningPixels.entrySet().stream().
                flatMap(entry -> entry.getValue().stream()).map(ForestPixel::getId).collect(Collectors.toList());
        return !burningPixelsList.isEmpty();
    }

}
