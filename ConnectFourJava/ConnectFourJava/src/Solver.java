public class Solver {
    private int[] colOrder = new int[]{3, 2, 4, 1, 5, 0, 6};
    private int nodeCount = 0;
    private TranspositionTable table;

    public Solver() {
        this.nodeCount = 0;
        table = new TranspositionTable(8388593);
    }

    public int getNodeCount() {
        return this.nodeCount;
    }

    public int solve(Board board) {
        this.nodeCount = 0; // reset node counter 

        if (board.canWinNext()) { // check if player can win in the next move (not included in minMax)
            return (43 - board.getNumMoves()) / 2;
        }
        int min = -1 * (42 - board.getNumMoves()) / 2; // starter lower bound
        int max = (43 - board.getNumMoves()) / 2; // starter upper bound 

        while (min < max) { // iteratively narrow the min-max exploration window (encourages shallow searches)
            int med = min + (max - min) / 2;
            if (med <= 0 && min / 2 < med) {
                med = min / 2;
            } else if (med >= 0 && max / 2 > med) {
                med = max / 2;
            }
            int score = minMax(board, med, med +  1); // null depth window to get upper/lower bounds
            if (score <= med) {
                max = score;
            } else {
                min = score;
            }
        }
        return min;
    }

    public int minMax(Board board, int alpha, int beta) {
        nodeCount++; // add current node to node counter 

        long possibleMoves = board.possibleNonLosingMoves();
        if (possibleMoves == 0) { // check for potential moves
            return -1 * (42 - board.getNumMoves()) / 2;
        }
        if (board.getNumMoves() >= 40) { // check for a tie 
            return 0;
        }
        int worst = -1 * (40 - board.getNumMoves())/ 2; // lower bound 
        if (alpha < worst) {
            alpha = worst;
            if (alpha >= beta) {
                return alpha;
            }
        }

        int best = (41 - board.getNumMoves()) / 2; // uppper bound 
        long key = board.key();
        int val = table.get(key);

        if (val != 0) {
            if (val > 37) { // lower bound
                worst = val - 56;
                if (alpha < worst) {
                    alpha = worst;
                    if (alpha >= beta) {
                        return alpha;
                    }
                }
            } else { // upper bound
                best = val - 19;
                if (beta > best) {
                    beta = best;
                    if (alpha >= beta) {
                        return beta;
                    }
                }
            }
        }

        Moves moves = new Moves();

        for (int i = 6; i >= 0; i-- ) {
            long move = possibleMoves & Board.column(colOrder[i]);
            if (move != 0) {
                moves.add(move, board.moveScore(move));
            }
        }

        long next = moves.next();
        while (next != 0) {
            Board newBoard = board.cloneBoard();
            newBoard.play(next);
            int score = -1 * (minMax(newBoard, -1 * beta, -1 * alpha));
            if (score >= beta) {
                table.put(key, score + 56);
                return score;
            }
            if (score > alpha) {
                alpha = score;
            }
            next = moves.next();
        }
        table.put(key, alpha + 19); // save an upper bound in the table
        return alpha;
    }
}
