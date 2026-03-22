package Model;

import java.util.Arrays;

public class Set {
    private CacheLine[] lines;
    private int capacity;

    public Set(int associativity) {
        this.capacity = associativity;
        this.lines = new CacheLine[associativity];
        for (int i = 0; i < associativity; i++) {
            lines[i] = new CacheLine();
        }
    }

    public CacheLine[] getLines() {
        return lines;
    }

    // Returns the line if found (Hit), or null (Miss)
    public CacheLine findLine(int tag) {
        for (CacheLine line : lines) {
            if (line.isValid() && line.getTag() == tag) {
                return line;
            }
        }
        return null;
    }

    // Finds an empty slot index, or -1 if full
    public int findEmptySlot() {
        for (int i = 0; i < capacity; i++) {
            if (!lines[i].isValid()) {
                return i;
            }
        }
        return -1;
    }
}