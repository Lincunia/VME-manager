package vme.manager.gui.utils;

import java.awt.Dimension;
import javax.swing.JTabbedPane;
import vme.manager.gui.ContainerBoilerPlate;

public class ContainerUtil extends ContainerBoilerPlate {
    private final JTabbedPane tabbedPaneParent;
    public ContainerUtil(JTabbedPane tabbedPane)
    {
        super();
        tabbedPaneParent = tabbedPane;
        setPreferredSize(new Dimension(400, 300));
    }

    public JTabbedPane getTabbedPaneParent()
    {
        return tabbedPaneParent;
    }
}
