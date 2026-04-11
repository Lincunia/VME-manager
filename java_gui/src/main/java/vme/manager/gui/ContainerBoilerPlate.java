package vme.manager.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;

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
	private static final OSUtils osUtils = OSFactory.create();
    public ContainerBoilerPlate() {
		/*
		 * Configuración estática
		 */
        this.fontStyle = "IosevkaTermSlab Nerd Font Mono";
        this.fontSize = 16;
		setStyle();

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

	private void setStyle(){
		Font fuenteGlobal = new Font(fontStyle, Font.PLAIN, fontSize);

        UIManager.put("Button.font", fuenteGlobal);
        UIManager.put("Label.font", fuenteGlobal);
        UIManager.put("TextField.font", fuenteGlobal);
        UIManager.put("TextArea.font", fuenteGlobal);
        UIManager.put("CheckBox.font", fuenteGlobal);
        UIManager.put("RadioButton.font", fuenteGlobal);
        UIManager.put("ComboBox.font", fuenteGlobal);
        UIManager.put("List.font", fuenteGlobal);
        UIManager.put("Table.font", fuenteGlobal);
        UIManager.put("Tree.font", fuenteGlobal);
        UIManager.put("TabbedPane.font", fuenteGlobal);
        UIManager.put("Menu.font", fuenteGlobal);
        UIManager.put("MenuItem.font", fuenteGlobal);
		UIManager.put("defaultFont", fuenteGlobal);
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
