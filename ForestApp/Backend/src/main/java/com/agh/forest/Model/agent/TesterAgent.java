package com.agh.forest.Model.agent;

import com.agh.forest.Model.ForestPixel;
import com.agh.forest.data.ForestSimulationApp;
import com.agh.forest.dto.MessageDto;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TesterAgent extends Agent {

    @Setter
    private ForestPixel[][] board;

    private List<ForestPixel> burning;
    @Override
    public List<MessageDto> sendMessage() {
        return messages;
    }

    public void createMessage(List<ForestPixel> pixels, String id, LocalDateTime time){
        String sensorName = "Tester Agent #" + id;
        if(pixels.isEmpty()){
            String warningText = "Agent przeszukał i przebadał teren i nie znalazł w otoczeniu piksela " + id + " zadnego pozaru";
            messages.add(new MessageDto(sensorName, time, warningText));
        }
        else {
            String ids = pixels.stream()
                    .map(pixel -> {
                        String idPixel = this.parseID(pixel.getId());
                        return idPixel + " -> " + Math.round(pixel.getForestFireIndexValue()) + "% \n";
                    })
                    .collect(Collectors.joining());
            String warningText = "Agent wykrył pożar na wybranych polach oraz oszacował straty pól. Przesłany raport  " + ids;
            messages.add(new MessageDto(sensorName, time, warningText));
        }

    }

    public List<ForestPixel> testForestField(ForestPixel pixel, LocalDateTime time) {
        List<ForestPixel> burningFields = this.createSurroundingsForPixel(this.getRowColNumberFromPixelId(pixel), board).stream()
                .flatMap(element -> this.createSurroundingsForPixel(this.getRowColNumberFromPixelId(pixel), board).stream())
                .distinct()
                .filter(ForestPixel::isBeingBurned)
                .collect(Collectors.toList());
        this.createMessage(burningFields, parseID(pixel.getId()), time);
        return burningFields;



    }
    public int calculateTime(ForestPixel pixel){
       return this.createSurroundingsForPixel(this.getRowColNumberFromPixelId(pixel), board).size();
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



}
