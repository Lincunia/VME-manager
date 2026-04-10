package vme.manager.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import vme.manager.gui.utils.*;

public class Frame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static JPanel titlePanel, bottomPanel;
    private static final JTabbedPane tabbedPane = new JTabbedPane();
    private JScrollPane
        scrollPaneFileAnalysis,
        scrollPaneNetworkState,
        scrollPaneHardwareIntegrity,
        scrollPaneDeviceCleaning,
        scrollPanePredictionOfDevice,
        scrollPaneOptimizationMethodology;
    private static DeviceCleaning deviceCleaning;
    private static FileAnalysis fileAnalysis;
    private static HardwareIntegrity hardwareIntegrity;
    private static NetworkState networkState;
    private static OptimizationMethodology optimizationMethodology;
    private static PredictionOfDevice predictionOfDevice;
    private JButton buttonConfig;

    public Frame()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("./resources/icons/tab_icon.png").getImage());
        setLayout(new BorderLayout());

		//getContentPane().setFont(new Font("sans-serif", Font.PLAIN, 16));
        initComponents();
        setTheme("Por defecto");

        pack();
    }

    private void initComponents()
    {
        titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        add(titlePanel, BorderLayout.NORTH);

        buttonConfig = new JButton(setIcon("./resources/icons/config.png", 20));
        buttonConfig.addActionListener((ActionEvent e) -> {
            SettingsDialog settingsDialog = new SettingsDialog(this, true);
            settingsDialog.setLocationRelativeTo(this);
            settingsDialog.setVisible(true);
        });
        titlePanel.add(buttonConfig);

        JLabel titleProgram = new JLabel("VME-Manager");
        //titleProgram.setFont(new Font("sans-serif", Font.BOLD, 32));
        titlePanel.add(titleProgram);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt)
            {
                if (tabbedPane.getSelectedIndex() == 3) {
					deviceCleaning.scanGarbage();
                }
            }
        });
        add(tabbedPane, BorderLayout.CENTER);

        fileAnalysis = new FileAnalysis(tabbedPane);
        scrollPaneFileAnalysis = new JScrollPane(fileAnalysis);
        tabbedPane.addTab("Análisis de datos",
            setIcon("./resources/icons/tab_icon.png", 10),
            scrollPaneFileAnalysis);

        networkState = new NetworkState(tabbedPane);
        scrollPaneNetworkState = new JScrollPane(networkState);
        tabbedPane.addTab("Estado de la red",
            setIcon("./resources/icons/tab_icon.png", 10),
            scrollPaneNetworkState);

        hardwareIntegrity = new HardwareIntegrity(tabbedPane);
        scrollPaneHardwareIntegrity = new JScrollPane(hardwareIntegrity);
        tabbedPane.addTab("Integridad de hardware",
            setIcon("./resources/icons/tab_icon.png", 10),
            scrollPaneHardwareIntegrity);

        deviceCleaning = new DeviceCleaning(tabbedPane);
        scrollPaneDeviceCleaning = new JScrollPane(deviceCleaning);
        tabbedPane.addTab("Limpieza del dispositivo",
            setIcon("./resources/icons/tab_icon.png", 10),
            scrollPaneDeviceCleaning);

        /*
         * Estas son funcionalidades aún más complejas de lo que son, por lo
         * tanto son elementos que van apareciendo poco a poco dentro del
         * proyecto y que por ahora no de van a implementar
         */
		predictionOfDevice = new PredictionOfDevice(tabbedPane);
        scrollPanePredictionOfDevice = new JScrollPane(predictionOfDevice);
        tabbedPane.addTab("Pronósticos *",
            setIcon("./resources/icons/tab_icon.png", 10),
            scrollPanePredictionOfDevice);
        tabbedPane.setEnabledAt(4, false);

		optimizationMethodology = new OptimizationMethodology(tabbedPane);
        scrollPaneOptimizationMethodology = new JScrollPane(optimizationMethodology);
        tabbedPane.addTab("Optimización *",
            setIcon("./resources/icons/tab_icon.png", 10),
            scrollPaneOptimizationMethodology);
        tabbedPane.setEnabledAt(5, false);

        bottomPanel = new JPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private ImageIcon setIcon(String path, int size)
    {
        ImageIcon imageIcon = new ImageIcon(path);
        Image img = imageIcon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public void setTheme(String theme)
    {
        try {

            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            switch (theme) {
            case "Oscuro":
				/*

                // Colores Nimbus específicos
                UIManager.put("nimbusFocus", new Color(100, 150, 200));

                // Colores de bordes y selección
                UIManager.put("nimbusBorder", new Color(100, 100, 100));
                UIManager.put("nimbusSelectionBackground", new Color(70, 130, 180));
                UIManager.put("nimbusSelectionForeground", Color.WHITE);

                // Colores de botones
                UIManager.put("Button.background", new Color(70, 73, 75));
                UIManager.put("Button.foreground", new Color(220, 220, 220));

                // Colores de textos y áreas de texto
                UIManager.put("TextArea.background", new Color(43, 43, 43));
                UIManager.put("TextArea.foreground", new Color(220, 220, 220));
                UIManager.put("TextArea.caretForeground", Color.WHITE);

                UIManager.put("TextField.background", new Color(43, 43, 43));
                UIManager.put("TextField.foreground", new Color(220, 220, 220));
				*/
                UIManager.put("control", new Color(60, 63, 65)); // Fondo de componentes
                UIManager.put("text", new Color(220, 220, 220)); // Color del texto
                UIManager.put("textText", new Color(220, 220, 220));
                UIManager.put("textForeground", new Color(220, 220, 220));
                UIManager.put("nimbusBase", new Color(70, 130, 180)); // Color base
                UIManager.put("nimbusBlueGrey", new Color(80, 80, 80)); // Fondo secundario
                UIManager.put("nimbusLightBackground", new Color(50, 50, 50)); // Fondo claro
                break;

            case "Claro":
                UIManager.put("control", new Color(240, 240, 240));
                UIManager.put("text", Color.BLACK);
                UIManager.put("textText", Color.BLACK);
                UIManager.put("textForeground", Color.BLACK);
                UIManager.put("nimbusBase", new Color(50, 125, 200));
                UIManager.put("nimbusBlueGrey", new Color(200, 200, 200));
                UIManager.put("nimbusLightBackground", Color.WHITE);
                break;

            case "Por defecto":
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                break;

            default:
                System.err.println("Not valid theme");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Window w : Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(w);
        }
    }
}
