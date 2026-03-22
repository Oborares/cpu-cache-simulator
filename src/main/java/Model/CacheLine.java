package Model;

public class CacheLine {
    private boolean valid;
    private boolean dirty; // For Write-Back
    private int tag;
    private String data;

    // Stats for replacement
    private long lastAccessTime; // For LRU
    private long loadTime;       // For FIFO
    private int accessCount;     // For LFU

    public CacheLine() {
        this.valid = false;
        this.dirty = false;
        this.tag = -1;
        this.data = "00";
        this.accessCount = 0;
    }

    // --- Getters ---
    public boolean isValid() { return valid; }
    public boolean isDirty() { return dirty; }
    public int getTag() { return tag; }
    public String getData() { return data; }
    public long getLastAccessTime() { return lastAccessTime; }
    public long getLoadTime() { return loadTime; }
    public int getAccessCount() { return accessCount; }

    // --- Setters ---
    public void setValid(boolean valid) { this.valid = valid; }
    public void setDirty(boolean dirty) { this.dirty = dirty; }
    public void setTag(int tag) { this.tag = tag; }
    public void setData(String data) { this.data = data; }
    public void setLastAccessTime(long time) { this.lastAccessTime = time; }
    public void setLoadTime(long time) { this.loadTime = time; }

    // This was the missing method!
    public void incrementAccessCount() {
        this.accessCount++;
    }

    public void setAccessCount(int count) {
        this.accessCount = count;
    }
}