package vme.manager.gui.utils;

import javax.swing.JTabbedPane;

public class DeviceCleaning extends ContainerBoilerPlate {
    public DeviceCleaning(JTabbedPane jTabbedPane)
    {
        super("sans-serif", 20, jTabbedPane);
        getPanelTop().add(setLabel("En desarrollo"));
    }

}
