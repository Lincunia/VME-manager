package vme.manager.gui.utils;

import javax.swing.JTabbedPane;

public class PredictionOfDevice extends ContainerBoilerPlate {

    public PredictionOfDevice(JTabbedPane jTabbedPane){
        super("sans-serif", 20, jTabbedPane);
        getPanelTop().add(setLabel("En desarrollo"));
    }
}
