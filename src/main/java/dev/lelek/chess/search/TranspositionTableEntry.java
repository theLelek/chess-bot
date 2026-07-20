package dev.lelek.chess.search;
// todo not all of zobristHash needs to be stored
record TranspositionTableEntry(long zobristHash, int searchedDepth, BoardResults boardResults) {}