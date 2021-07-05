public class Board implements Cloneable {
    private long current_position = 0; // Bitwise representation of what spots in the board the current player filled
    private long mask = 0; // Bitwise representation of all the spots in the board that are filled
    private int numMoves = 0; // Number of Moves
    
    private static long bottom_row = 4432676798593L;    // all bottom rows filled
    private static long fullBoard = 279258638311359L; // the entire board filled

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

    // Update the board to include the new move
    public void play(long move) {
        current_position = current_position ^ mask;
        mask |= move;
        numMoves++;
    }

    // Checks to see if col is a winning move 
    public boolean isWinningMove(int col) {
        long pos = current_position;
        pos = pos | (mask + bottom(col)) & column(col);
        return alignment(pos);
    }

    // Return true if the current player can win in one move 
    public boolean canWinNext() {
        return (winning_position() & possible()) != 0;
    }

    // Returns a bitwise represenation of exact positions the current player
    // can make to not be forced into a "Trap"
    public long possibleNonLosingMoves() {
        long possible_mask = possible();
        long opponent_win = opponent_winning_position();
        long forced_moves = possible_mask & opponent_win;
        if (forced_moves != 0) {
            if ((forced_moves & (forced_moves - 1)) != 0) { // check if there are more than one forced move
                return 0;   // the opponent has the opportunity to create a trap 
            } else {
                possible_mask = forced_moves;
            }
        }
        return possible_mask & ~(opponent_win >> 1); // avoid giving the opponent a winning opportunity 
    }


    // Returns a bitwise representation of exact positions the current player
    // can make to garentee a win
    public long winning_position() {
        return compute_winning_position(current_position, mask);
    }

    // Returns a bitwise representation of exact positions where the opponenet
    // can make to garentee a win
    public long opponent_winning_position() {
        return compute_winning_position(current_position ^ mask, mask);
    }

    // Returns a bitwise representation of exact positions the current player
    // can make to garentee a win
    public static long compute_winning_position(long pos, long mas) {
        // vertical
        long possibleAlignments = (pos << 1) & (pos << 2) & (pos << 3);

        // horizontal 
        long i =  (pos << 7) & (pos << 14); // checks one and two spaces to the right
        possibleAlignments |= i & (pos << 21);
        possibleAlignments |= i & (pos >> 7);
        i = (pos >> 7) & (pos >> 14);
        possibleAlignments |= i & (pos << 7);
        possibleAlignments |= i & (pos >> 21);

        // diagonal -x 
        i = (pos << 6) & (pos << 12);
        possibleAlignments |= i & (pos << 18);
        possibleAlignments |= i & (pos >> 6);
        i = (pos >> 6) & (pos >> 12);
        possibleAlignments |= i & (pos << 6);
        possibleAlignments |= i & (pos >> 18);

        // diagonal x 
        i = (pos << 8) & (pos << 16);
        possibleAlignments |= i & (pos << 24);
        possibleAlignments |= i & (pos >> 8);
        i = (pos >> 8) & (pos >> 16);
        possibleAlignments |= i & (pos << 8);
        possibleAlignments |= i & (pos >> 24);

        return possibleAlignments & (fullBoard ^ mas);
    }

    // Return a bitmaks representation of the exact positions of all possible moves
    public long possible() {
        return (mask + bottom_row) & fullBoard;
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

    // number of winning positions after the move is made 
    public int moveScore(long move) {
        return countOnes(compute_winning_position(current_position | move, mask));
    }

    // Return number of bits set to 1 in bits(input)
    public static int countOnes(long bits) {
        int a = 0;
        for (a = 0; bits != 0; a++) {
            bits &= bits - 1;
        }
        return a;
    }
}
