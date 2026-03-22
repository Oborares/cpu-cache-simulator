package UI;

import Stats.Statistics;
import javax.swing.*;
import java.awt.*;

public class StatisticsPanel extends JPanel {
    private JLabel hitsLabel;
    private JLabel missesLabel;
    private JLabel hitRateLabel;
    private JLabel totalLabel;

    private Statistics stats;

    public StatisticsPanel() {
        setLayout(new GridLayout(1, 4, 10, 0));
        setBorder(BorderFactory.createTitledBorder("Live Statistics"));
        setBackground(new Color(245, 245, 250));

        hitsLabel = createStatLabel("Hits: 0");
        missesLabel = createStatLabel("Misses: 0");
        hitRateLabel = createStatLabel("Hit Rate: 0.0%");
        totalLabel = createStatLabel("Total: 0");

        add(totalLabel);
        add(hitsLabel);
        add(missesLabel);
        add(hitRateLabel);
    }

    private JLabel createStatLabel(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        return lbl;
    }

    public void setStats(Statistics stats) {
        this.stats = stats;
        refresh();
    }

    public void refresh() {
        if (stats == null) return;

        hitsLabel.setText("Hits: " + stats.getHits());
        hitsLabel.setForeground(new Color(0, 100, 0)); // Dark Green

        missesLabel.setText("Misses: " + stats.getMisses());
        missesLabel.setForeground(Color.RED);

        totalLabel.setText("Total: " + stats.getTotalAccesses());

        hitRateLabel.setText(String.format("Hit Rate: %.1f%%", stats.getHitRate()));
    }
}