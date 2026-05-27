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


replace board piece getting (getBoardPieces) with getBoardPiece

refactor BoardPiece (also maybe add starting row for colors)

write unmove function in Board Class
        
using a color enum would make things A LOT SIMPLER especially for move function in board (home rank, back rank, ... could all be put into color enum)

maybe replace Move class with an interface (especially CastlingMove class is annoying with inheritance)

implement uci protocoll



move more logic out of BoardPiece enum

add getColor to boardPiece enum

in BoardPosition add function to check if its in bounds 

implement zobrist hash

merge PieceBitBoard into BoardPiece - maybe use 2 enums 1 extra enum for perfomance elements like all pieces, all white pieces, all black pieces



maybe create interface which is implemented by BoardPiece and PieceBitBoard so geting bitboard index becomes easier