public class Board implements Cloneable {
    private long current_position = 0; // Bitwise representation of what spots in the board the current player filled
    private long mask = 0; // Bitwise representation of all the spots in the board that are filled
    private int numMoves = 0; // Number of Moves


    // Initially the board is empty and no moves have been made
    public Board() {
        current_position  = 0;
        mask = 0;
        numMoves = 0;
    }

    // set value of current_position
    public void setCurrentPosition(long pos) {
        current_position = pos;
    }

    // set value of mask 
    public void setMask(long pos) {
        mask = pos;
    }

    // set value of numMoves
    public void setMoves(int pos) {
        numMoves = pos;
    }

    // Returns number of moves played 
    public int getNumMoves() {
        return numMoves;
    }

    // Checks if col is a valid move 
    public boolean canPlay(int col) {
        return (mask & top(col)) == 0;
    }

    // Update the board to include the new move
    public void play(int col) {
        current_position = current_position ^ mask;
        mask |= mask + bottom(col);
        numMoves++;
    }

    // Checks to see if col is a winning move 
    public boolean isWinningMove(int col) {
        long pos = current_position;
        pos = pos | (mask + bottom(col)) & column(col);
        return alignment(pos);
    }

    // Return the sum of current_postiion and mask which can be used
    // as a unique identifier of the current board 
    public long key() {
        return current_position + mask;
    }

    // Returns a clone of the current board
    public Board cloneBoard() {
        Board clone = new Board();
        clone.setCurrentPosition(current_position);
        clone.setMask(mask);
        clone.setMoves(numMoves);
        return clone;
    }

    // Checks to see if the current player has won
    public static boolean alignment(long pos) {
        // Horizontal
        long x = pos & (pos >> 7);
        if ((x & (x >> 14)) != 0) {
            return true;
        }

        // Vertical
        x = pos & (pos >> 1);
        if ((x & (x >> 2)) != 0) {
            return true;
        }

        // -X Diagonal
        x = pos & (pos >> 6); 
        if ((x & (x >> 12)) != 0) {
            return true;
        }

        x = pos & (pos >> 8);
        if ((x & (x >> 16)) != 0) {
            return true;
        }
        return false;
    }
        
    // Plays all of the moves 
    public void tests(int[] moves) {
        for (int i = 0; i < moves.length; i++) {
            play(moves[i] - 1);
        }
    }

    // Return a bitwise representation of the bottom spot of col
    public static long bottom(int col) {
        return (long) 1 << col *7;
    }

    // Return a bitwise representation of the top spot of col
    public static long top(int col) {
        return (long) 1 << 5 << col * 7;
    }

    // Returns a bitwise representation of all the spots in col filled
    public static long column(int col) {
        return ((long) 1 << 6) - 1 << col * 7;
    }
 
    // Returns a bitwise representation of the board with the bottom row filled
    public static long bottom_row() {
        long ret = 0;
        for (int i = 0; i < 7; i++) {
            ret |= bottom(i);
        }
        return ret;
    }

    public static void main(String args[]) {
        System.out.println(bottom_row());
    }
}
