package vme.manager.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
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
    private static JFrame parentFrame;

    // Definir colores para ambos temas
    private static final Color LIGHT_BG = new Color(236, 236, 236);
    private static final Color LIGHT_TEXT = Color.BLACK;
    private static final Color LIGHT_PANEL_BG = new Color(240, 240, 240);
    private static final Color LIGHT_BUTTON_BG = new Color(214, 214, 214);
    private static final Color LIGHT_TEXTAREA_BG = Color.WHITE;
    private static final Color LIGHT_BORDER = Color.GRAY;

    private static final Color DARK_BG = new Color(60, 63, 65);
    private static final Color DARK_TEXT = Color.WHITE;
    private static final Color DARK_PANEL_BG = new Color(43, 43, 43);
    private static final Color DARK_BUTTON_BG = new Color(75, 75, 75);
    private static final Color DARK_TEXTAREA_BG = new Color(43, 43, 43);
    private static final Color DARK_BORDER = new Color(105, 105, 105);

    private static String currentTheme = "Claro";

    public SettingsDialog(JFrame parent, boolean modal)
    {
        super(parent, modal);
        parentFrame = parent;
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Configuración de VME-manager");

        osUtils = OSFactory.create();

        initComponents();

        pack();
    }

    private void initComponents()
    {
        innerContainer = new InnerSettingsDialog();
        innerContainer.getPanelTop().setLayout(new FlowLayout());
        innerContainer.getPanelCenter().setLayout(new GridLayout(4, 1));
        innerContainer.getPanelBottom().setLayout(new FlowLayout());

        add(innerContainer);

        buttonSave = new JButton("Guardar");
        buttonSave.addActionListener((ActionEvent e) -> {
            save();
            this.dispose();
        });
        innerContainer.getPanelBottom().add(buttonSave);

        checkBoxStartUp = new JCheckBox("Iniciar al encender el equipo");
        innerContainer.getPanelCenter().add(checkBoxStartUp);

        comboBoxLanguage = new JComboBox<>();
        comboBoxLanguage.setModel(new DefaultComboBoxModel<>(new String[] { "Español", "Inglés" }));
        comboBoxLanguage.setSelectedIndex(0);
        innerContainer.getPanelCenter().add(comboBoxLanguage);

        comboBoxTheme = new JComboBox<>();
        comboBoxTheme.setModel(new DefaultComboBoxModel<>(new String[] { "Claro", "Oscuro" }));
        comboBoxTheme.setSelectedIndex(0);
        innerContainer.getPanelCenter().add(comboBoxTheme);
    }

    public void load()
    {
        try {
            currentTheme = "Claro";

            switch (currentTheme) {
            case "Claro":
                comboBoxTheme.setSelectedIndex(0);
                break;
            case "Oscuro":
                comboBoxTheme.setSelectedIndex(1);
                break;
            }

            applyTheme(comboBoxTheme.getSelectedItem().toString());

            //osUtils.setEnableStartup(checkBoxStartUp.isSelected());
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
        try {
            currentTheme = comboBoxTheme.getSelectedItem().toString();
            applyTheme(currentTheme);
            osUtils.setEnableStartup(checkBoxStartUp.isSelected());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void applyTheme(String theme)
    {
        try {
            if ("Oscuro".equals(theme)) {
                applyDarkTheme();
            } else {
                applyLightTheme();
            }

            // Actualizar todos los componentes de todas las ventanas
            updateAllComponents();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyDarkTheme()
    {
        // Configurar UIManager para nuevos componentes
        UIManager.put("control", new ColorUIResource(DARK_BG));
        UIManager.put("text", new ColorUIResource(DARK_TEXT));
        UIManager.put("TextArea.background", new ColorUIResource(DARK_TEXTAREA_BG));
        UIManager.put("TextArea.foreground", new ColorUIResource(DARK_TEXT));
        UIManager.put("List.background", new ColorUIResource(DARK_TEXTAREA_BG));
        UIManager.put("List.foreground", new ColorUIResource(DARK_TEXT));
        UIManager.put("Panel.background", new ColorUIResource(DARK_PANEL_BG));
        UIManager.put("Panel.foreground", new ColorUIResource(DARK_TEXT));
        UIManager.put("Button.background", new ColorUIResource(DARK_BUTTON_BG));
        UIManager.put("Button.foreground", new ColorUIResource(DARK_TEXT));
        UIManager.put("Label.foreground", new ColorUIResource(DARK_TEXT));
        UIManager.put("Label.background", new ColorUIResource(DARK_PANEL_BG));
        UIManager.put("CheckBox.background", new ColorUIResource(DARK_PANEL_BG));
        UIManager.put("CheckBox.foreground", new ColorUIResource(DARK_TEXT));
        UIManager.put("ComboBox.background", new ColorUIResource(DARK_BUTTON_BG));
        UIManager.put("ComboBox.foreground", new ColorUIResource(DARK_TEXT));
        UIManager.put("TabbedPane.background", new ColorUIResource(DARK_PANEL_BG));
        UIManager.put("TabbedPane.foreground", new ColorUIResource(DARK_TEXT));
        UIManager.put("TabbedPane.selected", new ColorUIResource(DARK_BG));
        UIManager.put("ScrollPane.background", new ColorUIResource(DARK_PANEL_BG));
        UIManager.put("Viewport.background", new ColorUIResource(DARK_PANEL_BG));

        // Configurar bordes
        Border lineBorder = BorderFactory.createLineBorder(DARK_BORDER);
        UIManager.put("TitledBorder.border", lineBorder);
        UIManager.put("TitledBorder.titleColor", new ColorUIResource(DARK_TEXT));

    }

    private void applyLightTheme()
    {
        // Configurar UIManager para nuevos componentes
        UIManager.put("control", new ColorUIResource(LIGHT_BG));
        UIManager.put("text", new ColorUIResource(LIGHT_TEXT));
        UIManager.put("TextArea.background", new ColorUIResource(LIGHT_TEXTAREA_BG));
        UIManager.put("TextArea.foreground", new ColorUIResource(LIGHT_TEXT));
        UIManager.put("List.background", new ColorUIResource(DARK_TEXT));
        UIManager.put("List.foreground", new ColorUIResource(DARK_TEXTAREA_BG));
        UIManager.put("Panel.background", new ColorUIResource(LIGHT_PANEL_BG));
        UIManager.put("Panel.foreground", new ColorUIResource(LIGHT_TEXT));
        UIManager.put("Button.background", new ColorUIResource(LIGHT_BUTTON_BG));
        UIManager.put("Button.foreground", new ColorUIResource(LIGHT_TEXT));
        UIManager.put("Label.foreground", new ColorUIResource(LIGHT_TEXT));
        UIManager.put("Label.background", new ColorUIResource(LIGHT_PANEL_BG));
        UIManager.put("CheckBox.background", new ColorUIResource(LIGHT_PANEL_BG));
        UIManager.put("CheckBox.foreground", new ColorUIResource(LIGHT_TEXT));
        UIManager.put("ComboBox.background", new ColorUIResource(LIGHT_BUTTON_BG));
        UIManager.put("ComboBox.foreground", new ColorUIResource(LIGHT_TEXT));
        UIManager.put("TabbedPane.background", new ColorUIResource(LIGHT_PANEL_BG));
        UIManager.put("TabbedPane.foreground", new ColorUIResource(LIGHT_TEXT));
        UIManager.put("TabbedPane.selected", new ColorUIResource(LIGHT_BG));
        UIManager.put("ScrollPane.background", new ColorUIResource(LIGHT_PANEL_BG));
        UIManager.put("Viewport.background", new ColorUIResource(LIGHT_PANEL_BG));

        // Configurar bordes
        Border lineBorder = BorderFactory.createLineBorder(LIGHT_BORDER);
        UIManager.put("TitledBorder.border", lineBorder);
        UIManager.put("TitledBorder.titleColor", new ColorUIResource(LIGHT_TEXT));
    }

    private void updateAllComponents()
    {
        // Actualizar la ventana principal
        if (parentFrame != null) {
            updateComponentTree(parentFrame);
            SwingUtilities.updateComponentTreeUI(parentFrame);
            parentFrame.repaint();
            parentFrame.revalidate();
        }

        // Actualizar todas las ventanas abiertas
        for (Window window : Window.getWindows()) {
            if (window != parentFrame) {
                updateComponentTree(window);
                SwingUtilities.updateComponentTreeUI(window);
                window.repaint();
                window.revalidate();
            }
        }

        // Actualizar este diálogo también
        updateComponentTree(this);
        SwingUtilities.updateComponentTreeUI(this);
        this.repaint();
        this.revalidate();
    }

    private void updateComponentTree(Component component)
    {
        if (component == null)
            return;

        // Actualizar colores del componente actual
        if (component instanceof JPanel) {
            JPanel panel = (JPanel)component;
            if ("Oscuro".equals(currentTheme)) {
                panel.setBackground(DARK_PANEL_BG);
                panel.setForeground(DARK_TEXT);
            } else {
                panel.setBackground(LIGHT_PANEL_BG);
                panel.setForeground(LIGHT_TEXT);
            }
        } else if (component instanceof JLabel) {
            JLabel label = (JLabel)component;
            if ("Oscuro".equals(currentTheme)) {
                label.setForeground(DARK_TEXT);
                if (label.getBackground() != null) {
                    label.setBackground(DARK_PANEL_BG);
                }
            } else {
                label.setForeground(LIGHT_TEXT);
                if (label.getBackground() != null) {
                    label.setBackground(LIGHT_PANEL_BG);
                }
            }
        } else if (component instanceof JButton) {
            JButton button = (JButton)component;
            if ("Oscuro".equals(currentTheme)) {
                button.setBackground(DARK_BUTTON_BG);
                button.setForeground(DARK_TEXT);
            } else {
                button.setBackground(LIGHT_BUTTON_BG);
                button.setForeground(LIGHT_TEXT);
            }
        } else if (component instanceof JCheckBox) {
            JCheckBox checkBox = (JCheckBox)component;
            if ("Oscuro".equals(currentTheme)) {
                checkBox.setBackground(DARK_PANEL_BG);
                checkBox.setForeground(DARK_TEXT);
            } else {
                checkBox.setBackground(LIGHT_PANEL_BG);
                checkBox.setForeground(LIGHT_TEXT);
            }
        } else if (component instanceof JComboBox) {
            JComboBox<?> comboBox = (JComboBox<?>)component;
            if ("Oscuro".equals(currentTheme)) {
                comboBox.setBackground(DARK_BUTTON_BG);
                comboBox.setForeground(DARK_TEXT);
            } else {
                comboBox.setBackground(LIGHT_BUTTON_BG);
                comboBox.setForeground(LIGHT_TEXT);
            }
        } else if (component instanceof JTabbedPane) {
            JTabbedPane tabbedPane = (JTabbedPane)component;
            if ("Oscuro".equals(currentTheme)) {
                tabbedPane.setBackground(DARK_PANEL_BG);
                tabbedPane.setForeground(DARK_TEXT);
            } else {
                tabbedPane.setBackground(LIGHT_PANEL_BG);
                tabbedPane.setForeground(LIGHT_TEXT);
            }
        } else if (component instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane)component;
            if ("Oscuro".equals(currentTheme)) {
                scrollPane.getViewport().setBackground(DARK_PANEL_BG);
                scrollPane.setBackground(DARK_PANEL_BG);
            } else {
                scrollPane.getViewport().setBackground(LIGHT_PANEL_BG);
                scrollPane.setBackground(LIGHT_PANEL_BG);
            }
        }

        // Actualizar recursivamente todos los componentes hijos
        if (component instanceof Container) {
            Container container = (Container)component;
            for (Component child : container.getComponents()) {
                updateComponentTree(child);
            }
        }
    }
}
