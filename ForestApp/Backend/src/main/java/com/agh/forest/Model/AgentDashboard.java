package com.agh.forest.Model;

import com.agh.forest.Model.agent.*;
import com.agh.forest.data.ForestSimulationApp;
import com.agh.forest.dto.MessageDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class AgentDashboard {


    private List<SensorAgent> sensorAgents;
    private List<FirefighterAgent> firefighterAgents;
    private AnalystAgent analystAgent;
    private ManagingAgent managingAgent;
    private ExitAgent exitAgent;
    private TesterAgent testerAgent;
    @Getter
    private FireControllAgent fireControllAgent;
    @Getter
    private LocalDateTime startingDate = LocalDateTime.now();
    @Getter
    private LocalDateTime date;
    private Random random = new Random();
    private List<ForestPixel> extinguishPixels = new ArrayList<>();
    private static int numberOfIteration = 0;

    public AgentDashboard(){
        sensorAgents = new ArrayList<>();
        analystAgent = new AnalystAgent();
        managingAgent = new ManagingAgent();
        exitAgent = new ExitAgent();
        testerAgent = new TesterAgent();
        fireControllAgent = new FireControllAgent();
        firefighterAgents = new ArrayList<>();
        date = LocalDateTime.now();
    }

    public void addSensorAgent(List<ForestPixel> agents, String id){
        SensorAgent sensorAgent = new SensorAgent();
        sensorAgent.setId(id);
        sensorAgent.setForestPixels(agents);
        sensorAgents.add(sensorAgent);
    }


    public void removeSensorAgents() {

        this.sensorAgents.clear();
    }
    public void sendFieldsToAnalystAgent(ForestPixel[][] board) throws InterruptedException {
        numberOfIteration++;
        boolean wasDiff = false;
        sensorAgents.forEach(agent -> {
                    agent.refreshData(board);
                    String id = agent.getId();
                    List<ForestPixel> forestPixels = agent.getForestPixels();
                    this.analystAgent.analyzeForestFields(forestPixels, id);
                });
        date = date.plusMinutes(getTimeDelay(4,9));
        System.out.println("Heh");
        int sum = sensorAgents.stream()
                .mapToInt(element -> element.getForestPixels().size())
                .sum();
        if(analystAgent.checkDiffrencesBetweenPixels()) {
            analystAgent.createMessage(date);
            Thread.sleep(1000);
            date = date.plusMinutes(getTimeDelay(3,7));
            managingAgent.computeTestingAgents(analystAgent.sendDangerousForestPixels(), date, analystAgent.getBurningPixelsList());
            Thread.sleep(1500);
            runTester(board);
            wasDiff = true;
        }
        else{
            date = date.plusMinutes(getTimeDelay(3,7));
            if(numberOfIteration % 10 == 0) {
                managingAgent.createMessage(false, date);
                date = date.plusMinutes(getTimeDelay(3,7));
                Thread.sleep(1500);
                List<ForestPixel> burningFields = new ArrayList<>();
                testerAgent.setBoard(board);
                managingAgent.getDangerousPixels().forEach(pixel -> burningFields.addAll(testerAgent.testForestField(pixel, date)));
                computingfireControllAgent(board, burningFields);
                wasDiff = true;

            }
        }
        if(fireControllAgent.isFire() && !wasDiff){
            fireControllAgent.setBoard(board);
            fireControllAgent.updateBurningFields();
            fireControllAgent.isFireExtinguished(date, exitAgent);
            this.processingFire();
        }

    }
    public List<MessageDto> getAgentMessages(){
        List<MessageDto> analystMessages = analystAgent.sendMessage();
        List<MessageDto> managingMessages =managingAgent.sendMessage();
        List<MessageDto> testerMessages =testerAgent.sendMessage();
        List<MessageDto> fireControllMessages =fireControllAgent.sendMessage();
        List<MessageDto> exitMessages =exitAgent.sendMessage();


        return Stream.of(analystMessages, managingMessages, testerMessages, fireControllMessages, exitMessages)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(MessageDto::getDate).reversed())
                .collect(Collectors.toList());

    }

    public void runTester(ForestPixel[][] board) throws InterruptedException {
        testerAgent.setBoard(board);

        Thread.sleep(1500);
        date = date.plusMinutes(getTimeDelay(3,7));
        List<ForestPixel> burningFields = new ArrayList<>();
         managingAgent.getPixelsWithDangerousNeighbours().
                forEach(pixel -> burningFields.addAll(testerAgent.testForestField(pixel, date)));
         managingAgent.getDangerousPixels().
                 forEach(pixel -> burningFields.addAll(testerAgent.testForestField(pixel, date)));
         managingAgent.getBurningPixels().forEach(pixel -> burningFields.addAll(testerAgent.testForestField(pixel, date)));
         //set board

        computingfireControllAgent(board, burningFields);

    }

    private void computingfireControllAgent(ForestPixel[][] board, List<ForestPixel> burningFields) {
        List<ForestPixel> burningFieldsDistinced = burningFields.stream()
                .distinct()
                .collect(Collectors.toList());
        fireControllAgent.setBoard(board);
        fireControllAgent.isFireExtinguished(date, exitAgent);
        for (ForestPixel forestPixel : burningFieldsDistinced) {
           fireControllAgent.findAllBurningFields(forestPixel);
        }
        fireControllAgent.cleanUp();
        fireControllAgent.createMessage(date);
        processingFire();

    }

    private void processingFire() {
        extinguishPixels = new ArrayList<>();
        fireControllAgent.getAllBurningFields().values()
                .forEach(field -> {
                    boolean isBeingExtinguish = field.stream()
                            .anyMatch(ForestPixel::isBeingExtinguish);
                    if(!isBeingExtinguish){
                        managingAgent.createMessage(date, Math.max(field.size()/3, 1), field.get(0).getId());
                       /* FirefighterAgent firefighterAgent = new FirefighterAgent();
                        firefighterAgent.setInitialStartForExtinguishFire(field.get(0));
                        firefighterAgents.add(firefighterAgent); */

                        this.addExtinguishFields(field);
                    }
                    else{
                        String id = fireControllAgent.findKeyByPixel(field.get(0));
                        int number = fireControllAgent.getCurrentlyExtinguishPixels().get(id);
                        if(number < field.size()){
                            extinguishPixels.addAll(field.stream().limit(number).collect(Collectors.toList()));
                        }
                        else{
                            extinguishPixels.addAll(field);
                        }

                    }
                });
        extinguishPixels = extinguishPixels.stream()
                .distinct()
                .map(element ->{
                    element.setBeingExtinguish(true);
                    return element;
                })
                .collect(Collectors.toList());
    }

    public int getTimeDelay(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    private void addExtinguishFields(List<ForestPixel> pixels){
        String id = fireControllAgent.findKeyByPixel(pixels.get(0));
        int number = Math.max(pixels.size()/3, 1);
        for(int i=0; i<number; i++){
            extinguishPixels.add(pixels.get(i));
            pixels.get(i).setBeingExtinguish(true);
        }
        fireControllAgent.getCurrentlyExtinguishPixels().put(id, number);
    }

    public List<ForestPixel> extinguishFields() {
        return this.extinguishPixels;

    }
    public void resetAgents(){
        sensorAgents = new ArrayList<>();
        analystAgent = new AnalystAgent();
        managingAgent = new ManagingAgent();
        exitAgent = new ExitAgent();
        testerAgent = new TesterAgent();
        fireControllAgent = new FireControllAgent();
        firefighterAgents = new ArrayList<>();
        date = LocalDateTime.now();
    }

}
