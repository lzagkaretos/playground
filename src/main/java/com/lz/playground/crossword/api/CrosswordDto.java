package com.lz.playground.crossword.api;

import java.util.List;

public record CrosswordDto(String identifier, char[][] emptyBoard, char[][] board, List<WordSlotDto> wordSlots,
                           int totalCombinations, long generationTime) {
}
