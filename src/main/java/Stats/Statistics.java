package Stats;

public class Statistics {
    private int hits;
    private int misses;
    private int totalAccesses;

    public Statistics() {
        reset();
    }

    public void recordHit() {
        hits++;
        totalAccesses++;
    }

    public void recordMiss() {
        misses++;
        totalAccesses++;
    }

    public void reset() {
        hits = 0;
        misses = 0;
        totalAccesses = 0;
    }

    // --- Calculated Metrics ---

    public double getHitRate() {
        if (totalAccesses == 0) return 0.0;
        return ((double) hits / totalAccesses) * 100.0;
    }

    public double getMissRate() {
        if (totalAccesses == 0) return 0.0;
        return ((double) misses / totalAccesses) * 100.0;
    }

    // --- Getters for Raw Numbers ---
    public int getHits() { return hits; }
    public int getMisses() { return misses; }
    public int getTotalAccesses() { return totalAccesses; }
}