package dev.lelek.chess.search;
class TranspositionTable { // static final int SIZE = 16_777_216;
    static final int SIZE = 134_217_728; // 2^27

    private static final TranspositionTable instance = new TranspositionTable();

    private TranspositionTableEntry[] transpositionTable = new TranspositionTableEntry[SIZE];

    static {
        if (! (SIZE > 0 && Long.bitCount(SIZE) == 1)) {
            throw new ExceptionInInitializerError("SIZE must be a power of two: " + SIZE);
        }
    }

    private TranspositionTable() {}

    static TranspositionTable getInstance() {
        return instance;
    }

    TranspositionTableEntry getEntry(long zobristHash) {
        TranspositionTableEntry entry = transpositionTable[getIndex(zobristHash)];
        return entry;
    }

    void setEntry(long zobristHash, TranspositionTableEntry entry) {
        transpositionTable[getIndex(zobristHash)] = entry;
    }

    void clear() {
        transpositionTable = new TranspositionTableEntry[SIZE];
    }

    private static int getIndex(long zobristHash) {
        return (int) (zobristHash & (SIZE - 1));
    }
}