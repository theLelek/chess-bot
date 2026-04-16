[33m63c9401[m[33m ([m[1;36mHEAD[m[33m -> [m[1;32mtheLelek[m[33m, [m[1;31morigin/theLelek[m[33m)[m Merge pull request #23 from theLelek/main
[33m887e781[m[33m ([m[1;31morigin/main[m[33m, [m[1;31morigin/HEAD[m[33m, [m[1;32mmain[m[33m)[m changed legal moves structure in Board
[33mabc2d6f[m Merge pull request #22 from theLelek/theLelek
[33m7929c55[m added equals and hashCode
[33m4b30ea7[m gave get function clearer name (getBoardPiece)
[33ma120e30[m Merge pull request #21 from theLelek/theLelek
[33me8c7b81[m fixed typo (-Move to -move) began with updateBoardPiece function
[33md81fc3f[m began with structure of MoveGenerator
[33m35e303c[m updated todo
[33m10a4b59[m added getter
[33m25fed55[m added constructor for copying CastlingRights instance
[33m596ccc0[m Merge pull request #20 from theLelek/theLelek
[33m2a47690[m created 2 instance variables for castling rights in board (1 for each color) - changed CastlingRights class to only have rights of 1 color - rewrote unit tests to work for new castling rights
[33m464a5b7[m removed unecessary .gitkeep files
[33mb7e5a1e[m added basic board evaluation
[33mc01d4b7[m added unit tests for Move class
[33m73f0174[m Merge pull request #19 from theLelek/theLelek
[33m81dada9[m seperated legal move generation into colors - rewrote unit tests - created instance variable for legal moves
[33m6f8d9c0[m Merge pull request #18 from theLelek/theLelek
[33mccbbd76[m made api smaller - refactored for better readability - rewrote tests
[33m7544607[m fixed bug in move function and replaced magic number, >= instead of >
[33meca174f[m fixed error: BoardPosition builder build function x y were reversed - fixed unit test for BoardPosition
[33m317a091[m Merge pull request #16 from theLelek/TheOnlySensenmann
[33m5867263[m[33m ([m[1;31morigin/TheOnlySensenmann[m[33m)[m write PseudoLegalMoveFinder::getLegalPawnMoves() test
[33m93de3a3[m update TODO
[33ma6f104d[m write PseudoLegalMoveFinder::getLegalPawnMoves()
[33m03031c5[m write BoardPosition::getFromString() plus test
[33ma35fbb4[m Merge pull request #15 from theLelek/TheOnlySensenmann
[33mc018f57[m finish getLegalMoves except for pawns, fix Board.initializeBoardPiecesFromFen() write test
[33m9ca7684[m write find move in PseudoLegalMoveFinder, does still not work right, write test
[33m9af6ea2[m Merge pull request #14 from theLelek/theLelek
[33m66a921f[m began with pseudoLegalMoveFinder
[33me50bdb1[m Merge pull request #13 from theLelek/theLelek
[33mbaf6524[m changed Move builder to use 4 ints instead of 2 BoardPositions
[33mebb8ce7[m Merge pull request #12 from theLelek/theLelek
[33md84ed8d[m renamed variable rules to moveRules
[33m18edb8c[m removed unecessary move Arrays
[33m8ab64df[m added constant variable for size
[33m4e3c119[m Merge pull request #11 from theLelek/theLelek
[33m191ae92[m changed PieceMoveRules format from y, x to x, y - fixed bug: rook move rules were incorrect
[33mff7f3bc[m Merge pull request #10 from theLelek/TheOnlySensenmann
[33m4e0bdf8[m update TODO
[33mb452c8d[m finish PieceMoveRules - except Pawn, still in y/x form, need to change that
[33m47d0275[m add PieceMuveRules
[33m6f9e0bb[m change board - remove Enum PieceColor and change it to boolean
[33m982d020[m Update BoardPiece - remove not working fields
[33m4f9ce51[m Merge pull request #9 from theLelek/TheOnlySensenmann
[33md7d4f64[m Write BoardPosition and Move toString() with test
[33m97fa4d7[m Merge pull request #8 from theLelek/theLelek
[33m2bcc349[m added Builder to BoardPosition and Move class, changed Move class instance variables to BoardPosition,
[33m277c174[m moved Board classes into model package
[33m7b4597a[m Merge branch 'theLelek' of github.com:theLelek/chess-bot into theLelek
[33m4b4f7e5[m Merge pull request #7 from theLelek/theLelek
[33m7ddc95f[m added structure so that pseudo legal move finding can be implemented
[33maaa832e[m Merge pull request #6 from theLelek/theLelek
[33m94ea3ca[m refactored Board for better readability
[33m9224af4[m Merge pull request #5 from theLelek/TheOnlySensenmann
[33m607b40b[m Update Move::toString() - prints with chars instead of ints now, write test, remove TODO
[33m524b6e4[m Merge pull request #4 from theLelek/TheOnlySensenmann
[33m5611b6a[m Update TODO with Move::toString() TODO
[33m6455958[m Merge pull request #3 from theLelek/theLelek
[33m2606ceb[m added more parts for initializer of fen format
[33ma551269[m Merge pull request #2 from theLelek/theLelek
[33m780bdd6[m added TODOs to TODO.md
[33mb43a925[m added fen parsing with unit tests to Board
[33m7ae2b6c[m fixed bug where BLACK_KNIGHT had wrong fortsyth edwards notation
[33mb47299d[m Merge branch 'theLelek' of github.com:theLelek/chess-bot into theLelek
[33m5eb6b33[m added BoardPiece class
[33mafbbcef[m added TODO.md file
[33m50a7ae3[m Merge pull request #1 from theLelek/theLelek
[33m7e4962e[m added Move class and cli api for Move
[33m5a82f06[m changed cli to api
[33mc5ab0c2[m moved Main into src/ and added own Cli file
[33m7d1f802[m added .gitkeep files to untracked directorys and updated readme
[33mb543dc1[m added readme and added the test directoy
[33m4f942da[m Setup initial chess-bot project structure
[33m100f4e0[m first commit
