package UI;

import Model.Cache;
import Model.CacheLine;
import Model.Set;
import javax.swing.table.AbstractTableModel;

public class CacheTableModel extends AbstractTableModel {
    private Cache cache;
    // Define column names
    private final String[] columnNames = { "Set", "Line", "Valid", "Dirty", "Tag", "Data", "Last Access", "Ref Count" };

    public CacheTableModel(Cache cache) {
        this.cache = cache;
    }

    @Override
    public int getRowCount() {
        // Total rows = (Number of Sets) * (Lines per Set)
        if (cache == null) return 0;
        return cache.getSetCount() * cache.getAllSets()[0].getLines().length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (cache == null) return null;

        // 1. Calculate which Set and which Line this row belongs to
        int associativity = cache.getAllSets()[0].getLines().length;
        int setIndex = rowIndex / associativity;
        int lineIndex = rowIndex % associativity;

        Set set = cache.getSet(setIndex);
        CacheLine line = set.getLines()[lineIndex];

        // 2. Return the specific data for that column
        switch (columnIndex) {
            case 0: return setIndex;           // Set Number
            case 1: return lineIndex;          // Line Number (within the set)
            case 2: return line.isValid();     // Valid Bit
            case 3: return line.isDirty();     // Dirty Bit (Crucial for Write-Back)
            case 4: return line.getTag();      // Tag
            case 5: return line.getData();     // Data
            case 6: return line.getLastAccessTime(); // For LRU visualization
            case 7: return line.getAccessCount();    // For LFU visualization
            default: return null;
        }
    }

    // Call this whenever the simulation changes the cache state
    public void fireDataChanged() {
        fireTableDataChanged();
    }
}