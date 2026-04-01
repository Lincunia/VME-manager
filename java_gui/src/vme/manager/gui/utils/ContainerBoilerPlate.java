package vme.manager.gui.utils;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public abstract class ContainerBoilerPlate extends JPanel {
    private final String fontStyle;
    private final int fontSize;
    private JPanel
        panelTop,
        panelBottom,
        panelCenter,
        panelLeft,
        panelRight;
    private static JTabbedPane tabbedPaneParent;

    public ContainerBoilerPlate(String fontStyle, int fontSize, JTabbedPane jTabbedPane)
    {
        this.fontStyle = fontStyle;
        this.fontSize = fontSize;
        this.tabbedPaneParent = jTabbedPane;

		/*
        JScrollPane scrollPane = new JScrollPane(this);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		*/
        setLayout(new BorderLayout());

        panelTop = new JPanel();
        add(panelTop, BorderLayout.NORTH);
        panelBottom = new JPanel();
        add(panelBottom, BorderLayout.SOUTH);
        panelCenter = new JPanel();
        add(panelCenter, BorderLayout.CENTER);
        panelLeft = new JPanel();
        add(panelLeft, BorderLayout.WEST);
        panelRight = new JPanel();
        add(panelRight, BorderLayout.EAST);
    }
    public JButton setButton(String str)
    {
        JButton btn = new JButton(str);
        btn.setFont(new Font(fontStyle, Font.PLAIN, fontSize));
        return btn;
    }
    public JLabel setLabel(String str)
    {
        JLabel label = new JLabel(str);
        label.setFont(new Font(fontStyle, Font.PLAIN, fontSize));
        return label;
    }
    public JTextField setTextField(int columns)
    {
        JTextField textField = new JTextField();
        textField.setFont(new Font(fontStyle, Font.PLAIN, fontSize));
		textField.setColumns(columns);
        return textField;
    }
    public JPanel getPanelTop()
    {
        return panelTop;
    }
    public JPanel getPanelLeft()
    {
        return panelLeft;
    }
    public JPanel getPanelRight()
    {
        return panelRight;
    }
    public JPanel getPanelBottom()
    {
        return panelBottom;
    }
    public JPanel getPanelCenter()
    {
        return panelCenter;
    }
    public static JTabbedPane getTabbedPaneParent()
    {
        return tabbedPaneParent;
    }
}
