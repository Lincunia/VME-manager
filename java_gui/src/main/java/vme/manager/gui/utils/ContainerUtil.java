package vme.manager.gui.utils;

import javax.swing.JTabbedPane;
import vme.manager.gui.ContainerBoilerPlate;

public class ContainerUtil extends ContainerBoilerPlate {
    private final JTabbedPane tabbedPaneParent;
    public ContainerUtil(JTabbedPane tabbedPane)
    {
        super();
        tabbedPaneParent = tabbedPane;
    }

    public JTabbedPane getTabbedPaneParent()
    {
        return tabbedPaneParent;
    }
}
