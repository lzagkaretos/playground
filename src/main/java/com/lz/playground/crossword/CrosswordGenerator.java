package com.lz.playground.crossword;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
class CrosswordGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrosswordGenerator.class);

    private final BoardFactory boardFactory;

    private final DictionaryFactory dictionaryFactory;

    public CrosswordGenerator(BoardFactory boardFactory, DictionaryFactory dictionaryFactory) {
        this.boardFactory = boardFactory;
        this.dictionaryFactory = dictionaryFactory;
    }

    Crossword run() {
        String[][] board = this.boardFactory.getBoard();
        LOGGER.info("Board size: {} x {}", board.length, board[0].length);

        String[] dictionary = this.dictionaryFactory.getDictionary();
        LOGGER.info("Dictionary: {}", dictionary.length);

        Crossword crossword = new Crossword(board);
        String[] filteredDictionary = this.filterDictionary(this.dictionaryFactory.getDictionary(), crossword.getWordSlots());
        LOGGER.info("Filtered dictionary: {}", filteredDictionary.length);
        LOGGER.info("Word slots: {}", crossword.getWordSlots().size());

        crossword.setDictionary(filteredDictionary);
        crossword.generate();

        return crossword;
    }

    private String[] filterDictionary(String[] dictionary, List<WordSlot> wordSlots) {
        Set<Integer> setOfLengths = wordSlots.stream().map(wordSlot -> wordSlot.getLength()).collect(Collectors.toSet());
        List<String> collect = Arrays.stream(dictionary)
                .filter(w -> setOfLengths.contains(w.length())).collect(Collectors.toList());
        return collect.toArray(new String[0]);
    }

}
