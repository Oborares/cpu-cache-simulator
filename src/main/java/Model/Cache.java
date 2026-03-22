package Model;

import Config.MappingType;

public class Cache {
    private Set[] sets;
    private int cacheSize;      // Total blocks
    private int associativity;  // Blocks per set

    // We need a reference to main memory to interact with it
    private MainMemory mainMemory;

    public Cache(int cacheSize, MappingType type, int ways, MainMemory mem) {
        this.cacheSize = cacheSize;
        this.mainMemory = mem;

        // Calculate number of sets based on mapping type
        int numSets = 1;

        switch (type) {
            case DIRECT_MAPPED:
                this.associativity = 1;
                numSets = cacheSize;
                break;
            case FULLY_ASSOCIATIVE:
                this.associativity = cacheSize;
                numSets = 1;
                break;
            case SET_ASSOCIATIVE:
                this.associativity = ways;
                numSets = cacheSize / ways;
                break;
        }

        sets = new Set[numSets];
        for (int i = 0; i < numSets; i++) {
            sets[i] = new Set(this.associativity);
        }
    }

    public Set getSet(int index) {
        if (index >= 0 && index < sets.length) {
            return sets[index];
        }
        return null;
    }

    public Set[] getAllSets() { return sets; }
    public int getSetCount() { return sets.length; }
    public MainMemory getMemory() { return mainMemory; }
}