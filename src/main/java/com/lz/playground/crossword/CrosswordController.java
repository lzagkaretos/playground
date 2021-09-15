package com.lz.playground.crossword;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crossword")
public class CrosswordController {

    private final CrosswordService crosswordService;

    public CrosswordController(CrosswordService crosswordService) {
        this.crosswordService = crosswordService;
    }

    @GetMapping("/generate")
    public ResponseEntity<Crossword> generateCrossword() {
        Crossword crossword = this.crosswordService.generateCrossword();
        return new ResponseEntity(crossword, HttpStatus.OK);
    }

}
