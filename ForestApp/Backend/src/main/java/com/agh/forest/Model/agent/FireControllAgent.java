package com.agh.forest.Model.agent;

import com.agh.forest.Model.ForestPixel;
import com.agh.forest.dto.MessageDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Data
public class FireControllAgent extends Agent {

    private List<ForestPixel> burningPixels;
    private boolean isFire = false;
    private Map<String, Integer> currentlyExtinguishPixels = new HashMap<>();
    private Map<String, List<ForestPixel>> allBurningFields = new HashMap<>();
    private ForestPixel[][] board;
    private List<ForestPixel> pixelsVisited;
    @Override
    public List<MessageDto> sendMessage() {
        return this.messages;
    }
    public void createMessage(LocalDateTime time){
        allBurningFields.values()
                .stream()
                .map(entry -> (ArrayList<String>) entry.stream().map(ForestPixel::getId).map(this::parseID).collect(Collectors.toList()))
                .forEach(element -> {
                    String ids = String.join(" ", element);
                    MessageDto messageDto = new MessageDto();
                    messageDto.setDate(time);
                    messageDto.setText("Agent rozpoznał stan pożaru. Wszystkie pola, które podlegają pożarowi to:  " + ids);
                    messageDto.setSensor("Fire Controll Agent #1");
                    messages.add(messageDto);
                });

    }
    public void createExtinguishMessage(LocalDateTime time, String id ){

                    MessageDto messageDto = new MessageDto();
                    messageDto.setDate(time);
                    messageDto.setText("Agent ugasił pożar w w otoczeniu piskela:  " + id);
                    messageDto.setSensor("Fire Controll Agent #1");
                    messages.add(messageDto);

    }

    public List<ForestPixel> findAllBurningFields(ForestPixel pixel){
        burningPixels = new ArrayList<>();
        isFire = true;
        pixelsVisited = new ArrayList<>();
        recursionFindingFire(pixel);
        burningPixels.add(0, pixel);
        burningPixels = burningPixels.stream().distinct().collect(Collectors.toList());
        List<ForestPixel> sortedBurningPixels = burningPixels.stream().
                sorted(Comparator.comparing(ForestPixel::getId)).collect(Collectors.toList());

       String id = null;
       OUTER: for (Map.Entry<String, List<ForestPixel>> entry : allBurningFields.entrySet()) {
            for(ForestPixel forestPixel: entry.getValue()){
                for(ForestPixel burningPixel: burningPixels){
                    if (burningPixel.getId().equals(forestPixel.getId())) {
                        id = entry.getKey();
                        break OUTER;
                    }
                }
            }
        }
        if(id == null) id = pixel.getId();
        allBurningFields.put(id, burningPixels);

        return burningPixels;
    }

    public String findKeyByPixel(ForestPixel pixel){
        for (Map.Entry<String, List<ForestPixel>> entry : allBurningFields.entrySet()) {
          if(entry.getValue().contains(pixel)) return entry.getKey();
        }
        return null;
    }

    public void recursionFindingFire(ForestPixel pixel){
        pixelsVisited.add(pixel);
        List<ForestPixel> burningFields = this.createSurroundingsForPixel(this.getRowColNumberFromPixelId(pixel), board).stream()
                .flatMap(element -> this.createSurroundingsForPixel(this.getRowColNumberFromPixelId(pixel), board).stream())
                .distinct()
                .filter(element -> element.isBeingExtinguish() || element.isBeingBurned())
                .collect(Collectors.toList());
        burningFields = burningFields.stream()
                .filter(pixel2 -> !pixelsVisited.contains(pixel2))
                .collect(Collectors.toList());
        if(burningFields.size() == 0 ) return;
        burningPixels.addAll(burningFields);
        for (ForestPixel burningField : burningFields) {
            recursionFindingFire(burningField);
        }
    }
    public void updateBurningFields(){
        this.allBurningFields.entrySet()
                .forEach(element -> {
                    Optional<ForestPixel> first = Arrays.stream(board)
                            .flatMap(Arrays::stream)
                            .filter(pixel -> pixel.getId().equals(element.getValue().get(0).getId()))
                            .findFirst();
                    ForestPixel forestPixel = first.orElse(null);
                    this.findAllBurningFields(forestPixel);
                });
    }

    public void cleanUp(){
        Set<List<ForestPixel>> existing = new HashSet<>();
        allBurningFields = allBurningFields.entrySet()
                .stream()
                .filter(element ->
                        allBurningFields.entrySet().stream().
                                filter(x -> !element.getKey().equals(x.getKey()))
                                .noneMatch(x -> x.getValue().equals(element.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


    }


    private Long[] getRowColNumberFromPixelId(ForestPixel forestPixel){
        String[] split = forestPixel.getId().split(":");
        return  Arrays.stream(split)
                .map(Long::parseLong)
                .toArray(Long[]::new);
    }

    private int getMinRange(int range, int windowSize) {
        return IntStream.iterate(range, r-> r - 1)
                .limit(Math.round(windowSize/2.0))
                .filter(number -> number >= 0)
                .min()
                .orElse(0);
    }

    private int getMaxRange(int range, int windowSize, int maxRange) {
        return IntStream.iterate(range, r -> r +1)
                .limit(Math.round(windowSize / 2.0))
                .filter(number -> number <= maxRange)
                .max()
                .orElse(range);
    }
    private List<ForestPixel> createSurroundingsForPixel(Long[] rowAndColumnIndex, ForestPixel[][] board){

        final int WINDOW_SIZE = 3;
        final int COLUMN_SIZE_BOARD = 10;
        final int ROW_SIZE_BOARD = 10;

        int row = rowAndColumnIndex[0].intValue();
        int column = rowAndColumnIndex[1].intValue();
        List<ForestPixel> forestPixels = new ArrayList<>();
        int maxColumnRange = this.getMaxRange( column, WINDOW_SIZE, COLUMN_SIZE_BOARD-1);
        int minColumnRange = this.getMinRange(column, WINDOW_SIZE);
        int maxRowRange = this.getMaxRange(row, WINDOW_SIZE, ROW_SIZE_BOARD-1);
        int minRowRange = this.getMinRange(row, WINDOW_SIZE);
        for( int i = minRowRange; i<=maxRowRange; i++){
            forestPixels.addAll(Arrays.asList(board[i]).subList(minColumnRange, maxColumnRange + 1));
        }
        return forestPixels;

    }

    public void isFireExtinguished(LocalDateTime time, ExitAgent agent){

        List<String> id = this.getAllBurningFields().entrySet()
                .stream()
                .filter(element -> element.getValue()
                        .stream()
                        .noneMatch(ForestPixel::isBeingBurned))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        allBurningFields.entrySet()
                .stream()
                .filter(entry -> id.contains(entry.getKey()))
                .forEach(entry -> {
                    entry.getValue().forEach(element ->{
                        element.setBeingBurned(false);
                        element.setBeingExtinguish(false);
                    } );
                });
        id.forEach(element -> allBurningFields.remove(element));
        id.forEach(element -> createExtinguishMessage(time, element));
        id.forEach(element -> agent.createMessage(time));
        if(allBurningFields.isEmpty()) {
            isFire = false;
        }

    }
}
