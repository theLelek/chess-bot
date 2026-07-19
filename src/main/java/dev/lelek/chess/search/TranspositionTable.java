package dev.lelek.chess.search;

class TranspositionTable {

    public static final int SIZE = 65536;
    private static final TranspositionTable instance = new TranspositionTable();

    private final TranspositionTableEntry[] transpositionTable = new TranspositionTableEntry[SIZE];

    static {
        if (! (SIZE > 0 && Long.bitCount(SIZE) == 1)) {
            throw new ExceptionInInitializerError("SIZE must be a power of two: " + SIZE);
        }
    }

    private TranspositionTable() {
    }

    public static TranspositionTable getInstance() {
        return instance;
    }

    public TranspositionTableEntry getEntry(long zobristHash) {
        TranspositionTableEntry entry = transpositionTable[getIndex(zobristHash)];
        return (entry != null && entry.zobristHash() == zobristHash) ? entry : null;
    }

    public void setEntry(long zobristHash, TranspositionTableEntry entry) {
        transpositionTable[getIndex(zobristHash)] = entry;
    }

    private static int getIndex(long zobristHash) {
        return (int) (zobristHash & (SIZE - 1));
    }
}
