package Main;

import Config.*;
import Control.ControlUnit;
import Model.*;
import UI.*;
import Stats.Statistics;

import javax.swing.*;
import java.awt.*;

public class SimulatorApp extends JFrame {
    private ControlPanel controlPanel;
    private CachePanel cachePanel;
    private StatisticsPanel statsPanel;
    private JTextArea logArea;

    private Cache cache;
    private MainMemory memory;
    private ControlUnit controlUnit;
    private CacheTableModel tableModel;
    private Statistics statistics;

    public SimulatorApp() {
        setTitle("Modular Cache Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(1000, 700);


        controlPanel = new ControlPanel(this);
        cachePanel = new CachePanel();
        statsPanel = new StatisticsPanel(); // <--- Initialize Panel

        logArea = new JTextArea(8, 50);
        logArea.setEditable(false);
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Simulation Log"));


        add(controlPanel, BorderLayout.WEST);


        JPanel centerContainer = new JPanel(new BorderLayout(5, 5));
        centerContainer.add(statsPanel, BorderLayout.NORTH); // Stats at top
        centerContainer.add(cachePanel, BorderLayout.CENTER); // Table below

        add(centerContainer, BorderLayout.CENTER);
        add(logScroll, BorderLayout.SOUTH);

        rebuildSimulation(MappingType.DIRECT_MAPPED, ReplacementPolicy.LRU, WritePolicy.WRITE_THROUGH, 8, 2);  
        setVisible(true);
    }


    public void rebuildSimulation(MappingType mapping, ReplacementPolicy replace, WritePolicy write, int cacheSize, int ways) {

        memory = new MainMemory(128);

        cache = new Cache(cacheSize, mapping, ways, memory);

        // Reset Stats
        statistics = new Statistics();

        // Initialize Control Unit
        controlUnit = new ControlUnit(cache, replace, write, statistics);

        // Reconnect UI
        tableModel = new CacheTableModel(cache);
        cachePanel.setModel(tableModel);
        statsPanel.setStats(statistics);

        log("SYSTEM RESET: " + mapping + " | Size: " + cacheSize + " | Ways: " + (mapping == MappingType.SET_ASSOCIATIVE ? ways : "N/A"));
    }

    public void executeOperation(int address, boolean isWrite, String data) {
        if (controlUnit == null) return;

        String result = controlUnit.accessMemory(address, isWrite, data);

        log((isWrite ? "WRITE " : "READ  ") + "[" + address + "]: " + result);

        // Refresh visuals
        cachePanel.refresh();
        statsPanel.refresh();
    }

    private void log(String msg) {
        logArea.append(msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new SimulatorApp());
    }
}
