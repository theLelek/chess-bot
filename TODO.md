use depth limited dfs for finding moves
https://www.youtube.com/watch?v=U4ogK0MIzqk = for general chess bot programming
https://www.dogeystamp.com/index.html = for general chess bot programming
https://www.youtube.com/watch?v=_vqlIPDR2TU = for advanced chess bot programming
https://www.youtube.com/watch?v=l-hh51ncgDI = for minimax and alpha beta pruning



über function naming conventions reden (was ist besser: initialize... getBy...)
talk if PseudoLegalMoveFinder::getLegalPawnMoves() is good enough

copy method in classes or constructor to copy instances
replace board piece getting (getBoardPieces) with getBoardPiece
maybe change Move class to bits
remove boolean paramether and replace them with Color enum
maybe add shortend fen support (shorter version of fen string)
add helper function for converting arrays to bitboard indexed arrays
maybe rename BoardPiece enum fields to format: PAWN_WHITE

add draw because of insufficient material
add draw becauce of repetition
add Threefold repetition
add Fivefold repetition
correctly implement Fifty-move rule
add Seventy-five-move rule

IMPROVE LOGGING IMPORTANT
MoveGenerator.generateMove can return null!!!