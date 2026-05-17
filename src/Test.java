import java.util.*;

public class Test {
    public static void main(String[] args) {
        long bitBoard = 0;
        bitBoard |= (1L << 5); // place piece on 0
        bitBoard &= ~(1L << 5); // removes piece on 5
        boolean occupied = (bitBoard & (1L << 5)) != 0; // check if bit in 5 is occupied
        bitBoard ^= (1L << 5); // reversed bit

        


    }
}
