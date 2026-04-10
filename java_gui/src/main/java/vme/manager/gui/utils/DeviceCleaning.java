package vme.manager.gui.utils;

import javax.swing.JTabbedPane;

public class DeviceCleaning extends ContainerUtil {
    public DeviceCleaning(JTabbedPane jTabbedPane)
    {
        super(jTabbedPane);
        getPanelTop().add(setLabel("En desarrollo"));
    }
}
