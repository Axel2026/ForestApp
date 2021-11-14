package com.agh.forest.Model.agent;

import com.agh.forest.dto.MessageDto;

import java.time.LocalDateTime;
import java.util.List;


public class ExitAgent extends  Agent {

    @Override
    public List<MessageDto> sendMessage() {
        return this.messages;
    }

    public void createMessage(LocalDateTime time){
        MessageDto messageDto = new MessageDto();
        messageDto.setDate(time.plusMinutes(1L));
        messageDto.setText("Agent prowadzi operację wycofywania jednostek po operacji gaszenia ognia! ");
        messageDto.setSensor("Exit Agent #1");
        messages.add(messageDto);
    }
}
