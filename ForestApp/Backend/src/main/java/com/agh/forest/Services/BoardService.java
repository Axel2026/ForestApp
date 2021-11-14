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
import org.springframework.stereotype.Component;

import java.util.SortedMap;

@Component
@AllArgsConstructor
@Data
public class BoardService {

    ForestSimulationApp forestSimulationApp;
    static int z =0;

    public ForestPixelDto[][] getBoard(){

        ForestPixel[][] board = forestSimulationApp.getBoard();
        ForestPixelDto[][] boardDto = new ForestPixelDto[10][10];
        int id = 0;
        for (int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                boardDto[i][j] = mapForestPixeltoForestPixelDTO(board[i][j], id++);
            }
        }
        return boardDto;
    }
    public void updateBoard(ForestPixelDto[][] boardDto){
        ForestPixel[][] board = forestSimulationApp.getBoard();
        forestSimulationApp.clearSensorAgents();
        for (int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                board[i][j].setHasSensor(boardDto[i][j].isHasSensor());
                board[i][j].setPressure(boardDto[i][j].getPressure());
                board[i][j].setWind(new Wind(boardDto[i][j].getWindStrength(), boardDto[i][j].getWindDirection() ));
                board[i][j].convertEnumToFieldDestroyed(boardDto[i][j].getForestFireState());
                board[i][j].setBeingBurned(!boardDto[i][j].getForestFireState().equals(ForestFireState.NONE));
                if(board[i][j].isHasSensor()) forestSimulationApp.addSensorAgents(board[i][j]);
            }
        }
    }

    public ForestPixelDto mapForestPixeltoForestPixelDTO(ForestPixel pixel, int id){
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
                .id(id)
                .build();

    }

    private ForestFireIndex converForestFireValueToForestFireIndex(double value ){

        if(value < 5) return ForestFireIndex.LOW;
        else if(value < 12) return ForestFireIndex.MODERATE;
        else if (value < 25) return ForestFireIndex.HIGH;
        else if( value < 50 ) return ForestFireIndex.VERY_HIGH;
        else return ForestFireIndex.EXTREME;

    }

    public void updateParameters(BasicParamaters basicParamaters) {
        ForestPixel[][] board = forestSimulationApp.getBoard();

        if(basicParamaters.getHumidity() != 0.0){
            for (int i=0; i<10; i++){
                for(int j=0; j<10; j++){
                    board[i][j].setHumidity(basicParamaters.getHumidity());
                    board[i][j].setPressure(basicParamaters.getPressure());
                    board[i][j].getTemperature().setCurrent(basicParamaters.getTemperature());
                    board[i][j].calculateForestFireDAagerIndex();

                }
            }
        }
        else{

            for (int i=0; i<10; i++){
                for(int j=0; j<10; j++){
                    Wind wind = new Wind(basicParamaters.getWindStrength(), basicParamaters.getWindDirection());
                    board[i][j].setWind(wind);
                    board[i][j].calculateForestFireDAagerIndex();
                }
            }
        }

    }
}
