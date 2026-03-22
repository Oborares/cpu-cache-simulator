package UI;

import Config.*;
import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    // Configuration Inputs
    private JComboBox<MappingType> mappingCombo;
    private JComboBox<ReplacementPolicy> replaceCombo;
    private JComboBox<WritePolicy> writeCombo;

    // NEW: Dynamic Size Inputs
    private JSpinner sizeSpinner;
    private JSpinner waysSpinner;

    private JButton applyConfigBtn;

    // Operation Inputs
    private JTextField addressField;
    private JTextField dataField;
    private JButton readBtn;
    private JButton writeBtn;

    private Main.SimulatorApp app;

    public ControlPanel(Main.SimulatorApp app) {
        this.app = app;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createConfigSection());
        add(Box.createVerticalStrut(15));
        add(new JSeparator());
        add(Box.createVerticalStrut(15));
        add(createOperationsSection());
    }

    private JPanel createConfigSection() {
        // Increased rows to fit new inputs
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Configuration"));

        mappingCombo = new JComboBox<>(MappingType.values());
        replaceCombo = new JComboBox<>(ReplacementPolicy.values());
        writeCombo = new JComboBox<>(WritePolicy.values());

        // Setup Spinners
        // Default size 8, min 1, max 1024, step 1
        sizeSpinner = new JSpinner(new SpinnerNumberModel(8, 1, 1024, 1));
        // Default ways 2, min 1, max 1024, step 1
        waysSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 1024, 1));

        // Disable "Ways" by default unless Set Associative is picked
        waysSpinner.setEnabled(false);

        mappingCombo.addItemListener(e -> {
            MappingType selected = (MappingType) mappingCombo.getSelectedItem();
            boolean isSetAssociative = (selected == MappingType.SET_ASSOCIATIVE);
            waysSpinner.setEnabled(isSetAssociative);
        });

        applyConfigBtn = new JButton("Reset & Apply Configuration");
        applyConfigBtn.setBackground(new Color(220, 50, 50));
        applyConfigBtn.setForeground(Color.WHITE);

        panel.add(new JLabel("Mapping Policy:"));
        panel.add(mappingCombo);

        // New Inputs
        panel.add(new JLabel("Cache Size (Blocks):"));
        panel.add(sizeSpinner);
        panel.add(new JLabel("Ways (Set Assoc only):"));
        panel.add(waysSpinner);

        panel.add(new JLabel("Replacement Policy:"));
        panel.add(replaceCombo);
        panel.add(new JLabel("Write Policy:"));
        panel.add(writeCombo);
        panel.add(new JLabel("")); // Spacer
        panel.add(applyConfigBtn);

        applyConfigBtn.addActionListener(e -> {
            MappingType map = (MappingType) mappingCombo.getSelectedItem();
            ReplacementPolicy rep = (ReplacementPolicy) replaceCombo.getSelectedItem();
            WritePolicy write = (WritePolicy) writeCombo.getSelectedItem();

            // Get values from spinners
            int size = (Integer) sizeSpinner.getValue();
            int ways = (Integer) waysSpinner.getValue();

            // Pass everything to the Main App
            app.rebuildSimulation(map, rep, write, size, ways);
        });

        return panel;
    }

    private JPanel createOperationsSection() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Operations"));

        addressField = new JTextField();
        dataField = new JTextField();
        readBtn = new JButton("READ");
        writeBtn = new JButton("WRITE");

        readBtn.addActionListener(e -> performAccess(false));
        writeBtn.addActionListener(e -> performAccess(true));

        panel.add(new JLabel("Address (Int):"));
        panel.add(addressField);
        panel.add(new JLabel("Data (String):"));
        panel.add(dataField);
        panel.add(readBtn);
        panel.add(writeBtn);

        return panel;
    }

    private void performAccess(boolean isWrite) {
        try {
            String addrText = addressField.getText().trim();
            if(addrText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an address.");
                return;
            }
            int addr = Integer.parseInt(addrText);
            String data = dataField.getText();

            if (isWrite && data.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter data to write.");
                return;
            }

            app.executeOperation(addr, isWrite, data);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Address must be a valid integer.");
        }
    }
}