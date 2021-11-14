package com.agh.forest.Model.agent;

import com.agh.forest.Model.ForestPixel;
import com.agh.forest.dto.MessageDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ManagingAgent extends Agent{

    private static final int DANGEROUS_FOREST_VALUE = 40;
    private static final int MIN_FOREST_VALUE_FOR_TESTERS = 22;
    @Getter
    private List<ForestPixel> dangerousPixels = new ArrayList<>();
    @Getter
    private List<ForestPixel> pixelsWithDangerousNeighbours = new ArrayList<>();
    private List<ForestPixel> avoidedPixels = new ArrayList<>();
    @Getter
    private List<ForestPixel> burningPixels = new ArrayList<>();
    @Override
    public List<MessageDto>  sendMessage() {
        return messages;
    }

    public void createMessage(boolean hasChanges, LocalDateTime time) {
        if(hasChanges){
            if(!dangerousPixels.isEmpty()){
                String idPixels = getIdsPixelsFromPixelList(dangerousPixels);
                MessageDto messageDto = new MessageDto();
                messageDto.setDate(time);
                messageDto.setText("Agent po analizie stwierdził, że pola " + idPixels + " mają wysokie " +
                        "prawdopodobienstwo wystąpienia pożaru, ze wzgledu na bardzo wysoki współczynnik McArthura. W związku z tym " +
                        "wysyła testerów ");
                messageDto.setSensor("Manage Agent #1");
                messages.add(messageDto);
            }


            if(!pixelsWithDangerousNeighbours.isEmpty()){
                String idPixels = getIdsPixelsFromPixelList(pixelsWithDangerousNeighbours);
                MessageDto messageDto = new MessageDto();
                messageDto.setDate(time);
                messageDto.setText("Agent po analizie stwierdził, że pola " + idPixels + " mają wysokie " +
                        "prawdopodobienstwo wystąpienia pożaru, ze wzgledu dość wysoki współczynnik McArthura, ale sąsiądująca okolica również" +
                        "posiada wysoki wspolczynnik W związku z tym " + "wysyła testerów ");
                messageDto.setSensor("Manage Agent #1");
                messages.add(messageDto);
            }
            if(!avoidedPixels.isEmpty()){
                String idPixels = getIdsPixelsFromPixelList(avoidedPixels);
                MessageDto messageDto = new MessageDto();
                messageDto.setDate(time);
                messageDto.setText("Agent po analizie stwierdził, że pola " + idPixels + "  nie stanowią wystarczającego zagrożenia." +
                        " W związku z tym nie wysyła testerów ");
                messageDto.setSensor("Manage Agent #1");
                messages.add(messageDto);
            }
            if(!burningPixels.isEmpty()){
                String idPixels = getIdsPixelsFromPixelList(avoidedPixels);
                MessageDto messageDto = new MessageDto();
                messageDto.setDate(time);
                messageDto.setText("Agent dostał informację, że pola " + idPixels + "  są pod wpływem ognia." +
                        " W związku z tym wysyła testerów dla potwiedzenia informacji! ");
                messageDto.setSensor("Manage Agent #1");
                messages.add(messageDto);
            }
        }
        else{
            String idPixels = getIdsPixelsFromPixelList(dangerousPixels);
            MessageDto messageDto = new MessageDto();
            messageDto.setDate(time);
            messageDto.setText("Cykliczne wysyłanie testerów do pól : " + idPixels + " gdyż mają wysokie " +
                    "prawdopodobienstwo wystąpienia pożaru, ze wzgledu na bardzo wysoki współczynnik McArthura. ");
            messageDto.setSensor("Manage Agent #1");
            messages.add(messageDto);
        }

    }

    public void createMessage(LocalDateTime time, int numbersOfCars, String id){
        MessageDto messageDto = new MessageDto();
        messageDto.setDate(time);
        messageDto.setText("Agent po analizie stwierdził, że posyła  " + numbersOfCars + " wozów strażackich na miejsce interwencji " +
                " w pobliżu punktu: " + this.parseID(id));
        messageDto.setSensor("Manage Agent #1");
        messages.add(messageDto);
    }


    private String getIdsPixelsFromPixelList(List<ForestPixel> list) {
        return list.stream()
                .map(pixel -> this.parseID(pixel.getId()))
                .collect(Collectors.joining(" "));
    }

    public void computeTestingAgents(List<ForestPixel> pixels, LocalDateTime time, List<ForestPixel> burningPixels){
        calculateDangerousFieldAtFixedRate(pixels);
        pixelsWithDangerousNeighbours = pixels.stream()
                .filter(pixel -> pixel.getForestFireIndexValue() < DANGEROUS_FOREST_VALUE &&
                        pixel.getForestFireIndexValue() > MIN_FOREST_VALUE_FOR_TESTERS)
                .filter(pixel -> this.calculateNumberOfNeighbours(pixel, pixels) > 2)
                .collect(Collectors.toList());
        avoidedPixels = pixels.stream()
                .filter(pixel -> pixel.getForestFireIndexValue() < DANGEROUS_FOREST_VALUE)
                .filter(pixel -> this.calculateNumberOfNeighbours(pixel, pixels) <= 2)
                .collect(Collectors.toList());
        this.burningPixels = burningPixels;
        createMessage(true, time);

    }


    private void calculateDangerousFieldAtFixedRate(List<ForestPixel> pixels) {
        dangerousPixels = pixels.stream()
                .filter(pixel -> pixel.getForestFireIndexValue() > DANGEROUS_FOREST_VALUE)
                .collect(Collectors.toList());
    }

    public int calculateNumberOfNeighbours(ForestPixel pixel, List<ForestPixel> pixels){
        int numberOfNeighbours = 0;
        Integer[] rowAndColumndId = this.getRowAndColumndIdFromPixel(pixel);
        for(int i=rowAndColumndId[0]-1; i<=rowAndColumndId[0] + 1; i++){
            for(int j=rowAndColumndId[1] - 1; j<= rowAndColumndId[1] + 1; j++){
                String id = i + ":" + j;
                if(pixel.getId().equals(id)) continue;
                boolean hasNeigbours =  pixels.stream()
                        .map(ForestPixel::getId)
                        .anyMatch(ids -> ids.equals(id));
                if(hasNeigbours) numberOfNeighbours ++;
            }
        }
        return numberOfNeighbours;

    }
    private Integer [] getRowAndColumndIdFromPixel(ForestPixel pixel){
        String[] split = pixel.getId().split(":");
        return  Arrays.stream(split)
                .map(Integer::parseInt)
                .toArray(Integer[]::new);
    }


}
