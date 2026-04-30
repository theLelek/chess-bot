use depth limited dfs for finding moves
https://www.youtube.com/watch?v=U4ogK0MIzqk = for general chess bot programming
https://www.dogeystamp.com/index.html = for general chess bot programming
https://www.youtube.com/watch?v=_vqlIPDR2TU = for advanced chess bot programming
https://www.youtube.com/watch?v=l-hh51ncgDI = for minimax and alpha beta pruning


über function naming conventions reden (was ist besser: initialize... getBy...)
talk if PseudoLegalMoveFinder::getLegalPawnMoves() is good enough

refactor getPseudoLegalMoves

implement promotion, en passant, castling
find legal moves instead of pseudo legal
maybe use the same board instance in every move generator node (faster)
copy method in classes or constructor to copy instances

calculate legal moves in mimimax

right now CastlingRights class is used 2 times // todo change
- in board to know if pieces have moved
- for legal move finding to know if moves can be made

refactor BoardPiece (also maybe add starting row for colors)

write unmove function in Board Class