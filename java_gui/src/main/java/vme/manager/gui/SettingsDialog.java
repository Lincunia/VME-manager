package vme.manager.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import vme.manager.gui.misc.OSFactory;
import vme.manager.gui.misc.OSUtils;

public class SettingsDialog extends JDialog {
    private class InnerSettingsDialog extends ContainerBoilerPlate {
        public InnerSettingsDialog()
        {
        }
    }
    private JButton buttonSave;
    private JCheckBox checkBoxStartUp;
    private JComboBox<String> comboBoxLanguage, comboBoxTheme;
    private static OSUtils osUtils;
    private ContainerBoilerPlate innerContainer;

    public SettingsDialog(JFrame parent, boolean modal)
    {
        super(parent, modal);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
		setTitle("Configuración de VME-manager");

        osUtils = OSFactory.create();

        initComponents();
        load();

        pack();
    }

    private void initComponents()
    {
        innerContainer = new InnerSettingsDialog();
		innerContainer.getPanelTop().setLayout(new FlowLayout());
		innerContainer.getPanelCenter().setLayout(new GridLayout(4,1));
		innerContainer.getPanelBottom().setLayout(new FlowLayout());

        innerContainer.getPanelTop().add(
            innerContainer.setLabel("Configuración"));
        add(innerContainer);

        buttonSave = innerContainer.setButton("Guardar");
        buttonSave.addActionListener((ActionEvent e) -> {
            save();
            this.dispose();
        });
        innerContainer.getPanelBottom().add(buttonSave);

        checkBoxStartUp = innerContainer.setCheckBox("Iniciar al encender el equipo");
        innerContainer.getPanelCenter().add(checkBoxStartUp);

        comboBoxLanguage = new JComboBox<>();
        comboBoxLanguage.setModel(new DefaultComboBoxModel<>(new String[] { "Español", "Inglés" }));
        comboBoxLanguage.setSelectedIndex(0);
        innerContainer.getPanelCenter().add(comboBoxLanguage);

        comboBoxTheme = new JComboBox<>();
        comboBoxTheme.setModel(new DefaultComboBoxModel<>(new String[] { "Claro", "Oscuro", "Por defecto" }));
        comboBoxTheme.setSelectedIndex(0);
        innerContainer.getPanelCenter().add(comboBoxTheme);
    }

    public void load()
    {
        try {
            osUtils.setEnableStartup(checkBoxStartUp.isSelected());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null,
                e.getMessage(),
                "Ha ocurrido un error",
                JOptionPane.WARNING_MESSAGE);
        }
    }
    public void save()
    {

        for (Window w : Window.getWindows()) {
            if (w instanceof Frame) {
                // Please Implement the Language feature;
                ((Frame)w).setTheme(comboBoxTheme.getSelectedItem().toString());
            }
        }
    }
}
