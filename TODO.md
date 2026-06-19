use depth limited dfs for finding moves
https://www.youtube.com/watch?v=U4ogK0MIzqk = for general chess bot programming
https://www.dogeystamp.com/index.html = for general chess bot programming
https://www.youtube.com/watch?v=_vqlIPDR2TU = for advanced chess bot programming
https://www.youtube.com/watch?v=l-hh51ncgDI = for minimax and alpha beta pruning



write unmove function in Board Class

über function naming conventions reden (was ist besser: initialize... getBy...)
talk if PseudoLegalMoveFinder::getLegalPawnMoves() is good enough

copy method in classes or constructor to copy instances

replace board piece getting (getBoardPieces) with getBoardPiece

implement uci protocoll

maybe change Move class to bits

remove boolean paramether and replace them with Color enum

maybe add shortend fen support (shorter version of fen string)

add more logging


MoveGenerator:
1. order moves
2. iterative deepening with ab pruning
3. zobrist hash (tranposition tables)
