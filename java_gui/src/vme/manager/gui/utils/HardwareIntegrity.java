package vme.manager.gui.utils;

import javax.swing.JTabbedPane;

public class HardwareIntegrity extends ContainerBoilerPlate {
    public HardwareIntegrity(JTabbedPane jTabbedPane)
    {
        super("sans-serif", 20, jTabbedPane);
        getPanelTop().add(setLabel("En desarrollo"));
    }
}
