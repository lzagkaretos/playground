package com.lz.playground.crossword;

class WordSlot {

    private int id;

    private int startX;

    private int startY;

    private int length;

    private Direction direction;

    private String word;

    private boolean sortUsed;

    WordSlot(int id, int startX, int startY, int length, Direction direction) {
        this.id = id;
        this.startX = startX;
        this.startY = startY;
        this.length = length;
        this.direction = direction;
    }

    public int getId() {
        return id;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getLength() {
        return length;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isSortUsed() {
        return sortUsed;
    }

    public void setSortUsed(boolean sortUsed) {
        this.sortUsed = sortUsed;
    }

    @Override
    public String toString() {
        return "WordSlot{" +
                "id=" + id +
                ", word='" + word + '\'' +
                '}';
    }

}
