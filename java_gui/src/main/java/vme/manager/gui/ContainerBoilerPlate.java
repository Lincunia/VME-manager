package vme.manager.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import vme.manager.gui.misc.OSFactory;
import vme.manager.gui.misc.OSUtils;

public abstract class ContainerBoilerPlate extends JComponent {
    private final JPanel panelTop,
        panelBottom,
        panelCenter,
        panelLeft,
        panelRight;
    private static final OSUtils osUtils = OSFactory.create();

    public ContainerBoilerPlate()
    {
        setFontGlobally();
        setLayout(new BorderLayout());

        // Crear paneles con espaciado
        panelTop = new JPanel();
        panelTop.setBorder(new EmptyBorder(5, 5, 5, 5));
        add(panelTop, BorderLayout.NORTH);

        panelBottom = new JPanel();
        panelBottom.setBorder(new EmptyBorder(5, 5, 5, 5));
        add(panelBottom, BorderLayout.SOUTH);

        panelCenter = new JPanel(new BorderLayout()); // Cambiar a BorderLayout
        panelCenter.setBorder(new EmptyBorder(5, 5, 5, 5));
        add(panelCenter, BorderLayout.CENTER);

        panelLeft = new JPanel();
        panelLeft.setBorder(new EmptyBorder(5, 5, 5, 5));
        add(panelLeft, BorderLayout.WEST);

        panelRight = new JPanel();
        panelRight.setBorder(new EmptyBorder(5, 5, 5, 5));
        add(panelRight, BorderLayout.EAST);
    }

    private void setFontGlobally()
    {
        Font fuenteGlobal = new Font("sans-serif", Font.PLAIN, 16);

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
        UIManager.put("TitledBorder.font", fuenteGlobal);
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

    public OSUtils getOsUtils()
    {
        return osUtils;
    }
}
