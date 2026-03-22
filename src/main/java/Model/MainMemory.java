package Model;

public class MainMemory {
    private String[] memory;
    private int size;

    public MainMemory(int size) {
        this.size = size;
        this.memory = new String[size];
        // Initialize with dummy data
        for (int i = 0; i < size; i++) {
            memory[i] = "Mem" + i;
        }
    }

    public String read(int address) {
        if (address >= 0 && address < size) {
            return memory[address];
        }
        return "ERR";
    }

    public void write(int address, String data) {
        if (address >= 0 && address < size) {
            this.memory[address] = data;
        }
    }

    public int getSize() { return size; }
}