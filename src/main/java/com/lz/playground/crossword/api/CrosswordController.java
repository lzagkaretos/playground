package com.lz.playground.crossword.api;

import com.lz.playground.crossword.CrosswordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/crossword")
@CrossOrigin
public class CrosswordController {

    private final CrosswordService crosswordService;

    public CrosswordController(CrosswordService crosswordService) {
        this.crosswordService = crosswordService;
    }

    @GetMapping("/boards")
    public ResponseEntity getCrosswordBoards() {
        List<BoardDto> boards = this.crosswordService.getCrosswordBoards();
        return new ResponseEntity(boards, HttpStatus.OK);
    }

    @PostMapping("/generate")
    public ResponseEntity<CrosswordDto> generateCrossword(@RequestBody GenerateCrosswordDto generateCrossword) {
        CrosswordDto crossword = this.crosswordService.generateCrossword(generateCrossword);
        return new ResponseEntity(crossword, HttpStatus.OK);
    }

}
