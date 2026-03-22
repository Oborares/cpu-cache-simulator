package Control;

import Config.ReplacementPolicy;
import Config.WritePolicy;
import Model.Cache;
import Model.CacheLine;
import Model.Set;
import Stats.Statistics;

public class ControlUnit {
    private Cache cache;
    private ReplacementPolicy replacementPolicy;
    private WritePolicy writePolicy;
    private Statistics stats;


    public ControlUnit(Cache cache, ReplacementPolicy rp, WritePolicy wp,Statistics stats) {
        this.cache = cache;
        this.replacementPolicy = rp;
        this.writePolicy = wp;
        this.stats = stats;
    }

    // The main entry point for the UI
    public String accessMemory(int address, boolean isWrite, String dataToWrite) {

        // 1. Calculate Geometry
        int numSets = cache.getSetCount();
        int setIndex = address % numSets; // The magic formula for all mapping types
        int tag = address / numSets;      // Integer division acts as the Tag

        Set set = cache.getSet(setIndex);
        CacheLine line = set.findLine(tag);
        long currentTime = System.nanoTime();


        if (line != null) {
            stats.recordHit();
            updateReplacementMetadata(line, currentTime);

            if (isWrite) {
                line.setData(dataToWrite);
                line.setDirty(true);

                if (writePolicy == WritePolicy.WRITE_THROUGH) {
                    cache.getMemory().write(address, dataToWrite);
                    line.setDirty(false); // It's clean now
                }
                return "HIT (Write): Updated Cache";
            } else {
                return "HIT (Read): " + line.getData();
            }
        }

        stats.recordMiss();
        String resultMsg = "MISS: ";

        int slotIndex = set.findEmptySlot();
        if (slotIndex == -1) {
            slotIndex = performReplacement(set);
            resultMsg += "Evicted line. ";
        }

        CacheLine targetLine = set.getLines()[slotIndex];

        // 2. Fetch from Memory (simulated)
        String dataFromMem = cache.getMemory().read(address);

        // 3. Load into Cache
        targetLine.setValid(true);
        targetLine.setTag(tag);
        targetLine.setLoadTime(currentTime);
        targetLine.setLastAccessTime(currentTime);
        targetLine.setAccessCount(1);

        if (isWrite) {
            // Write-Allocate: Load block, then modify it
            targetLine.setData(dataToWrite);
            targetLine.setDirty(true);
            if (writePolicy == WritePolicy.WRITE_THROUGH) {
                cache.getMemory().write(address, dataToWrite);
                targetLine.setDirty(false);
            }
            resultMsg += "Loaded & Written";
        } else {
            targetLine.setData(dataFromMem);
            targetLine.setDirty(false); // Fresh from memory
            resultMsg += "Loaded from Mem";
        }

        return resultMsg;
    }

    // Helper: Selects victim based on policy and handles Write-Back if needed
    private int performReplacement(Set set) {
        int victimIndex = 0;
        CacheLine[] lines = set.getLines();
        CacheLine victim = lines[0];

        for (int i = 1; i < lines.length; i++) {
            boolean changeVictim = false;
            CacheLine current = lines[i];

            switch (replacementPolicy) {
                case LRU:
                    if (current.getLastAccessTime() < victim.getLastAccessTime()) changeVictim = true;
                    break;
                case FIFO:
                    if (current.getLoadTime() < victim.getLoadTime()) changeVictim = true;
                    break;
                case LFU:
                    if (current.getAccessCount() < victim.getAccessCount()) changeVictim = true;
                    break;
                case RANDOM:
                    break;
            }

            if (changeVictim) {
                victim = current;
                victimIndex = i;
            }
        }

        if (replacementPolicy == ReplacementPolicy.RANDOM) {
            victimIndex = (int) (Math.random() * lines.length);
            victim = lines[victimIndex];
        }

        if (writePolicy == WritePolicy.WRITE_BACK && victim.isDirty()) {
            System.out.println("Write-Back triggered for Tag: " + victim.getTag());
            int setIndex = getSetIndexForLine(set);
            int address = (victim.getTag() * cache.getSetCount()) + setIndex;
            cache.getMemory().write(address, victim.getData());
        }

        return victimIndex;
    }

    private void updateReplacementMetadata(CacheLine line, long time) {
        line.setLastAccessTime(time);
        line.incrementAccessCount();
    }

    private int getSetIndexForLine(Set set) {
        Set[] allSets = cache.getAllSets();
        for(int i=0; i<allSets.length; i++) {
            if(allSets[i] == set) return i;
        }
        return 0;
    }
}