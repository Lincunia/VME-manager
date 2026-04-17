package vme.manager.gui.utils;

import java.awt.GridLayout;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class HardwareIntegrity extends ContainerUtil {
    private static JTextArea textAreaTest;
    public HardwareIntegrity(JTabbedPane jTabbedPane)
    {
        super(jTabbedPane);
        initComponents();
        checkSystemInformation();
    }

    private void initComponents()
    {
        textAreaTest = new JTextArea();
        textAreaTest.setEditable(false);
        getPanelCenter().setLayout(new GridLayout());
        getPanelCenter().add(new JScrollPane(textAreaTest));
    }

    public void checkSystemInformation()
    {
        try {
            textAreaTest.append(getOsUtils().getMemInf());
			textAreaTest.append("\n=============================\n");
            textAreaTest.append(getOsUtils().getCPUInf());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public void refreshTexts(){
		System.out.println("Lo traduciré LOL");
	}
}
