package com.agh.forest.Model.agent;


import com.agh.forest.Model.ForestPixel;
import lombok.Data;


@Data
public class FirefighterAgent extends Agent {
    private ForestPixel forestPixel;
    @Override
    public Object sendMessage() {
        return null;
    }
    public void setInitialStartForExtinguishFire(ForestPixel pixel)
    {
        this.forestPixel = pixel;
        forestPixel.setBeingExtinguish(true);
    }



}
