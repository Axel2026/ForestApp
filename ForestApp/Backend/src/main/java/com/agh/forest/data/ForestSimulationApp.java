package com.agh.forest.data;

import com.agh.forest.Model.AgentDashboard;
import com.agh.forest.Model.ForestPixel;
import com.agh.forest.Utilites.CreatingModels;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@Component
public class ForestSimulationApp {

    public final int ROW_SIZE_BOARD = 10;
    public final int COLUMN_SIZE_BOARD = 10;
    public final int WINDOW_SIZE = 3;

    @Getter
    private AgentDashboard agentDashboard;
    private BoardCalculation boardCalculation;
    @Getter
    @Setter
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(26);
    @Getter
    ForestPixel[][] board = new ForestPixel[ROW_SIZE_BOARD][COLUMN_SIZE_BOARD];


    ForestSimulationApp(AgentDashboard agentDashboard, BoardCalculation boardCalculation) {
        this.agentDashboard = agentDashboard;
        this.boardCalculation = boardCalculation;
        createBoard("London");
    }

    public void createBoard(String cityName) {
        ForestPixel pixel = null;
        try {
            pixel = CreatingModels.createForestPixelFromrApi(cityName);
            pixel.setId("0:0");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        int z = 0;
        for (int i = 0; i < ROW_SIZE_BOARD; i++) {
            for (int j = 0; j < COLUMN_SIZE_BOARD; j++) {
                board[i][j] = pixel.createSimilarPixel(COLUMN_SIZE_BOARD);
                board[i][j].setId(String.valueOf(i) + ":" + String.valueOf(j));
            }
        }
    }

    public void startSimulation() {

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                agentAndForestFireIteration();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }, 0, 4, TimeUnit.SECONDS);

        // agentDashboard.sendFieldsToAnalystAgent();
        //  Timer timer = new Timer();
        // scheduledExecutorService.scheduleAtFixedRate(this::calculateFireForestIteration, 0, 4, TimeUnit.SECONDS);
//       timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                calculateFireForestIteration();
//            }
//        }, 0, 2000);
    }

    public void agentAndForestFireIteration() throws InterruptedException {
        agentDashboard.sendFieldsToAnalystAgent(board);
        this.calculateFireForestIteration();
    }

    public List<ForestPixel> createSurroundingsForPixel(Long[] rowAndColumnIndex) {

        int row = rowAndColumnIndex[0].intValue();
        int column = rowAndColumnIndex[1].intValue();
        List<ForestPixel> forestPixels = new ArrayList<>();
        int maxColumnRange = boardCalculation.getMaxRange(column, WINDOW_SIZE, COLUMN_SIZE_BOARD - 1);
        int minColumnRange = boardCalculation.getMinRange(column, WINDOW_SIZE);
        int maxRowRange = boardCalculation.getMaxRange(row, WINDOW_SIZE, ROW_SIZE_BOARD - 1);
        int minRowRange = boardCalculation.getMinRange(row, WINDOW_SIZE);
        for (int i = minRowRange; i <= maxRowRange; i++) {
            forestPixels.addAll(Arrays.asList(board[i]).subList(minColumnRange, maxColumnRange + 1));
        }
        return forestPixels;

    }

    public void addSensorAgents(ForestPixel forestPixel) {
        agentDashboard.addSensorAgent(createSurroundingsForPixel(boardCalculation.getRowColNumberFromPixelId(forestPixel)),
                forestPixel.getId());
    }

    public void clearSensorAgents() {
        agentDashboard.removeSensorAgents();
    }


    private void calculateFireForestIteration() {
        System.out.println("XD");
        var copyBoard = makeCopyOfBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                var currentPixel = board[i][j];
                List<ForestPixel> surroundingsForPixel = createSurroundingsForPixel(boardCalculation.getRowColNumberFromPixelId(currentPixel));
                if (!currentPixel.isBeingBurned() && !currentPixel.isBeingExtinguish() && currentPixel.getFieldPercentageDestroyed() < 99) {
                    if (boardCalculation.calculateProbabilityOfStartingBurning(currentPixel, surroundingsForPixel)) {
                        copyBoard[i][j].setBeingBurned(true);
                        copyBoard[i][j].setFieldPercentageDestroyed(1);
                    }
                    if (boardCalculation.calculeOwnBurning(currentPixel)) {
                        copyBoard[i][j].setBeingBurned(true);
                        copyBoard[i][j].setFieldPercentageDestroyed(1);
                    }
                } else if (currentPixel.isBeingBurned()) {
                    var valueDestroyed = boardCalculation.calculateFireRising(currentPixel, surroundingsForPixel);
                    if (copyBoard[i][j].isBeingExtinguish()) {
                        valueDestroyed = valueDestroyed * (1 - copyBoard[i][j].convertForestFireExtiguishToValue() / 100.0);
                        copyBoard[i][j].setForestFireExtingush(copyBoard[i][j].getForestFireExtingush().getNext());
                        if (copyBoard[i][j].getFieldPercentageDestroyed() > 99.9) {
                            copyBoard[i][j].setBeingBurned(false);
                            String keyByPixel = agentDashboard.getFireControllAgent().findKeyByPixel(currentPixel);
                            agentDashboard.getFireControllAgent().getCurrentlyExtinguishPixels().compute(keyByPixel, (k, v) -> v == null ? v = 0 : v + 1);
                        }
                        if (valueDestroyed < 6) {
                            copyBoard[i][j].setBeingBurned(false);
                            String keyByPixel = agentDashboard.getFireControllAgent().findKeyByPixel(currentPixel);
                            agentDashboard.getFireControllAgent().getCurrentlyExtinguishPixels().compute(keyByPixel, (k, v) -> v == null ? v = 0 : v + 1);
                        }
                        valueDestroyed = valueDestroyed / 900 * 100;
                        copyBoard[i][j].setFieldPercentageDestroyed(currentPixel.getFieldPercentageDestroyed() + valueDestroyed);
                    } else {
                        valueDestroyed = valueDestroyed / 900 * 100;
                        copyBoard[i][j].setFieldPercentageDestroyed(currentPixel.getFieldPercentageDestroyed() + valueDestroyed);
                    }
                }

            }
        }
        this.board = copyBoard;
    }

    private ForestPixel[][] makeCopyOfBoard() {
        ForestPixel[][] newBoard = new ForestPixel[ROW_SIZE_BOARD][COLUMN_SIZE_BOARD];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                newBoard[i][j] = board[i][j].createCopy();
            }
        }
        return newBoard;
    }


}
