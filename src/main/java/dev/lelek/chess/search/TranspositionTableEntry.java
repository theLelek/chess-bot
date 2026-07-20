package dev.lelek.chess.search;

import dev.lelek.chess.Move.Move;

// todo not all of zobristHash needs to be stored
record TranspositionTableEntry(long zobristHash, int searchedDepth, Move move, int score) {}