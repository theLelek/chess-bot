package dev.lelek.chess.search;

import dev.lelek.chess.Move.Move;

record BoardResults(int score, Move move, boolean hasLost, boolean hasDrawn) {}

