package UI;

import javax.swing.*;
import java.awt.*;

public class CachePanel extends JPanel {
    private JTable table;
    private CacheTableModel tableModel;

    public CachePanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Cache State (Visual Debugger)"));

        table = new JTable();
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setModel(CacheTableModel model) {
        this.tableModel = model;
        table.setModel(model);

        // Optional: Adjust column widths for better "at a glance" visibility
        table.getColumnModel().getColumn(0).setPreferredWidth(40); // Set
        table.getColumnModel().getColumn(1).setPreferredWidth(40); // Line
        table.getColumnModel().getColumn(5).setPreferredWidth(100); // Data
    }

    public void refresh() {
        if(tableModel != null) {
            tableModel.fireDataChanged();
        }
        table.repaint();
    }
}