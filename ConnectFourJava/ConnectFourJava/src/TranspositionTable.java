// Hash table that stores an upper bound score for previously calculated boards
public class TranspositionTable {
    private Entry[] table;

    // initally the hash table is created of of size length and empty entries 
    public TranspositionTable(int size) {
        table = new Entry[size];
        for (int i = 0; i < size; i++) {
            table[i] = new Entry();
            table[i].key = 0;
            table[i].score = 0;
        }
    }

    // adds an upper bound score for a specific board into the hash table
    public void put(long key, int score) {
        int i = index(key);
        table[i].key = key;
        table[i].score = score;
    }

    // returns the previously calculated score for key 
    public int get(long key) {
        int i = index(key);
        if (table[i].key == key) {
            return table[i].score;
        } else {
            return 0;
        }
    }

    // returns the index that key would appear in the hash table
    public int index(long key) {
        return Math.toIntExact(key%(table.length));
    }

    public static void main(String args[]) {
        int a = 1;
        System.out.println(-a);
    }
}
