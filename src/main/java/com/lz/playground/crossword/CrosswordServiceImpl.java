package com.lz.playground.crossword;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
class CrosswordServiceImpl implements CrosswordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrosswordServiceImpl.class);

    private final CrosswordGenerator crosswordGenerator;

    public CrosswordServiceImpl(CrosswordGenerator crosswordGenerator) {
        this.crosswordGenerator = crosswordGenerator;
    }

    @Override
    public Crossword generateCrossword() {
        LOGGER.debug("Generate crossword started");
        Crossword crossword = this.crosswordGenerator.run();
        LOGGER.debug("Generate crossword completed");
        return crossword;
    }

}
