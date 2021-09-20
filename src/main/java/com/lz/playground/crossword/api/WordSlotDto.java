package com.lz.playground.crossword.api;

public record WordSlotDto(int startX, int startY, int length, String direction, String word) {
}
