package com.agh.forest.Services;

import com.agh.forest.Model.ForestFireIndex;
import com.agh.forest.Model.ForestFireState;
import com.agh.forest.Model.ForestPixel;
import com.agh.forest.Model.Wind;
import com.agh.forest.data.ForestSimulationApp;
import com.agh.forest.dto.BasicParamaters;
import com.agh.forest.dto.ForestPixelDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Component
@AllArgsConstructor
@Data
public class BoardService {

    ForestSimulationApp forestSimulationApp;
    static int z = 0;
    static int iter = 0;
    static ArrayList<Integer> burningList = new ArrayList<Integer>();
    static ArrayList<Integer> iterList = new ArrayList<Integer>();
    static ArrayList<Integer> iterList2 = new ArrayList<Integer>();
    static ArrayList<Integer> destroyedList = new ArrayList<Integer>();


    public ForestPixelDto[][] getBoard() throws IOException {

        ForestPixel[][] board = forestSimulationApp.getBoard();
        ForestPixelDto[][] boardDto = new ForestPixelDto[10][10];
        int id = 0;
        iter++;

        BufferedWriter outStream4 = new BufferedWriter(new FileWriter("Activity.csv", true));
        File file4 = new File("Activity.csv");
        if (file4.length() == 0) {
        outStream4.append("Aktywnosc");
        outStream4.append(",");
        outStream4.append("Piksele");
        outStream4.append("\n");
        }
        outStream4.close();

        BufferedWriter outStream3 = new BufferedWriter(new FileWriter("Individuality2.csv", true));
        File file = new File("Individuality2.csv");
        if (file.length() == 0) {
            outStream3.append("Id pola");
            outStream3.append(" , ");
            outStream3.append("Aktualna temperatura");
            outStream3.append(" , ");
            outStream3.append("Temperatura min");
            outStream3.append(" , ");
            outStream3.append("Temperatura max");
            outStream3.append(" , ");
            outStream3.append("Posiada sensor");
            outStream3.append(" , ");
            outStream3.append("Predkość wiatru");
            outStream3.append(" , ");
            outStream3.append("Kierunek wiatru");
            outStream3.append(" , ");
            outStream3.append("Cisnienie");
            outStream3.append(" , ");
            outStream3.append("Wilgotnosc");
            outStream3.append(" , ");
            outStream3.append("Jakosc powietrza");
            outStream3.append(" , ");
            outStream3.append("CO");
            outStream3.append(" , ");
            outStream3.append("NO");
            outStream3.append(" , ");
            outStream3.append("NO2");
            outStream3.append(" , ");
            outStream3.append("O3");
            outStream3.append(" , ");
            outStream3.append("SO2");
            outStream3.append(" , ");
            outStream3.append("PM2_5");
            outStream3.append(" , ");
            outStream3.append("PM10");
            outStream3.append(" , ");
            outStream3.append("NH3");
            outStream3.append(" , ");
            outStream3.append("Indeks McArthura");
            outStream3.append(" , ");
            outStream3.append("Procent zniszczonych pol");
            outStream3.append(" , ");
            outStream3.append("Pali sie");
            outStream3.append(" , ");
            outStream3.append("Jest gaszone");
            outStream3.append(" , ");
            outStream3.append("Typ lasu");
            outStream3.append(" , ");
            outStream3.append("Stan ognia");
            outStream3.append(" , ");
            outStream3.append("Emocje Strazakow");
            outStream3.append("\n");
        }
        outStream3.close();

        BufferedWriter outStream2 = new BufferedWriter(new FileWriter("Akcja.csv", true));
        File file2 = new File("Akcja.csv");

        if (file2.length() == 0) {
            outStream2.append("Id gaszonego pola");
            outStream2.append(" , ");
            outStream2.append("Ilosc wysłanych jednostek");
            outStream2.append(" , ");
            outStream2.append("Data");
            outStream2.append("\n");
        }
        outStream2.close();

        BufferedWriter outStream = new BufferedWriter(new FileWriter("Individuality.csv", true));
        outStream.append("Id pola");
        outStream.append(" , ");
        outStream.append("Temperatura");
        outStream.append(" , ");
        outStream.append("Sila wiatru");
        outStream.append(" , ");
        outStream.append("Kierunek wiatru");
        outStream.append(" , ");
        outStream.append("Wilgotnosc");
        outStream.append(" , ");
        outStream.append("Cisnienie");
        outStream.append(" , ");
        outStream.append("id");
        outStream.append(" , ");
        outStream.append("Posiada sensor");
        outStream.append(" , ");
        outStream.append("Indeks pozaru lasu");
        outStream.append(" , ");
        outStream.append("Pali sie");
        outStream.append(" , ");
        outStream.append("Jest gaszone");
        outStream.append(" , ");
        outStream.append("Stan pozaru");
        outStream.append(" , ");
        outStream.append("Typ lasu");
        outStream.append(" , ");
        outStream.append("Emocje strazakow");
        outStream.append("\n");
        int burningSum = 0;
        int destroyedSum = 0;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                boardDto[i][j] = mapForestPixeltoForestPixelDTO(board[i][j], id++);
                outStream.append("[" + i + "]" + "[" + j + "] ," + String.join(" , ", boardDto[i][j].toString()).replace("ForestPixelDto(temperature=", "").replace("windStrength=", "").replace("windDirection=", "").replace("humidity=", "").replace("pressure=", "").replace("id=", "").replace("hasSensor=", "").replace("forestFireIndex=", "").replace("isBeingBurned=", "").replace("isBeingExtinguished=", "").replace("forestFireState=", "").replace(")", "").replace("forestType=", "").replace("firefighterEmotion=", ""));
                outStream.append("\n");
                if(boardDto[i][j].isBeingBurned()){
                    burningSum++;
                    iterList.add(iter);
                }

                if(ForestPixel.checkDestroyed(boardDto[i][j].getForestFireState())){
                    destroyedSum++;
                    iterList2.add(iter);
                }
            }
        }
        if(burningList.size() == 0){
            burningList.add(burningSum);
        }else if (burningList.get(burningList.size() - 1) != burningSum){
            burningList.add(burningSum);
        }

        if(destroyedList.size() == 0){
            destroyedList.add(destroyedSum);
            System.out.println(destroyedSum + " t1");
        }else if (destroyedList.get(destroyedList.size() - 1) != destroyedSum){
            destroyedList.add(destroyedSum);
            System.out.println(destroyedSum+ " t2");
        }
        outStream.append("Iteracja: " + iter);
        outStream.append("\n");
        outStream.close();
        return boardDto;
    }

    public static void createChart(){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(int i = 0; i < burningList.size(); i++){
            dataset.setValue(burningList.get(i), "Iteracja", iterList.get(i));
        }

        JFreeChart chart = ChartFactory.createLineChart("Palenie się pól",
                "Iteracja", "Ilość palących się pól", dataset, PlotOrientation.VERTICAL,
                false, true, false);

        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        for(int i = 0; i < destroyedList.size(); i++){
            dataset.setValue(destroyedList.get(i), "Iteracja", iterList2.get(i));
        }

        JFreeChart chart2 = ChartFactory.createLineChart("Zniszczone pola",
                "Iteracja", "Ilość zniszczonych pól", dataset2, PlotOrientation.VERTICAL,
                false, true, false);
        try {
            ChartUtils.saveChartAsJPEG(new File("src/main/java/com/agh/forest/charts/chartBurning.jpg"), chart, 700, 500);
            ChartUtils.saveChartAsJPEG(new File("src/main/java/com/agh/forest/charts/chartDestroyed.jpg"), chart2, 700, 500);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void updateBoard(ForestPixelDto[][] boardDto) throws IOException {
        ForestPixel[][] board = forestSimulationApp.getBoard();
        forestSimulationApp.clearSensorAgents();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j].setHasSensor(boardDto[i][j].isHasSensor());
                board[i][j].setPressure(boardDto[i][j].getPressure());
                board[i][j].setWind(new Wind(boardDto[i][j].getWindStrength(), boardDto[i][j].getWindDirection()));
                board[i][j].convertEnumToFieldDestroyed(boardDto[i][j].getForestFireState());
                board[i][j].setBeingBurned(!boardDto[i][j].getForestFireState().equals(ForestFireState.NONE));
                if (board[i][j].isHasSensor()) forestSimulationApp.addSensorAgents(board[i][j]);
            }
        }
    }

    public ForestPixelDto mapForestPixeltoForestPixelDTO(ForestPixel pixel, int id) {
        return ForestPixelDto.builder()
                .humidity(pixel.getHumidity())
                .pressure(pixel.getPressure())
                .windDirection(pixel.getWind().getDeg())
                .windStrength(pixel.getWind().getSpeed())
                .temperature(pixel.getTemperature().getCurrent())
                .hasSensor(pixel.isHasSensor())
                .forestFireIndex(converForestFireValueToForestFireIndex(pixel.getForestFireIndexValue()))
                .forestFireState(pixel.convertFieldDestroyedToEnum())
                .isBeingBurned(pixel.isBeingBurned())
                .isBeingExtinguished(pixel.isBeingExtinguish())
                .forestType(setForestType())
                .id(id)
                .build();

    }

    private String setForestType() {
        int number = (int) (Math.random() * 3) + 1;
        switch (number) {
            case 1:
                return "coniferous";
            case 2:
                return "deciduous";
            default:
                return "mixed";
        }
    }

    private ForestFireIndex converForestFireValueToForestFireIndex(double value) {

        if (value < 5) return ForestFireIndex.LOW;
        else if (value < 12) return ForestFireIndex.MODERATE;
        else if (value < 25) return ForestFireIndex.HIGH;
        else if (value < 50) return ForestFireIndex.VERY_HIGH;
        else return ForestFireIndex.EXTREME;

    }

    public void updateParameters(BasicParamaters basicParamaters) {
        ForestPixel[][] board = forestSimulationApp.getBoard();

        if (basicParamaters.getHumidity() != 0.0) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    board[i][j].setHumidity(basicParamaters.getHumidity());
                    board[i][j].setPressure(basicParamaters.getPressure());
                    board[i][j].getTemperature().setCurrent(basicParamaters.getTemperature());
                    board[i][j].calculateForestFireDAagerIndex();
                }
            }
        } else {

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    Wind wind = new Wind(basicParamaters.getWindStrength(), basicParamaters.getWindDirection());
                    board[i][j].setWind(wind);
                    board[i][j].calculateForestFireDAagerIndex();
                }
            }
        }

    }
}
