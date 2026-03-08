package vme.manager.gui.utils;

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OptimizationMethodology extends JPanel {
    public OptimizationMethodology()
    {
        add(setLabel("En desarrollo", 18));
    }

    private JLabel setLabel(String str, int size)
    {
        JLabel label = new JLabel(str);
        label.setFont(new Font("sans-serif", Font.PLAIN, size));
        return label;
    }
}
