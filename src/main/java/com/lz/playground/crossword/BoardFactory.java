package com.lz.playground.crossword;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
class BoardFactory {

    private final Map<Integer, Board> boards;

    BoardFactory() {
        this.boards = new HashMap<>();
        final String[][] boardA = {
                {"#", "-", "-", "-", "-", "#", "#", "-", "#"},
                {"-", "-", "#", "#", "-", "-", "-", "-", "-"},
                {"#", "-", "-", "-", "-", "#", "#", "-", "#"},
                {"#", "-", "#", "#", "-", "-", "-", "-", "-"},
                {"#", "-", "#", "#", "-", "#", "#", "-", "#"},
                {"-", "-", "-", "-", "-", "-", "-", "-", "-"},
        };
        final String[][] boardB = {
                {"#", "-", "-", "-", "-", "-", "-", "-", "-", "#", "-", "-", "-", "-", "#"},
                {"-", "#", "-", "#", "#", "#", "-", "#", "#", "#", "#", "#", "-", "#", "-"},
                {"-", "-", "-", "-", "-", "-", "-", "-", "-", "#", "#", "#", "#", "#", "-"},
                {"-", "#", "-", "#", "#", "#", "-", "#", "#", "#", "-", "-", "-", "-", "-"},
                {"-", "#", "-", "#", "-", "-", "-", "-", "-", "#", "#", "-", "#", "#", "-"},
                {"-", "#", "-", "#", "#", "#", "-", "#", "-", "#", "#", "-", "#", "#", "-"},
                {"-", "-", "-", "-", "-", "-", "-", "#", "-", "-", "-", "-", "-", "-", "-"},
                {"-", "#", "#", "-", "#", "#", "#", "#", "#", "#", "#", "-", "#", "#", "-"},
                {"#", "-", "#", "-", "#", "#", "-", "#", "-", "#", "#", "-", "#", "#", "-"},
                {"#", "-", "#", "-", "#", "#", "-", "#", "-", "#", "#", "-", "#", "#", "#"},
                {"-", "-", "-", "-", "-", "#", "-", "#", "-", "-", "-", "-", "-", "-", "-"},
                {"-", "#", "#", "-", "#", "#", "-", "#", "-", "#", "#", "-", "#", "#", "-"},
                {"-", "-", "-", "-", "-", "-", "-", "#", "-", "#", "-", "-", "-", "-", "-"},
                {"-", "#", "#", "-", "#", "#", "#", "#", "-", "#", "#", "-", "#", "#", "-"},
                {"-", "-", "-", "-", "-", "-", "#", "#", "-", "#", "-", "-", "-", "-", "-"},
        };
        final String[][] boardC = {
                {"#", "-", "-", "-", "-", "-", "-", "-", "#", "-", "-", "-", "-", "-", "-", "-", "-", "-"},
                {"-", "#", "-", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "-", "#", "#", "-"},
                {"-", "#", "-", "-", "-", "-", "-", "-", "-", "#", "#", "#", "#", "#", "-", "#", "#", "-"},
                {"-", "#", "-", "#", "#", "#", "-", "#", "#", "-", "-", "-", "#", "#", "-", "#", "#", "-"},
                {"-", "-", "-", "-", "-", "#", "-", "#", "#", "#", "#", "-", "#", "#", "-", "#", "#", "-"},
                {"-", "#", "-", "#", "-", "#", "-", "-", "-", "#", "#", "-", "-", "-", "-", "#", "-", "#"},
                {"-", "-", "-", "-", "-", "#", "-", "#", "-", "-", "-", "-", "#", "#", "-", "#", "-", "-"},
                {"-", "#", "-", "#", "-", "#", "-", "#", "-", "#", "#", "-", "#", "#", "-", "#", "-", "#"},
                {"-", "#", "-", "#", "-", "#", "#", "#", "#", "#", "#", "-", "#", "#", "-", "#", "-", "#"},
                {"-", "-", "-", "#", "-", "-", "-", "-", "-", "-", "-", "-", "#", "-", "-", "-", "-", "-"},
        };
        final String[][] boardD = {
                {"-", "-", "-", "-", "-", "-", "-", "-", "#", "-", "-", "-"},
                {"-", "#", "-", "#", "#", "#", "-", "#", "#", "#", "#", "#"},
                {"-", "#", "-", "-", "-", "-", "-", "-", "-", "#", "#", "#"},
                {"-", "#", "-", "#", "#", "#", "-", "#", "#", "-", "-", "-"},
                {"-", "#", "-", "#", "-", "#", "-", "#", "-", "#", "#", "-"},
                {"-", "#", "-", "#", "-", "#", "-", "#", "-", "#", "#", "-"},
                {"-", "#", "-", "#", "-", "#", "-", "#", "-", "#", "#", "-"},
                {"-", "#", "#", "#", "-", "#", "-", "#", "-", "#", "#", "-"},
                {"#", "#", "#", "#", "-", "#", "#", "#", "#", "#", "#", "-"},
                {"#", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"},
        };
        this.boards.put(1, new Board(1, "Small", boardA));
        this.boards.put(2, new Board(2, "Big", boardB));
        this.boards.put(3, new Board(3, "Flat", boardC));
        this.boards.put(4, new Board(4, "Medium", boardD));
    }

    List<Board> getBoards() {
        return this.boards.values().stream().toList();
    }

    Board getBoard(Integer boardId) {
        if (boardId == null) {
            // pick and return random board from the available ones
            return this.boards.get(new Random().nextInt(this.boards.size()) + 1);
        } else {
            return this.boards.get(boardId);
        }
    }

}
