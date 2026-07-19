package dev.lelek.chess.search;

import dev.lelek.chess.Move.Move;

record TranspositionTableEntry(long zobristHash, Move move, int searchedDepth) { // todo not all of zobristHash needs to be stored

}