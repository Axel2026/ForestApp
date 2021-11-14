package com.agh.forest.Services;

import com.agh.forest.Model.AgentDashboard;
import com.agh.forest.data.ForestSimulationApp;
import com.agh.forest.dto.MessageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Data
public class MessageService {

    private AgentDashboard agentDashboard;

    public List<MessageDto> getAgentMessages(){
        return agentDashboard.getAgentMessages();

    }
}
