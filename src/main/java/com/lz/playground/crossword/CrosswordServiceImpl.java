package com.lz.playground.crossword;

import com.lz.playground.crossword.api.BoardDto;
import com.lz.playground.crossword.api.CrosswordDto;
import com.lz.playground.crossword.api.GenerateCrosswordDto;
import com.lz.playground.crossword.api.WordSlotDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.UUID;

@Service
class CrosswordServiceImpl implements CrosswordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrosswordServiceImpl.class);

    private final BoardFactory boardFactory;

    private final CrosswordGenerator crosswordGenerator;

    public CrosswordServiceImpl(BoardFactory boardFactory, CrosswordGenerator crosswordGenerator) {
        this.boardFactory = boardFactory;
        this.crosswordGenerator = crosswordGenerator;
    }

    @Override
    public List<BoardDto> getCrosswordBoards() {
        return this.boardFactory.getBoards().stream().map(b -> new BoardDto(b.id(), b.name())).toList();
    }

    @Override
    public CrosswordDto generateCrossword(GenerateCrosswordDto generateCrossword) {
        LOGGER.debug("Generate crossword started");
        StopWatch timeMeasure = new StopWatch();
        timeMeasure.start("Crossword Generation");
        Crossword crossword = this.crosswordGenerator.run(generateCrossword.boardId());
        timeMeasure.stop();
        LOGGER.debug("Generate crossword completed in {} milliseconds", timeMeasure.getLastTaskTimeMillis());
        return new CrosswordDto(UUID.randomUUID().toString(), crossword.getEmptyBoard(),
                crossword.getBoard(),
                crossword.getWordSlots().stream()
                        .map(ws -> new WordSlotDto(ws.getStartX(), ws.getStartY(), ws.getLength(), ws.getDirection().name(), ws.getWord())).toList(),
                crossword.getFindWordCount(), timeMeasure.getLastTaskTimeMillis());
    }

}
