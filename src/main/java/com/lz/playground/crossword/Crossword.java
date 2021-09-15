package com.lz.playground.crossword;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

class Crossword {

    private static final Logger LOGGER = LoggerFactory.getLogger(Crossword.class);

    private String[][] emptyBoard;
    private String[][] board;
    private List<WordSlot> wordSlots;
    private List<WordSlot> sortedWordSlots;
    private String[] dictionary;
    private Map<Integer, List<String>> dictionaryMap;
    private int findWordCount = 0;

    public Crossword(String[][] board) {
        this.emptyBoard = Arrays.stream(board).map(String[]::clone).toArray(String[][]::new);
        this.board = Arrays.stream(board).map(String[]::clone).toArray(String[][]::new);
        this.wordSlots = initWordSlots(this.board);
        this.sortedWordSlots = new ArrayList<>();
        this.sortWordSlotsMain();
    }

    public String[][] getEmptyBoard() {
        return emptyBoard;
    }

    public String[][] getBoard() {
        return board;
    }

    public List<WordSlot> getWordSlots() {
        return wordSlots;
    }

    public int getFindWordCount() {
        return findWordCount;
    }

    public void setDictionary(String[] dictionary) {
        this.dictionary = dictionary;
        this.dictionaryMap = Arrays.stream(this.dictionary).collect(Collectors.groupingBy(String::length));
    }

    /**
     * Calculate available word slots in the board<br/>
     * "-" -> word character should exist<br/>
     * "#" -> word character should not exist
     *
     * @param board
     * @return
     */
    private List<WordSlot> initWordSlots(String[][] board) {
        final List<WordSlot> wordSlots = new ArrayList<>();
        int slotId = 0, startX = 0, startY = 0, length = 0;

        // horizontal process
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].equals("-")) {
                    if (startX == 0 && length == 0) {
                        startX = i;
                        startY = j;
                    }
                    length = length + 1;
                } else if (board[i][j].equals("#")) {
                    if (length > 1) {
                        wordSlots.add(new WordSlot(++slotId, startX, startY, length, Direction.HORIZONTAL));
                        startX = 0;
                        startY = 0;
                        length = 0;
                    } else {
                        startX = 0;
                        startY = 0;
                        length = 0;
                    }
                }
            }
            if (length > 1) {
                wordSlots.add(new WordSlot(++slotId, startX, startY, length, Direction.HORIZONTAL));
                startX = 0;
                startY = 0;
                length = 0;
            } else {
                startX = 0;
                startY = 0;
                length = 0;
            }
        }

        startX = 0;
        startY = 0;
        length = 0;
        // vertical process
        for (int j = 0; j < board[0].length; j++) {
            for (int i = 0; i < board.length; i++) {
                if (board[i][j].equals("-")) {
                    if (startY == 0 && length == 0) {
                        startX = i;
                        startY = j;
                    }
                    length = length + 1;
                } else if (board[i][j].equals("#")) {
                    if (length > 1) {
                        wordSlots.add(new WordSlot(++slotId, startX, startY, length, Direction.VERTICAL));
                        startX = 0;
                        startY = 0;
                        length = 0;
                    } else {
                        startX = 0;
                        startY = 0;
                        length = 0;
                    }
                }
            }
            if (length > 1) {
                wordSlots.add(new WordSlot(++slotId, startX, startY, length, Direction.VERTICAL));
                startX = 0;
                startY = 0;
                length = 0;
            } else {
                startX = 0;
                startY = 0;
                length = 0;
            }
        }
        return wordSlots;
    }

    private void sortWordSlotsMain() {
        Optional<WordSlot> maxLengthWordSlot = this.wordSlots.stream().filter(w -> !w.isSortUsed()).max(Comparator.comparingInt(WordSlot::getLength));
        while (maxLengthWordSlot.isPresent()) {
            this.sortWordSlots(maxLengthWordSlot.get());
            maxLengthWordSlot = this.wordSlots.stream().filter(w -> !w.isSortUsed()).max(Comparator.comparingInt(WordSlot::getLength));
        }
    }

    private void sortWordSlots(WordSlot wordSlot) {
        if (wordSlot == null) {
            return;
        }
        wordSlot.setSortUsed(true);
        this.sortedWordSlots.add(wordSlot);
        if (wordSlot.getDirection() == Direction.HORIZONTAL) {
            for (WordSlot slot : this.wordSlots) {
                if (slot.getId() != wordSlot.getId() && slot.getDirection() == Direction.VERTICAL &&
                        !slot.isSortUsed() && slot.getStartX() <= wordSlot.getStartX() &&
                        slot.getStartX() + slot.getLength() >= wordSlot.getStartX() &&
                        slot.getStartY() >= wordSlot.getStartY() && slot.getStartY() <= wordSlot.getStartY() + wordSlot.getLength()) {
                    this.sortWordSlots(slot);
                }
            }
            this.sortWordSlots(null);
        } else {
            for (WordSlot slot : this.wordSlots) {
                if (slot.getId() != wordSlot.getId() && slot.getDirection() == Direction.HORIZONTAL &&
                        !slot.isSortUsed() && slot.getStartY() <= wordSlot.getStartY() &&
                        slot.getStartY() + slot.getLength() >= wordSlot.getStartY() &&
                        slot.getStartX() >= wordSlot.getStartX() && slot.getStartX() <= wordSlot.getStartX() + wordSlot.getLength()) {
                    this.sortWordSlots(slot);
                }
            }
            this.sortWordSlots(null);
        }
    }

    void generate() {
        try {
            this.wordSlots = this.sortedWordSlots;
            this.findWord(0);
        } catch (SolutionFound solutionFound) {
            return;
        }
    }

    private void findWord(int slotIndex) {
        this.findWordCount++;
        if (this.findWordCount % 1000000 == 0) {
            this.printBoard(false);
        }
        if (this.solutionFound()) {
            LOGGER.debug("Solution found");
            this.printBoard(false);
            throw new SolutionFound();
        }

        WordSlot wordSlot = this.wordSlots.get(slotIndex);
        List<String> candidateWords = this.dictionaryMap.get(wordSlot.getLength());
        Collections.shuffle(candidateWords);

        // find if filled intersections exist and exclude not matched candidates
        List<IntersectionPoint> intersectionPoints = computeIntersectionPoints(wordSlot);
        for (IntersectionPoint ip : intersectionPoints) {
            candidateWords = candidateWords.stream().filter(cw ->
                    String.valueOf(cw.charAt(ip.getIndex())).equals(ip.getCharacter())).collect(Collectors.toList());
        }

        for (int i = 0; i < candidateWords.size(); i++) {
            if (isValid(slotIndex, candidateWords.get(i))) {
                // place word
                wordSlot.setWord(candidateWords.get(i));
                this.updateWordSlotsOnBoard();
                // move to next word slot
                this.findWord(slotIndex + 1);
                // backtrack
                wordSlot.setWord(null);
                this.updateWordSlotsOnBoard();
            }
        }
    }

    private void printBoard(boolean showIds) {
        StringBuilder logBoardBuilder = new StringBuilder("\n");
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                if (showIds) {
                    int finalI = i;
                    int finalJ = j;
                    Optional<WordSlot> first = this.wordSlots.stream()
                            .filter(w -> w.getStartX() == finalI && w.getStartY() == finalJ).findFirst();
                    if (first.isPresent()) {
                        logBoardBuilder.append(first.get().getId() + " ");
                        continue;
                    }
                }
                logBoardBuilder.append(this.board[i][j] + " ");
            }
            logBoardBuilder.append("\n");
        }
        logBoardBuilder.append("\n");
        LOGGER.debug("Find word counter: {}", findWordCount);
        LOGGER.debug(logBoardBuilder.toString());
    }

    private boolean solutionFound() {
        Optional<WordSlot> any = wordSlots.stream().filter(ws -> ws.getWord() == null).findAny();
        return !any.isPresent();
    }

    private List<IntersectionPoint> computeIntersectionPoints(WordSlot wordSlot) {
        List<IntersectionPoint> intersectionPoints = new ArrayList<>();
        if (wordSlot.getDirection() == Direction.HORIZONTAL) {
            for (int i = 0; i < wordSlot.getLength(); i++) {
                if (!this.board[wordSlot.getStartX()][wordSlot.getStartY() + i].equals("-") &&
                        !this.board[wordSlot.getStartX()][wordSlot.getStartY() + i].equals("#")) {
                    intersectionPoints.add(new IntersectionPoint(i, this.board[wordSlot.getStartX()][wordSlot.getStartY() + i]));
                }
            }
        } else {
            for (int i = 0; i < wordSlot.getLength(); i++) {
                if (!this.board[wordSlot.getStartX() + i][wordSlot.getStartY()].equals("-") &&
                        !this.board[wordSlot.getStartX() + i][wordSlot.getStartY()].equals("#")) {
                    intersectionPoints.add(new IntersectionPoint(i, this.board[wordSlot.getStartX() + i][wordSlot.getStartY()]));
                }
            }
        }
        return intersectionPoints;
    }

    private boolean isValid(int slotIndex, String currentWord) {
        // if already used
        boolean used = this.wordSlots.stream().anyMatch(ws -> currentWord.equals(ws.getWord()));
        if (used) {
            return false;
        }
        // if length does not match
        WordSlot wordSlot = this.wordSlots.get(slotIndex);
        if (wordSlot.getLength() != currentWord.length()) {
            return false;
        }
        // if does not fit with current board state
        if (wordSlot.getDirection() == Direction.HORIZONTAL) {
            for (int i = 0; i < wordSlot.getLength(); i++) {
                if (this.board[wordSlot.getStartX()][wordSlot.getStartY() + i] != "-" &&
                        !this.board[wordSlot.getStartX()][wordSlot.getStartY() + i].equals(String.valueOf(currentWord.charAt(i)))) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < wordSlot.getLength(); i++) {
                if (this.board[wordSlot.getStartX() + i][wordSlot.getStartY()] != "-" &&
                        !this.board[wordSlot.getStartX() + i][wordSlot.getStartY()].equals(String.valueOf(currentWord.charAt(i)))) {
                    return false;
                }
            }
        }
        return true;
    }

    private void updateWordSlotsOnBoard() {
        this.board = Arrays.stream(this.emptyBoard).map(String[]::clone).toArray(String[][]::new);
        this.wordSlots.stream().filter(wordSlot -> wordSlot.getWord() != null).forEach(
                wordSlot -> {
                    if (wordSlot.getDirection() == Direction.HORIZONTAL) {
                        for (int i = 0; i < wordSlot.getLength(); i++) {
                            this.board[wordSlot.getStartX()][wordSlot.getStartY() + i] =
                                    String.valueOf(wordSlot.getWord().charAt(i));
                        }
                    } else {
                        for (int i = 0; i < wordSlot.getLength(); i++) {
                            this.board[wordSlot.getStartX() + i][wordSlot.getStartY()] =
                                    String.valueOf(wordSlot.getWord().charAt(i));
                        }
                    }
                }
        );
    }

    private class IntersectionPoint {
        private int index;
        private String character;

        public IntersectionPoint(int index, String character) {
            this.index = index;
            this.character = character;
        }

        public int getIndex() {
            return index;
        }

        public String getCharacter() {
            return character;
        }
    }

}
