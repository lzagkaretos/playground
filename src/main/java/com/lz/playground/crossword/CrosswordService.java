package com.lz.playground.crossword;

import com.lz.playground.crossword.api.BoardDto;
import com.lz.playground.crossword.api.CrosswordDto;
import com.lz.playground.crossword.api.GenerateCrosswordDto;

import java.util.List;

public interface CrosswordService {

    List<BoardDto> getCrosswordBoards();

    CrosswordDto generateCrossword(GenerateCrosswordDto generateCrossword);

}
