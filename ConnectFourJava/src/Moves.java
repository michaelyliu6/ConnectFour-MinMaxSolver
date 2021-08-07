public class Moves {
    private int size;
    private Entry[] table;

    public Moves() {
        table = new Entry[7];
        for (int i = 0; i < 7; i++) {
            table[i] = new Entry();
            table[i].key = 0;
            table[i].score = 0;
        }
        size = 0;
    }

    // adds a potential move with its score into the table
    // according to insertion sort
    public void add(long move, int score) {
        int i = size;
        while (i > 0 && table[i - 1].score > score) {
            table[i].key = table[i - 1].key;
            table[i].score = table[i - 1].score;
            i--;
        }
        table[i].key = move;
        table[i].score = score;
        size = size + 1;
    }

    // Return the move with the next highest score 
    public long next() {
        if (size == 0) {
            return 0;
        }
        size--;
        return table[size].key;
    }
}
