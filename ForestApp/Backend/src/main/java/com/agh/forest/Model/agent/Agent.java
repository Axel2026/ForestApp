package com.agh.forest.Model.agent;

import com.agh.forest.dto.MessageDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public abstract class Agent {

    protected String id;
    protected List<MessageDto> messages = new ArrayList<>();
    public abstract Object sendMessage();

    protected String parseID(String id){
        String[] split = id.split(":");
        return Arrays.stream(split)
                .map(Integer::parseInt)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
