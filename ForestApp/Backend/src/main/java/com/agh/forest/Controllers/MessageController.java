package com.agh.forest.Controllers;

import com.agh.forest.Services.MessageService;
import com.agh.forest.dto.MessageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Data
@CrossOrigin("*")
@RestController
@AllArgsConstructor
public class MessageController {

    private MessageService messageService;

    @GetMapping("/messages")
    public ResponseEntity<List<MessageDto>> getMessages(){
        return new ResponseEntity<>(messageService.getAgentMessages(), HttpStatus.OK);
    }

}
