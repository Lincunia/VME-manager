package vme.manager.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
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
    private SettingsDialog settingsDialog;

    public Frame() {
        settingsDialog = new SettingsDialog(this, true);
        settingsDialog.load();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("./resources/icons/tab_icon.png").getImage());
        setLayout(new BorderLayout());

        initComponents();

        pack();
    }

    private void initComponents() {
        titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        add(titlePanel, BorderLayout.NORTH);

        buttonConfig = new JButton(setIcon("./resources/icons/config.png", 20));
        buttonConfig.addActionListener((ActionEvent e) -> {
            settingsDialog.setLocationRelativeTo(this);
            settingsDialog.setVisible(true);
        });
        titlePanel.add(buttonConfig);

        JLabel titleProgram = new JLabel("VME-Manager");
        titlePanel.add(titleProgram);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
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

    private ImageIcon setIcon(String path, int size) {
        ImageIcon imageIcon = new ImageIcon(path);
        Image img = imageIcon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
