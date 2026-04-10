package vme.manager.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import vme.manager.gui.misc.OSFactory;
import vme.manager.gui.misc.OSUtils;

public abstract class ContainerBoilerPlate extends JComponent {
    private final String fontStyle;
    private final int fontSize;
    private final JPanel panelTop,
            panelBottom,
            panelCenter,
            panelLeft,
            panelRight;
	private static OSUtils osUtils;
    public ContainerBoilerPlate() {
		/*
		 * Configuración estática
		 */
        this.fontStyle = "sans-serif";
        this.fontSize = 16;
		osUtils = OSFactory.create();

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

    public JButton setButton(String str) {
        JButton btn = new JButton(str);
        btn.setFont(new Font(fontStyle, Font.PLAIN, fontSize));
        return btn;
    }

    public JLabel setLabel(String str) {
        JLabel label = new JLabel(str);
        label.setFont(new Font(fontStyle, Font.PLAIN, fontSize));
        return label;
    }

    public JTextField setTextField(int columns) {
        JTextField textField = new JTextField();
        textField.setFont(new Font(fontStyle, Font.PLAIN, fontSize));
        textField.setColumns(columns);
        return textField;
    }

	public JCheckBox setCheckBox(String str){
		JCheckBox checkBox = new JCheckBox(str);
		checkBox.setFont(new Font(fontStyle, Font.PLAIN, fontSize));
		return checkBox;
	}

    public JPanel getPanelTop() {
        return panelTop;
    }

    public JPanel getPanelLeft() {
        return panelLeft;
    }

    public JPanel getPanelRight() {
        return panelRight;
    }

    public JPanel getPanelBottom() {
        return panelBottom;
    }

    public JPanel getPanelCenter() {
        return panelCenter;
    }

	public OSUtils getOsUtils() {
		return osUtils;
	}
}
