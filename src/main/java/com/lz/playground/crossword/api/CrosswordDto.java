package com.lz.playground.crossword.api;

import java.util.List;

public record CrosswordDto(String[][] emptyBoard, String[][] board, List<WordSlotDto> wordSlots,
                           int totalCombinations) {
}
