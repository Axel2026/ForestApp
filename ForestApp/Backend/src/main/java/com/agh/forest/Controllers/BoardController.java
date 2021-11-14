package com.agh.forest.Controllers;

import com.agh.forest.Services.BoardService;
import com.agh.forest.dto.BasicParamaters;
import com.agh.forest.dto.ForestPixelDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin("*")
@AllArgsConstructor
public class BoardController {

    private BoardService boardService;

    @GetMapping("/board")
    public ForestPixelDto[][] getPixels() throws IOException, InterruptedException {

        return boardService.getBoard();

    }

    @PostMapping("/board")
    public ResponseEntity<ForestPixelDto[][]> updateBoard(@RequestBody ForestPixelDto[][] forestPixelDtos) {
        boardService.updateBoard(forestPixelDtos);
        return new ResponseEntity<>(forestPixelDtos, HttpStatus.OK);
    }

    @PostMapping("/forms")
    public ResponseEntity<BasicParamaters> updateBasicParameters(@RequestBody BasicParamaters basicParamaters) {
        boardService.updateParameters(basicParamaters);
        return new ResponseEntity<>(basicParamaters, HttpStatus.OK);
    }
}
